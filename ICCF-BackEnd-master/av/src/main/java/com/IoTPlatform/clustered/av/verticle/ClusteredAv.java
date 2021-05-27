package com.IoTPlatform.clustered.av.verticle;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.SQLOptions;
import io.vertx.ext.sql.UpdateResult;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;

import static com.IoTPlatform.clustered.av.verticle.ActionHelper.*;
import static com.IoTPlatform.util.Constants.ADDRESS;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

public class ClusteredAv extends AbstractVerticle {

    private JDBCClient jdbc;

    @Override
    public void start(Future<Void> fut) {
    	
    	
       	final EventBus eventBus = vertx.eventBus();
    	
    	
        DeliveryOptions delOps = new DeliveryOptions()
        		.addHeader("location", "192.168.56.102:3306")
        		.addHeader("name", "DriveCycle_AVExepriment")
        		.addHeader("metric", "MPH")
        		.addHeader("url", "http://localhost:8087/assets/index.html");
        eventBus.publish(ADDRESS, "a value", delOps);
    	
    	
    	

        // Create a router object.
        Router router = Router.router(vertx);

        
  

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
        router.route().handler(CorsHandler.create("http://localhost:8081")
        		.allowedMethod(io.vertx.core.http.HttpMethod.GET)
        		.allowedMethod(io.vertx.core.http.HttpMethod.POST)
        		.allowedMethod(io.vertx.core.http.HttpMethod.OPTIONS)
        		.allowCredentials(true)
        		.allowedHeader("Access-Control-Allow-Method")
        		.allowedHeader("Access-Control-Allow-Origin")
        		.allowedHeader("Access-Control-Allow-Credentials")
        		.allowedHeader("Content-Type"));
        // Serve static resources from the /assets directory
   //     router.route("/assets/*").handler(StaticHandler.create("assets"));
     //   router.route("/*").handler(StaticHandler.create());
        

        
        
        router.get("/api/interactions").handler(this::getAll);
//        router.get("/api/interactions/:entryId").handler(this::getOne);
//        router.route("/api/interactions*").handler(BodyHandler.create());
//        router.post("/api/interactions").handler(this::addOne);
//        router.delete("/api/interactions/:entryId").handler(this::deleteOne);
//        router.put("/api/interactions/:entryId").handler(this::updateOne);

        ConfigRetriever retriever = ConfigRetriever.create(vertx);

        // Start sequence:
        // 1 - Retrieve the configuration
        //      |- 2 - Create the JDBC client
        //      |- 3 - Connect to the database (retrieve a connection)
        //              |- 4 - Create table if needed
        //                   |- 5 - Add some data if needed
        //                          |- 6 - Close connection when done
        //              |- 7 - Start HTTP server
        //      |- 9 - we are done!

        ConfigRetriever.getConfigAsFuture(retriever)
            .compose(config -> {
            	config.put("url", "jdbc:mysql://192.168.56.102:3306/AV_2018_12_11_15_34_55?user=root&password=c2wt");
                jdbc = JDBCClient.createShared(vertx, config, "AV_Experiment ");

                return connect()
                    .compose(connection -> {
                        Future<Void> future = Future.future();
                        createTableIfNeeded(connection)
                            .compose(this::createSomeDataIfNone)
                            .setHandler(x -> {
                                connection.close();
                                future.handle(x.mapEmpty());
                            });
                        return future;
                    })
                    .compose(v -> createHttpServer(config, router));

            })
            .setHandler(fut);
    }

    private Future<Void> createHttpServer(JsonObject config, Router router) {
        Future<Void> future = Future.future();
        vertx
            .createHttpServer()
            .requestHandler(router::accept)
            .listen(
                config.getInteger("HTTP_PORT", 8087),
                res -> future.handle(res.mapEmpty())
            );
        return future;
    }

    private Future<SQLConnection> connect() {
        Future<SQLConnection> future = Future.future();
        jdbc.getConnection(ar ->
            future.handle(ar.map(c ->
                    c.setOptions(new SQLOptions().setAutoGeneratedKeys(true))
                )
            )
        );
        return future;
    }

    private Future<Interaction> insert(SQLConnection connection, Interaction interaction, boolean closeConnection) {
        Future<Interaction> future = Future.future();
        String sql = "INSERT INTO `Table1.interaction` (ID18B, DataField) VALUES (?, ?)";
        connection.updateWithParams(sql,
            new JsonArray().add(interaction.getID18B()).add(interaction.getDataField()),
            ar -> {
                if (closeConnection) {
                    connection.close();
                }
                future.handle(
                    ar.map(res -> new Interaction(res.getKeys().getLong(0), interaction.getID18B(), interaction.getDataField()))
                );
            }
        );
        return future;
    }

    private Future<List<Interaction>> query(SQLConnection connection) {
        Future<List<Interaction>> future = Future.future();
        connection.query("SELECT * FROM `Table1.interaction` WHERE ID18B = 'COA' ", result -> {
                connection.close();
                future.handle(
                    result.map(rs -> rs.getRows().stream().map(Interaction::new).collect(Collectors.toList()))
                );
            }
        );
        return future;
    }

    private Future<Interaction> queryOne(SQLConnection connection, String entryId) {
        Future<Interaction> future = Future.future();
        String sql = "SELECT * FROM `Table1.interaction` WHERE entryId = ?";
        connection.queryWithParams(sql, new JsonArray().add(Integer.valueOf(entryId)), result -> {
            connection.close();
            future.handle(
                result.map(rs -> {
                    List<JsonObject> rows = rs.getRows();
                    if (rows.size() == 0) {
                        throw new NoSuchElementException("No interaction with entryId " + entryId);
                    } else {
                        JsonObject row = rows.get(0);
                        return new Interaction(row);
                    }
                })
            );
        });
        return future;
    }

    private Future<Void> update(SQLConnection connection, String entryId, Interaction interaction) {
        Future<Void> future = Future.future();
        String sql = "UPDATE `Table1.interaction` SET ID18B = ?, DataField = ? WHERE entryId = ?";
        connection.updateWithParams(sql, new JsonArray().add(interaction.getID18B()).add(interaction.getDataField())
                .add(Integer.valueOf(entryId)),
            ar -> {
                connection.close();
                if (ar.failed()) {
                    future.fail(ar.cause());
                } else {
                    UpdateResult ur = ar.result();
                    if (ur.getUpdated() == 0) {
                        future.fail(new NoSuchElementException("No interaction with entryId " + entryId));
                    } else {
                        future.complete();
                    }
                }
            });
        return future;
    }

    private Future<Void> delete(SQLConnection connection, String entryId) {
        Future<Void> future = Future.future();
        String sql = "DELETE FROM `Table1.interaction` WHERE entryId = ?";
        connection.updateWithParams(sql,
            new JsonArray().add(Integer.valueOf(entryId)),
            ar -> {
                connection.close();
                if (ar.failed()) {
                    future.fail(ar.cause());
                } else {
                    if (ar.result().getUpdated() == 0) {
                        future.fail(new NoSuchElementException("Unknown interaction " + entryId));
                    } else {
                        future.complete();
                    }
                }
            }
        );
        return future;
    }

    private Future<SQLConnection> createTableIfNeeded(SQLConnection connection) {
        Future<SQLConnection> future = Future.future();
        vertx.fileSystem().readFile("tables.sql", ar -> {
            if (ar.failed()) {
                future.fail(ar.cause());
            } else {
                connection.execute(ar.result().toString(),
                    ar2 -> future.handle(ar2.map(connection))
                );
            }
        });
        return future;
    }

    private Future<SQLConnection> createSomeDataIfNone(SQLConnection connection) {
        Future<SQLConnection> future = Future.future();
        connection.query("SELECT * FROM `Table1.interaction`", select -> {
            if (select.failed()) {
                future.fail(select.cause());
            } else {
                if (select.result().getResults().isEmpty()) {
                    Interaction interaction1 = new Interaction("Fallacies of distributed computing",
                        "https://en.wikipedia.org/wiki/Fallacies_of_distributed_computing");
                    Interaction interaction2 = new Interaction("Reactive Manifesto",
                        "https://www.reactivemanifesto.org/");
                    Future<Interaction> insertion1 = insert(connection, interaction1, false);
                    Future<Interaction> insertion2 = insert(connection, interaction2, false);
                    CompositeFuture.all(insertion1, insertion2)
                        .setHandler(r -> future.handle(r.map(connection)));
                } else {
                    future.complete(connection);
                }
            }
        });
        return future;
    }


    // ---- HTTP Actions ----

    private void getAll(RoutingContext rc) {
        connect()
            .compose(this::query)
            .setHandler(ok(rc));
    }

    private void addOne(RoutingContext rc) {
        Interaction interaction = rc.getBodyAsJson().mapTo(Interaction.class);
        connect()
            .compose(connection -> insert(connection, interaction, true))
            .setHandler(created(rc));
    }


    private void deleteOne(RoutingContext rc) {
        String entryId = rc.pathParam("entryId");
        connect()
            .compose(connection -> delete(connection, entryId))
            .setHandler(noContent(rc));
    }


    private void getOne(RoutingContext rc) {
        String entryId = rc.pathParam("entryId");
        connect()
            .compose(connection -> queryOne(connection, entryId))
            .setHandler(ok(rc));
    }

    private void updateOne(RoutingContext rc) {
        String entryId = rc.request().getParam("entryId");
        Interaction interaction = rc.getBodyAsJson().mapTo(Interaction.class);
        connect()
            .compose(connection -> update(connection, entryId, interaction))
            .setHandler(noContent(rc));
    }

}