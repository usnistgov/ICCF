package com.IoTPlatform.clustered.capmanager.verticle;

/**
 * Hello world!
 *
 */
import java.util.LinkedHashMap;
import java.util.Map;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.core.Future;

/*
 * 
 */


import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import lombok.extern.slf4j.Slf4j;
//import static com.IoTPlatform.util.Constants.ADDRESS;

@Slf4j
public class ClusteredCapmanager extends AbstractVerticle {
	
	
	
	 public static final String ADDRESS = "IoTPlatform.com";
	 
	 private String  metric = "MPH";
//	 private String  address = null;
//	 private String  classname = null;
//	 private String  messageheaders = null;
	 private String  servicename = null;
	 private String  servicelocation = null;
	// private String  servicemetric = null;
	 private String  serviceurl = null;
    @Override
    public void start(Future<Void> fut) {
    	
    	
        final EventBus eventBus = vertx.eventBus();

        
        eventBus.consumer(ADDRESS, result -> {
        	

//        	address = result.address().toString();
//        	classname = result.getClass().toString();
      //  	messageheaders = result.headers().toString();
        	
        	
        	metric = result.body().toString();
     
       	//	    Cap.setName(servicename);
        //		Cap.setLocation(servicelocation);
  
        		
        		
      
        	
        	
        	
   //     log.debug("metric " + metric+  "  address() " +address + "  getClass()  " + classname+"   headers()   " +messageheaders +"   name  " +servicename+"   location  " +servicelocation );	
        	
        	
        	
if(metric!=null) {
	
	log.debug("message received !");
        		
        	   	servicename = result.headers().get("name").toString();
            	servicelocation = result.headers().get("location").toString();
             //	servicemetric = result.headers().get("metric").toString();
            	serviceurl = result.headers().get("url").toString();
            	
            	
            	
            	metric = result.body().toString();
            	
            	Capability Cap = new Capability();
             		    Cap.setName(servicename);
               		Cap.setLocation(servicelocation);
               		Cap.setMetric(metric);
               		Cap.setUrl(serviceurl);
//                		
            if (CapMap.isEmpty())
            	
            {	
            	
	CapMap.put(Cap.getId(), Cap); 	

            }   
            
            else
                
            { 
            //	CapMap.put(Cap.getId(), Cap);
            	
        		CapMap.forEach((key, value)-> { 

        		
        			
        			
        			if ((value.getName().equals(Cap.getName()))) {
        				//CapMap.replace(key, Cap);
        				CapMap.remove(key);
       				CapMap.put(Cap.getId(), Cap); 
        			
        			//	CapMap.replace(key, value, Cap);
        				log.debug("replaced old value " + value.getMetric()    +  " by new value " + Cap.getMetric() + " for service " + Cap.getName() );

        				
        				
        			//	log.debug("service " + Cap.getName()    +  " exists already and the current value is "  + metric);

        			}
        			
        			
        			
        			else
        				
        			{   
        				
        				
      				log.debug("service " + Cap.getName()    +  " brand new => will be added ");
        				CapMap.put(Cap.getId(), Cap); 	
        			}

        		});
        		
           
            }
        		
   

        	}
        	
        	else {

     //   CapMap.clear();
		log.debug("waiting for incoming messages");
	
        	}
        });
      //  String x = eventBus.consumer(ADDRESS).handler(receivedMessage -> log.debug("Received message: {} ", receivedMessage.metric())).toString();
        
	//	createSomeData();
		// Create a router object.
		 Router router = Router.router(vertx);

	 	 router.route("/assets/*").handler(StaticHandler.create("assets"));
	
    	 router.get("/api/capabilities").handler(this::getAll);
	 
		// This line instructs the router to handle the GET requests on “/api/capabilities” by calling the getAll method. 
	    // We could have inlined the handler code, but for clarity reasons we have created getall method: 
	 
	    router.get("/api/capabilities/:id").handler(this::getOne);
	    router.put("/api/capabilities/:id").handler(this::updateOne);
	 
	  router.route("/api/capabilities*").handler(BodyHandler.create());
	 // router.post("/api/capabilities").handler(this::addOne);
	  router.delete("/api/capabilities/:id").handler(this::deleteOne);
	  
	  
	  router.route("/*").handler(StaticHandler.create());

	 
	 
    vertx
        .createHttpServer()
        .requestHandler(router::accept)
        .listen(
        		 // Retrieve the port from the configuration,
                // default to 8080.
        		
        config().getInteger("http.port", 8080),
        result -> {
          if (result.succeeded()) {
            fut.complete();

          } else {
            fut.fail(result.cause());
          }
        });
        
                       
    }

	private void getAll(RoutingContext routingContext) {
		  routingContext.response()
		      .putHeader("content-type", "application/json; charset=utf-8")
		      .end(Json.encodePrettily(CapMap.values()));
		}
	
	private void addOne(RoutingContext routingContext) {
		  final Capability cap = Json.decodeValue(routingContext.getBodyAsString(),
				  Capability.class);
		  CapMap.put(cap.getId(), cap);
		  routingContext.response()
		      .setStatusCode(201)
		      .putHeader("content-type", "application/json; charset=utf-8")
		      .end(Json.encodePrettily(cap));
		}
	
	
	
	
	
	
	private void checkStatus() 
	
	{
	
		// listen on the event bus for incoming hellos/names from verticles
		// store hellos/names in a map. 
		// check if the verticle that inserted that record is still alive. 
		// if not, kill the record inserted by that verticle.
		// if yes, do nothing.

		
		
		
	}

	
	
	
	
	
	
	
	private void deleteOne(RoutingContext routingContext) {
		  String id = routingContext.request().getParam("id");
		  if (id == null) {
		    routingContext.response().setStatusCode(400).end();
		  } else {
		    Integer idAsInteger = Integer.valueOf(id);
		    CapMap.remove(idAsInteger);
		  }
		  routingContext.response().setStatusCode(204).end();
		}
	
	
	
	
	  private void getOne(RoutingContext routingContext) {
		    final String id = routingContext.request().getParam("id");
		    if (id == null) {
		      routingContext.response().setStatusCode(400).end();
		    } else {
		      final Integer idAsInteger = Integer.valueOf(id);
		      Capability capability = CapMap.get(idAsInteger);
		      if (capability == null) {
		        routingContext.response().setStatusCode(404).end();
		      } else {
		        routingContext.response()
		            .putHeader("content-type", "application/json; charset=utf-8")
		            .end(Json.encodePrettily(capability));
		      }
		    }
		  }

		  private void updateOne(RoutingContext routingContext) {
		    final String id = routingContext.request().getParam("id");
		    JsonObject json = routingContext.getBodyAsJson();
		    if (id == null || json == null) {
		      routingContext.response().setStatusCode(400).end();
		    } else {
		      final Integer idAsInteger = Integer.valueOf(id);
		      Capability capability = CapMap.get(idAsInteger);
		      if (capability == null) {
		        routingContext.response().setStatusCode(404).end();
		      } else {
		        capability.setName(json.getString("name"));
		        capability.setLocation(json.getString("location"));
		        capability.setMetric(json.getString("metric"));
		        routingContext.response()
		            .putHeader("content-type", "application/json; charset=utf-8")
		            .end(Json.encodePrettily(capability));
		      }
		    }
		  }
	
	

	private Map<Integer, Capability> CapMap = new LinkedHashMap<>();
	
	
	
	
	
	// Create some product
	private void createSomeData() {
//		Capability Cap = new Capability("Caperature", "192.168.1.240:3001");
//		
//	//	CapMap.
//		
//		CapMap.put(Cap.getId(), Cap);
//	  Capability airq = new Capability("dust", "192.168.1.240:3002");
//	  CapMap.put(airq.getId(), airq);
//	  
//	  
//	  
	  
	}
	

}
