package com.eduardo.idealista.scrapper;

import com.eduardo.idealista.model.Flat;
import com.eduardo.idealista.model.SearchTerms;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

/**
 * Created by hedu on 6/04/17.
 */
public class SimpleIdealistaScrapper {

    List<SearchTerms> searchTerms;

    public SimpleIdealistaScrapper() {
        searchTerms = new ArrayList<>();
    }

    public SimpleIdealistaScrapper(List<SearchTerms> searchTerms) {
        if (searchTerms == null) {
            this.searchTerms = new ArrayList<>();
        }
        else {
            this.searchTerms = searchTerms;
        }
    }

    public List<Flat> searchFlats() {

        List<Flat> flats = new ArrayList<>();
        for (SearchTerms st: searchTerms) {

            int maxPrice = st.getMaxPrice();
            int minRooms = st.getMinRooms();
            boolean includeGroundFloor = st.isIncludeGroundFloor();
            boolean picturesRequired = st.isPicturesRequired();
            SearchTerms.PublishedPeriod publishedPeriod = st.getPublishedPeriod();

            for (String city: st.getZones().keySet()) {
                List<String> neighborhoods = st.getZones().get(city);
                for (String neighborhood: neighborhoods) {
                    System.out.println("-----neighborhood: " + neighborhood);
                    String url = IdealistaUrlGenerator.generateSearchUrl(
                            city, neighborhood, maxPrice, minRooms,
                            includeGroundFloor, picturesRequired, publishedPeriod);

                    System.out.println("url: " + url);

                    try {
                        Document doc = getDocument(url);
                        List<Flat> neighborhoodFlats = parseList(doc);

                        if (neighborhoodFlats != null && !neighborhoodFlats.isEmpty()) {
                            System.out.println("Total: " + neighborhoodFlats.size());
                            flats.addAll(neighborhoodFlats);
                        }
                    } catch (ConnectionException ce) {
                        System.out.println(ce.getMessage());
                        ce.printStackTrace(System.out);
                    }

                }
            }
        }
        return flats;
    }

    private List<Flat> parseList(Document doc) {
        List<Flat> flats = new ArrayList<>();

        Elements elements = doc.select("div.item-info-container");
        if (elements == null || elements.isEmpty()) {
            return null;
        }

        for (Element element : elements) {
            Element link = element.select("a.item-link").first();
            if (link != null) {
                String flatUrl = IdealistaUrlGenerator.getFlatUrl(link.attr("href"));
                System.out.println("flat url: " + flatUrl);
                try {
                    Document flatDoc = getDocument(flatUrl);

                    Flat flat = parseFlat(flatDoc);
                    if (flat != null) {
                        flats.add(flat);
                    }
                }
                catch (ConnectionException ce) {
                    System.out.println(ce.getMessage());
                    ce.printStackTrace(System.out);
                }
            }
        }
        return flats;
    }

    private Flat parseFlat(Document doc) {
        String url = doc.location();
        String title = null;
        int price = 0;
        int rooms = 0;
        int floor = 0;
        int size = 0;
        Element mainInfo = doc.select("section.main-info").first();
        if (mainInfo != null) {
            Element titleSpan = mainInfo.select("h1 > span.txt-bold").first();
            if (titleSpan != null) {
                title = titleSpan.html();
            }
            Elements infoData = mainInfo.select("span > span.txt-big");
            if (infoData != null && infoData.size() >= 4) {
                for (Element element: infoData) {
                    String parentHtml = element.parent().html();
                    if (parentHtml.contains("€")) {
                        price = Integer.parseInt(element.html());
                    }
                    else if (parentHtml.contains("hab")) {
                        rooms = Integer.parseInt(element.html());
                    }
                    else if (parentHtml.contains("interior") || parentHtml.contains("exterior")) {
                        String fl = element.html();
                        if (fl.equalsIgnoreCase("bajo")) {
                            floor = 0;
                        }
                        else {
                            floor = Integer.parseInt(fl.substring(0, fl.length() -1));
                        }
                    }
                    else if (parentHtml.contains("m")) {
                        size = Integer.parseInt(element.html());
                    }
                }
            }
            return new Flat(url,title, price, size, rooms, floor);
        }
        System.out.println("Fallo: " + doc);
        return null;
    }

    private Document getDocument(String url) throws ConnectionException {
        Document doc;
        try {
            doc = Jsoup.connect(url).timeout(30000).get();
        } catch (IOException e) {
            throw new ConnectionException("Problem connecting to url: " + url);
        }
        return doc;
    }

    private class ConnectionException extends Throwable {
        ConnectionException(String messsage) {
            super(messsage);
        }
    }
}
