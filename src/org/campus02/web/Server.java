package org.campus02.web;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {
        PageCache pageCache = new PageCache();
        // cache wird befüllt
        pageCache.warmUp("data/demo_urls.txt");

        WebProxy webProxy = new WebProxy(pageCache);

        System.out.println("start server on port 5678");
        try (ServerSocket server = new ServerSocket(5678)) {
            while (true) {
                System.out.println("waiting for client");
                try (Socket client = server.accept()) {
                    System.out.println("client connected");
                    new ClientHandler(client, webProxy).run();
                }
            }
        }
    }
}
