package com.IoTPlatform.clustered.weather.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import com.IoTPlatform.util.Constants;

import io.vertx.core.Future;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import static com.IoTPlatform.util.Constants.*;












/**
 * Hello world!
 *
 */
@Slf4j
public class ClusteredWeather extends AbstractVerticle {

	
	
	
	
	

    // Label on dust data
 

    @Override
    public void start(Future<Void> fut) {
    final String TYP_FS = "{typ=\"fs\"}";
   	final EventBus eventBus = vertx.eventBus();

 //   eventBus.publish(ADDRESS, message);
    
    
    
   
    
    
    
  
    
 //   eventBus.publish(ADDRESS,"salut salut " , delOps);
    

      Map<String,String> values = new HashMap<>();

      // Create a router object.
      Router router = Router.router(vertx);

      // We need the body, so we need to enable it.
      router.route("/").handler(BodyHandler.create());

      // Bind "/" to our fs-input
      router.post("/").handler(routingContext -> {
        // Get the sensor id from the header
        String tmp = routingContext.request().getHeader("X-Sensor");
        String sensor_id = tmp.replace('-', '_');
        log.debug("Sensor Id: " + sensor_id);
     
        
        
        JsonObject body_json = routingContext.getBodyAsJson();
        
        
        
        
        JsonArray value_array = body_json.getJsonArray("sensordatavalues");
        log.debug(value_array.toString());
        // value_type  value

        value_array.forEach((elem) -> {
        	
        	
            JsonObject jo = (JsonObject)elem;
            String key = jo.getString("value_type");
            String value = jo.getString("value");
            String url = null;
       	  
            if (key=="temperature") {
            	
            	url = "http://localhost:3000/d/xfceu3liz/temperature?panelId=2&fullscreen&orgId=1&kiosk";
            	
            	
            	  // block of code to be executed if condition1 is true
            	} else if (key=="humidity") {
            	  // block of code to be executed if the condition1 is false and condition2 is true
            		url = "http://localhost:3000/d/xfceu3liz/temperature?panelId=2&fullscreen&orgId=1&kiosk";
            		
       
            	
            	} else if (key=="SDS_P1") {
            		
            		url = "http://localhost:3000/d/xfceu3liz/temperature?panelId=2&fullscreen&orgId=1&kiosk";
            		
            	} else if (key=="SDS_P2") {
            		url = "http://localhost:3000/d/xfceu3liz/temperature?panelId=2&fullscreen&orgId=1&kiosk";
            		
            	} else {
            		
            		
            		url = "http://localhost:3000/d/xfceu3liz/temperature?panelId=2&fullscreen&orgId=1&kiosk";
            		
            	       }
            
            
            DeliveryOptions delOps = new DeliveryOptions()
            		.addHeader("location", "Building1")
            		.addHeader("name",key)
            		.addHeader("metric", value.toString())
            		.addHeader("url", url);    
            
        	
        	
        	
  
         // DeliveryOptions delOps = new DeliveryOptions().addHeader("location", "localhost").addHeader("name", key);
          
            
           
           // if (key!="samples" || key!="min_micro" ||  key!="min_micro" )
           // {
            eventBus.publish(ADDRESS, value, delOps);
            log.debug("Key : " + key  + " Value : " + value);
            
         //   }
          
        
          
       //   if (true)
         // {
        	  
        //	  log.info("temperatuuuuure    "+ value);
          
          
//          
//          if ( key.equals("temperature") )
//        	  
//          {  
//        	  
//        	  
//        	  delOps.addHeader("name", Constants.temperatureCapability) ;
//      
//     //    
//     //     log.info("it wooooooooooooooorks");
//          
//          
//          }
//          
//          
//          
//     if ( key.equals("humidity") )
//        	  
//          {  
//    	 
//    	 
//    	 delOps.addHeader("name", Constants.humidityCapability) ;
//          eventBus.publish(ADDRESS, value, delOps);
//     //    
//     //     log.info("it wooooooooooooooorks");
//          
//          
//          }
    
          
          
          
          
          
          
          if (value.contains("dBm")) {
            value = value.substring(0,value.indexOf(" "));
          }
          values.put(sensor_id + "_" +key,value);
      
         
          
          
          
          
        });
     

        HttpServerResponse response = routingContext.response();
        response.setStatusCode(200)
            .end();
      });


      /*
       * Now the listener for scraping of data
       */
      router.get("/metrics").handler(routingContext -> {

    	  log.debug("Request from " + routingContext.request().remoteAddress().toString());
        HttpServerResponse response = routingContext.response();

        // If we have no data yet, return a 204 'no content'
        if (values.isEmpty()) {
          response.setStatusCode(204)
              .end();
          return;
        }

        StringBuilder builder = new StringBuilder();
        values.forEach((k,v) -> {
          builder.append("# TYPE ").append(k).append(" gauge\n");
          if (k.contains("P1")) {
            builder.append("# HELP ").append(k).append(" PM 10 Wert\n");
            builder.append(k).append(TYP_FS + " ").append(v).append("\n");
          }
          else if (k.contains("P2")) {
            builder.append("# HELP ").append(k).append(" PM 2.5 Wert\n");
            builder.append(k).append(TYP_FS + " ").append(v).append("\n");
          }
          else{
            builder.append(k).append(" ").append(v).append("\n");
          }
        });

        response.setStatusCode(200)
            .putHeader("content-type","text/plain; version=0.0.4")
            .end(builder.toString());
      });


      // Create the HTTP server and pass the "accept" method to the request handler.
      vertx
          .createHttpServer()
          .requestHandler(router::accept)
          .listen(
              // Retrieve the port from the configuration,
              // default to 10080.
              config().getInteger("http.port", 10080),
              result -> {
                if (result.succeeded()) {
                  fut.complete();
                } else {
                  fut.fail(result.cause());
                }
              }
          );
    }  
       
}
