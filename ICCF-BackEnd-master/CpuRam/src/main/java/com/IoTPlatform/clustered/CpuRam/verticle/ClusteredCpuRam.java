package com.IoTPlatform.clustered.CpuRam.verticle;


import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

import io.vertx.core.AbstractVerticle;

import io.vertx.core.Future;
import io.vertx.core.Handler;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;


import java.util.HashMap;
import java.util.HashSet;

import java.util.Map;

import java.util.Set;




public class ClusteredCpuRam extends AbstractVerticle {

	   private Map<String, JsonObject> products = new HashMap<>();
	   
    @Override
    public void start(Future<Void> fut) {


		  
	
	   //   setUpInitialData();

	      Router router = Router.router(vertx);

	      router.route().handler(BodyHandler.create());
	      
	   
	      //router.get("/api/cpuramload2").handler(this::handleListProducts);
	      
	      
	      Set<String> allowedHeaders = new HashSet<>();
	      allowedHeaders.add("x-requested-with");
	      allowedHeaders.add("Access-Control-Allow-Origin");
	      allowedHeaders.add("origin");
	      allowedHeaders.add("Content-Type");
	      allowedHeaders.add("accept");
	      allowedHeaders.add("X-PINGARUNER");

	      Set<HttpMethod> allowedMethods = new HashSet<>();
	      allowedMethods.add(HttpMethod.GET);
	      allowedMethods.add(HttpMethod.POST);
	      allowedMethods.add(HttpMethod.OPTIONS);
	      /*
	       * these methods aren't necessary for this sample, 
	       * but you may need them for your projects
	       */
	      allowedMethods.add(HttpMethod.DELETE);
	      allowedMethods.add(HttpMethod.PATCH);
	      allowedMethods.add(HttpMethod.PUT);
	      router.route().handler(CorsHandler.create("http://localhost:8093")
	      		.allowedMethod(io.vertx.core.http.HttpMethod.GET)
	      		.allowedMethod(io.vertx.core.http.HttpMethod.POST)
	      		.allowedMethod(io.vertx.core.http.HttpMethod.OPTIONS)
	      		.allowCredentials(true)
	      		.allowedHeader("Access-Control-Allow-Method")
	      		.allowedHeader("Access-Control-Allow-Origin")
	      		.allowedHeader("Access-Control-Allow-Credentials")
	      		.allowedHeader("Content-Type"));
	      
	      
	      router.get("/products").handler(this::handleListProducts);

	   //   router.get("api/cpuramload").handler(this::handleListProducts);

	      
	   //   vertx.createHttpServer().requestHandler(router).listen(8088);
  
	      vertx.createHttpServer().requestHandler(router::accept).listen(8099);
}


    private void handleListProducts(RoutingContext routingContext) {
    	
    	
    	SystemInfo si = new SystemInfo();
    	CentralProcessor processor = si.getHardware().getProcessor();

		  final long timeInterval = 1000;

    	
		  Runnable runnable = new Runnable() {
			  
			  
			  
			  
		  public void run() {
		    while (true) {
		    	
		    	
		    	System.out.println(processor.getSystemCpuLoad());
		    	
		    	
		    	
		    	
		        JsonArray arr = new JsonArray();
		       
                 arr.add(processor.getSystemCpuLoad());
		         routingContext.response().putHeader("content-type", "application/json").end(arr.encodePrettily());
		    	
		    	
		      try {
		       
		    	  
		    	  Thread.sleep(timeInterval);
		      } catch (InterruptedException e) {
		        e.printStackTrace();
		      }
		      }
		    }
		  };	
		  Thread thread = new Thread(runnable);
		  thread.start();
    	
    	
    	
		  

    }

//    private void setUpInitialData() {
//      addProduct(new JsonObject().put("price", 3.99));
//    }
//    private void addProduct(JsonObject product) {
//      products.put(product.getString("id"), product);
//    }

}