package org.campus02.web;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class PageCache {

    public static void main(String[] args) throws CacheMissException {
        PageCache pageCache = new PageCache();
        pageCache.warmUp("data/demo_urls.txt");
        System.out.println(pageCache.cache.keySet());
        System.out.println(pageCache.readFromCache("https://www.orf.at"));
    }
    private HashMap<String, WebPage> cache = new HashMap<>();

    // leerer Konstruktor -> nicht notwendig -> wird von java selbst erstellt sofern kein anderer Konstruktor vorhanden
    //public PageCache() {
    //}


    public HashMap<String, WebPage> getCache() {
        return cache;
    }

    public WebPage readFromCache(String url) throws CacheMissException {
        if (!cache.containsKey(url)) {
            throw new CacheMissException("url not in cache");
        }
        return cache.get(url);
    }

    public void writeToCache(WebPage webPage) {
        cache.put(webPage.getUrl(), webPage);
    }

    /**
     * lese alle URLs aus dem demo_urls file und füge sie dem cache hinzu
     * es wird sichergestellt, dass alle urls bearbeitet werden
     *
     * @param pathToUrls
     */
    public void warmUp(String pathToUrls) {
        try (BufferedReader br = new BufferedReader(new FileReader(pathToUrls))) {
            String url;
            while ((url = br.readLine()) != null) {
                try {
                    WebPage webPage = UrlLoader.loadWebPage(url);
                    writeToCache(webPage);
                } catch (UrlLoaderException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
