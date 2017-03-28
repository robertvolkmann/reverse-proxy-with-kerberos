package com.rvolkmann.kerberos;

import org.apache.camel.builder.RouteBuilder;

import java.util.Optional;

public class HttpRouteBuilder extends RouteBuilder {

    public void configure() {
        from("netty4-http:http://0.0.0.0:8080")
                .process(exchange -> {
                    Optional<String> header = Optional.ofNullable((String) exchange.getIn().getHeader("X-Forwarded-User"));
                    exchange.getOut().setBody(header.orElse("Header X-Forwarded-User is missing."));
                });
    }
}
