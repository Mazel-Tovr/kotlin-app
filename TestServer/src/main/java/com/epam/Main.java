package com.epam;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello there");
        TestServer localServer = new TestServer();
        localServer.startServer();
        Runtime.getRuntime().addShutdownHook(new Thread(localServer::stopServe));
    }
}
