package org.campus02.web;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket client;
    private WebProxy webProxy;

    public ClientHandler(Socket client, WebProxy webProxy) {
        this.client = client;
        this.webProxy = webProxy;
    }

    private void start() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()))) {
            String input;

            while ((input = br.readLine()) != null) {
                if (input.equalsIgnoreCase("bye")) {
                    System.out.println("client wants to exit");
                    bw.write("good bye!");
                    bw.newLine();
                    bw.flush();
                    client.close(); // es wird eine Fehlermeldung geworfen oder mit break beenden
                }

                String[] cmds = input.split(" ");
                if (cmds.length != 2) {
                    bw.write("error: invalid command");
                    bw.newLine();
                    bw.flush();
                    continue;
                }

                // [0 -> "fetch | stats", 1 -> value]
                switch (cmds[0]) {
                    case "fetch":
                        try {
                            WebPage webPage = webProxy.fetch(cmds[1]);
                            bw.write(webPage.getContent());
                        } catch (UrlLoaderException e) {
                            bw.write("error: loading url failed");
                        }
                        break;
                    case "stats":
                        if (cmds[1].equals("hits")) {
                            // schicken wir die Anzahl der hits retour
                            bw.write(webProxy.statsHits());
                        } else if (cmds[1].equals("misses")) {
                            // schicken wir die Anzahl der misses retour
                            bw.write(webProxy.statsMisses());
                        } else {
                            // falsches cmd geschickt
                            bw.write("error: invalid command");
                        }
                        break;
                    default:
                        bw.write("error: invalid command");
                }

                bw.newLine();
                bw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        start();
    }
}
