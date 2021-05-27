package com.IoTPlatform.clustered.sender.verticle;


import com.IoTPlatform.helper.HttpServerHelper;
import com.IoTPlatform.helper.RouterHelper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import static com.IoTPlatform.util.Constants.*;

@Slf4j
public class ClusteredSender extends AbstractVerticle {

    /**
     *
     * @param future
     */
	
	//private Message<>  message = null;
	
	
	
    @Override
    public void start(Future<Void> future) {
//        final Router router = RouterHelper.createRouter(vertx, "Hello from clustered messenger example!");
//      // router.post("/sendForAll/:" + PATH_PARAM_TO_RECEIVE_MESSAGE).handler(this::sendMessageForAllReceivers);
//        router.post().handler(this::sendMessageForAllReceivers);
//         
//        
//        HttpServerHelper.createAnHttpServer(vertx, router, config(), future);
    	
    	RoutingContext routingContext;
    	 final EventBus eventBus = vertx.eventBus();
         //  final String message = routingContext.request().getParam(PATH_PARAM_TO_RECEIVE_MESSAGE);
           final String message = "Templater Sender ";
           
           
         
           
           
           eventBus.publish(ADDRESS, message);
         //  log.info("Current Thread Id {} Is Clustered {} ", Thread.currentThread().getId(), vertx.isClustered());
       //    routingContext.response().end(message);
    	
    }

    /**
     *
     * @param routingContext
     */
//    private void sendMessageForAllReceivers(RoutingContext routingContext){
//        final EventBus eventBus = vertx.eventBus();
//      //  final String message = routingContext.request().getParam(PATH_PARAM_TO_RECEIVE_MESSAGE);
//        final String message = "nayda";
//        eventBus.publish(ADDRESS, message);
//        log.info("Current Thread Id {} Is Clustered {} ", Thread.currentThread().getId(), vertx.isClustered());
//        routingContext.response().end(message);
//    }
}
