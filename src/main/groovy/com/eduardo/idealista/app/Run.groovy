package com.eduardo.idealista.app

import com.eduardo.idealista.mail.MailSender
import com.eduardo.idealista.model.Flat;
import com.eduardo.idealista.model.SearchTerms
import com.eduardo.idealista.scrapper.AnonymousIdealistaScrapper;
import com.eduardo.idealista.settings.Configuration

/**
 * Created by hedu on 9/04/17.
 */
public class Run {

    private static Set<Flat> previousFlats = new HashSet<>()
    // 5 minutes
    private static final long MILLIS = 60000 * 5

    public static void main(String[] args) {

        Configuration conf = new Configuration();
        def zones = conf.getMap(Configuration.FILTER_ZONES);

        SearchTerms st = new SearchTerms(
                zones,
                conf.getInt(Configuration.FILTER_MAX_PRICE),
                conf.getInt(Configuration.FILTER_MIN_ROOMS),
                conf.getBoolean(Configuration.FILTER_INCLUDE_GROUND_FLOOR),
                conf.getBoolean(Configuration.FILTER_PICTURES_REQUIRED),
                SearchTerms.getPublishedPeriod(conf.get(Configuration.FILTER_PUBLISHED_PERIOD))
        );
        AnonymousIdealistaScrapper anonymousIdealistaScrapper = new AnonymousIdealistaScrapper([st])

        MailSender mailSender = new MailSender(
                conf.get(Configuration.MAIL_FROM),
                conf.get(Configuration.MAIL_PASSWORD)
        );

        while (true ) {
            def flats = anonymousIdealistaScrapper.searchFlats();
            def mailContent = ""

            flats.each { flat ->
                if (previousFlats.add(flat)) {
                    println(flat)
                    mailContent <<= flat.toString() << "\n"
                } else println "Flat: ${flat.url} already exists"
            }

            previousFlats = new HashSet<>();
            previousFlats.addAll(flats);

            if (mailContent != "") {
                List<String> receivers = conf.getList(Configuration.MAIL_RECEIVERS);
                receivers.each {mail -> mailSender.sendMail(mail, "PISOS", mailContent.toString())}
            }
            Thread.sleep(MILLIS)
        }


    }
}
