package org.campus02.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlLoader {

    /**
     * for tests
     * @param args
     * @throws UrlLoaderException
     */
    public static void main(String[] args) throws UrlLoaderException {
        WebPage webPage = loadWebPage("https://www.news.at");
        System.out.println("webPage.getContent() = " + webPage.getContent());
    }

    public static WebPage loadWebPage(String url) throws UrlLoaderException {

        // 1. Variante
        //            try (BufferedReader br = new BufferedReader(
        //                    new InputStreamReader(new URL(url).openStream()))) {
        //
        //            }

        // 2. Variante
        try {
            URL webUrl = new URL(url);//STRG + ALT + v => erstellt variable

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(webUrl.openStream()))) {
                String content = "";
                String line;

                while ((line = br.readLine()) != null) {
                    content += line;
                }

                return new WebPage(url, content);
            } catch (IOException e) {
                throw new UrlLoaderException("error reading from url", e);
            }
        } catch (MalformedURLException e) {
            throw new UrlLoaderException("error reading from url", e);
        }
    }
}
