package com.experiment;

import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.core.Vertx;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Path("/events")
public class SSEResource {

    private static final String BUS_ADDRESS = "event_source";


    private final Vertx vertx;


    public SSEResource(Vertx vertx) {
        this.vertx = vertx;
    }

    // Demonstration of SSE randomly generated data
    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @Path("/stream/{name}")
    public Multi<String> stream(@PathParam String name) {
        return vertx.periodicStream(2000).toMulti()
                .map(l -> String.format("Hello %s! (%s)%n", name, new Date()));
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/message/{message}")
    public String message(@PathParam String message) {
        vertx.eventBus().publish(BUS_ADDRESS, message);
        return "message published successfully";
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/automated/message")
    public Response message() {
        var eb = vertx.eventBus();
        var time = LocalDateTime.now();

        vertx.setPeriodic(15000, v -> eb.publish(BUS_ADDRESS, generateRandomMessage(time)));
        return Response.ok("initiated at " + time).build();
    }

    private String generateRandomMessage(LocalDateTime time) {
        return "publishing message " + time + " " + UUID.randomUUID().toString();
    }

    // Demonstration of SSE using event bus
    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @Path("/stream")
    public Multi<String> streamEvents() {
        return vertx.eventBus().consumer(BUS_ADDRESS).toMulti().map(item -> item.body().toString());
    }

}
