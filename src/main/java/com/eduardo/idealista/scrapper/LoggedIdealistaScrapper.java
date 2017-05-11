package com.eduardo.idealista.scrapper;

import com.eduardo.idealista.model.Flat;
import com.eduardo.idealista.model.SearchTerms;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.codehaus.groovy.tools.groovydoc.Main.execute;

/**
 * Created by hedu on 6/04/17.
 */
public class LoggedIdealistaScrapper {

    final static String LOGIN_URL = "https://www.idealista.com/es/login.ajax";
    final static String SEARCH_LIST = "https://www.idealista.com/usuario/tus-alertas";
    Map<String, String> cookies;

    public LoggedIdealistaScrapper(String email, String password) throws ConnectionException {
        cookies = new HashMap<>();
        login(email, password);

    }

    public List<Flat> searchFlats() {

        List<Flat> flats = new ArrayList<>();
        try {
            Document searchList = getDocument(SEARCH_LIST);
            Elements links = searchList.select("table#searches td.description h3 a");
            List<String> urls = links.stream().map(link -> link.attr("href")).collect(Collectors.toList());
            for (String url : urls) {
                Document doc = getDocument(IdealistaUrlGenerator.getFlatUrl(url));
                List<Flat> searchFlats = parseListSimple(doc);

                if (searchFlats != null && !searchFlats.isEmpty()) {
                    flats.addAll(searchFlats);
                }
            }
        }
        catch (ConnectionException ce) {
            System.out.println(ce.getMessage());
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
            doc = Jsoup.connect(url)
                    .timeout(30000)
                    .cookies(cookies).get();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace(System.out);
            throw new ConnectionException("Problem connecting to url: " + url);
        }
        return doc;
    }

    private void login(String email, String password) throws ConnectionException {
        try {
            Connection connection = Jsoup.connect(LOGIN_URL)
                    .timeout(30000)
                    .method(Connection.Method.POST)
                    .ignoreContentType(true)
                    .data("email", email)
                    .data("password", password)
                    .data("rememberMe", "true")
                    .data("giveMeUrl", "true")
                    .data("device", "1813F93768895F5F621118EDF78A026D399D60FD3108C4EFEADDB8D992E7804C279C62");

            Connection.Response response = connection.execute();

            cookies = response.cookies();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace(System.out);
            throw new ConnectionException("Problem connecting to url: " + LOGIN_URL);
        }

    }

    private class ConnectionException extends Throwable {
        ConnectionException(String messsage) {
            super(messsage);
        }
    }
}
