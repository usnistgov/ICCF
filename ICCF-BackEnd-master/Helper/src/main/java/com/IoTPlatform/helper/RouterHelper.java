package com.IoTPlatform.helper;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;


public class RouterHelper {

    private RouterHelper(){}

    /**
     *
     * @param vertx
     * @param welcomeMessage
     * @return
     */
    public static Router createRouter(Vertx vertx, String welcomeMessage){
        final Router router = Router.router(vertx);
        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "text/html").end("<h1>" + welcomeMessage + "</h1>");
        });

        return router;
    }
}
