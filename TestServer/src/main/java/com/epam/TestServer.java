package com.epam;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class TestServer {

    private HttpServer httpServer;

    public void startServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress("localhost",1234), 0);

        httpServer.createContext("/test", httpExchange -> {
            String response = "This is the response";
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream responseBody = httpExchange.getResponseBody();
            responseBody.write(response.getBytes());
            responseBody.close();
        });

        httpServer.start();
        System.out.println("Server host" + httpServer.getAddress());
    }

    public void stopServe() {
        httpServer.stop(1);
    }

}
