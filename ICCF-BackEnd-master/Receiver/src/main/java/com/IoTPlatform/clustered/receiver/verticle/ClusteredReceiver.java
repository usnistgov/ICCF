package com.IoTPlatform.clustered.receiver.verticle;

/*
 * 
 */

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
//import static com.IoTPlatform.util.Constants.ADDRESS;

@Slf4j
public class ClusteredReceiver extends AbstractVerticle {
	 public static final String ADDRESS = "IoTPlatform.com";
    @Override
    public void start() {
        final EventBus eventBus = vertx.eventBus();
        
        eventBus.consumer(ADDRESS, receivedMessage -> log.debug("Received message: {} ", receivedMessage.body()));
        log.info("Receiver ready!");
       
    }
}
