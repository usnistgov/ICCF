package com.IoTPlatform.helper;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

import static com.IoTPlatform.util.Constants.*;



@Slf4j
public class HttpServerHelper {

    private HttpServerHelper() {}

    /**
     *
     * @param vertx
     * @param router
     * @param config
     * @param future
     */
    public static void createAnHttpServer(Vertx vertx, Router router, JsonObject config, Future<Void> future){
        createAnHttpServer(vertx, router, config, DEFAULT_HTTP_PORT, future);
    }

    /**
     *
     * @param vertx
     * @param router
     * @param config
     * @param port
     * @param future
     */
    public static void createAnHttpServer(Vertx vertx, Router router, JsonObject config, int port, Future<Void> future){
        vertx.createHttpServer().requestHandler(router::accept)
                .listen(config.getInteger("http.server.port", port), result -> {
                    if (result.succeeded()) {
                        log.info("HTTP server running on port {} ", port);
                        future.complete();
                    } else {
                        log.error("Could not start a HTTP server", result.cause());
                        future.fail(result.cause());
                    }
                });

    }


}
