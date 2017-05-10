package com.eduardo.idealista.scrapper;

import com.eduardo.idealista.model.Flat;
import com.eduardo.idealista.model.SearchTerms;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.*;

/**
 * Created by hedu on 6/04/17.
 */
public class AnonymousIdealistaScrapper {

    List<SearchTerms> searchTerms;

    public AnonymousIdealistaScrapper() {
        searchTerms = new ArrayList<>();
    }

    public AnonymousIdealistaScrapper(List<SearchTerms> searchTerms) {
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
                    String url = IdealistaUrlGenerator.generateSearchUrl(
                            city, neighborhood, maxPrice, minRooms,
                            includeGroundFloor, picturesRequired, publishedPeriod);

                    try {
                        Document doc = getDocument(url);
                        List<Flat> neighborhoodFlats = parseListSimple(doc);

                        if (neighborhoodFlats != null && !neighborhoodFlats.isEmpty()) {
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

    private List<Flat> parseListSimple(Document doc) {
        List<Flat> flats = new ArrayList<>();

        Elements elements = doc.select("div.item div.item-info-container");
        if (elements == null || elements.isEmpty()) {
            return null;
        }

        for (Element element : elements) {
            Element link = element.select("a.item-link").first();
            if (link != null) {
                String flatUrl = IdealistaUrlGenerator.getFlatUrl(link.attr("href"));
                String title = link.text();
                int price = 0;
                int floor = 0;
                int rooms = 0;
                int size = 0;

                Element elementPrice = element.select("span.item-price").first();
                if (elementPrice != null) {
                    price = Integer.valueOf(
                            element.select("span.item-price").text().replace("€/mes", ""));
                }

                Elements itemDetails = element.select("span.item-detail");
                if (itemDetails != null) {
                    for (Element detail: itemDetails) {
                        String text = detail.text();
                        if (text.contains("hab")) {
                            rooms = Integer.parseInt(text.split(" ")[0]);
                        }
                        else if (text.contains("exterior") || text.contains("interior")) {
                            if (text.contains("Bajo") || text.contains("Entreplanta") ) {
                                floor = 0;
                            }
                            else {
                                System.out.print(text);
                                floor = Integer.parseInt(text.substring(0, text.indexOf("ª")));
                            }
                        }
                        else if (text.contains("m²")){
                            size = Integer.parseInt(text.split(" ")[0]);
                        }
                    }
                }
                Flat flat = new Flat(flatUrl, title, price, size, rooms, floor);
                flats.add(flat);
            }
        }
        return flats;
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
