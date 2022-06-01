package org.campus02.web;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WebProxy {

    public static void main(String[] args) {
        WebProxy webProxy = new WebProxy();
        webProxy.cache.warmUp("data/demo_urls.txt");
        webProxy.writePageCacheToFile("data/cached_urls.txt");
    }
    private PageCache cache;
    private int numCacheHits = 0;
    private int numCacheMisses = 0;

    public WebProxy() {
        this.cache = new PageCache();
    }

    public WebProxy(PageCache cache) {
        this.cache = cache;
    }

    public WebPage fetch(String url) throws UrlLoaderException {
        WebPage webPage;
        try {
            // lade URL vom cache und erhöhe numCacheHits
            webPage = cache.readFromCache(url);
            numCacheHits++;
        } catch (CacheMissException e) {
            // falls die URL noch nicht im cache ist, neu laden und numCacheMisses erhöhen
            numCacheMisses++;
            webPage = UrlLoader.loadWebPage(url);
            cache.writeToCache(webPage);
        }
        return webPage;
    }

    public String statsHits() {
        return "stats hits: " + this.numCacheHits;
    }

    public String statsMisses() {
        return "stats misses: " + this.numCacheMisses;
    }

    public boolean writePageCacheToFile(String pathToFile) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(pathToFile))) {
            HashMap<String, WebPage> webCache = cache.getCache();
            //for (String key : webCache.keySet()) { // -> verwende alle keys

            //iter
            for (Map.Entry<String, WebPage> urlWebPage : webCache.entrySet()) {
                bw.write(urlWebPage.getKey() + ";" + urlWebPage.getValue().getContent());
                bw.newLine();
            }
            bw.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
