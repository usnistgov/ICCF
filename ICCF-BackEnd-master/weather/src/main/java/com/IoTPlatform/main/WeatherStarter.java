package com.IoTPlatform.main;

/*
 * 
 */

import com.IoTPlatform.clustered.weather.verticle.ClusteredWeather;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WeatherStarter {

    /**
     *
     * @param args
     */
    public static void main(String[] args){

        final ClusterManager mgr = new HazelcastClusterManager();
        final VertxOptions options = new VertxOptions().setClusterManager(mgr);
        Vertx.clusteredVertx(options, cluster -> {
            if (cluster.succeeded()) {
                cluster.result().deployVerticle(ClusteredWeather.class.getName(), res -> {
                    if(res.succeeded()){
                        log.info("Deployment id is: " + res.result());
                    } else {
                        log.error("Deployment failed!");
                    }
                });
            } else {
                log.error("Cluster up failed: " + cluster.cause());
            }
        });
   

    }
}
