package com.example.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.cglib.core.Local;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static DayOfWeek dayOfWeek(String day) {
        switch (day) {
            case "понеділок" : return DayOfWeek.MONDAY;
            case "вівторок" : return DayOfWeek.TUESDAY;
            case "середа" : return DayOfWeek.WEDNESDAY;
            case "четвер" : return DayOfWeek.THURSDAY;
            case "п'ятниця" : return DayOfWeek.FRIDAY;
            case "субота" : return DayOfWeek.SATURDAY;
            case "неділя" : return DayOfWeek.SUNDAY;
            default : return null;
        }
    }

    public static String dayOfWeek(DayOfWeek day) {
        switch (day) {
            case MONDAY : return "понеділок";
            case TUESDAY : return "вівторок";
            case WEDNESDAY: return "середа";
            case THURSDAY: return "четвер";
            case FRIDAY : return "п'ятниця";
            case SATURDAY : return "субота";
            case SUNDAY : return "неділя";
            default : return null;
        }
    }
    public static String resp() {
        DayOfWeek todaysDay = LocalDate.now(ZoneId.of("Europe/Kiev")).getDayOfWeek();
        String html = "https://www.toe.com.ua/index.php/hrafik-pohodynnykh-vymknen-spozhyvachiv";
        try {
            Document doc = Jsoup.connect(html).get();
            Element tableElements = doc.selectFirst("table");
            Elements tableHeaderEles = tableElements.select("tr");
            var tr = getTrIndex(tableHeaderEles);
            var td = getTdIndex(tableHeaderEles.first());
            var style = tableHeaderEles.get(tr).children().get(td).attributes().get("style");
            if (style.contains("#00ff00")) {
                return "ЗЕЛЕНА ЗОНА " + getUntil();
            } else if (style.contains("#ff3300")) {
                return "ЧЕРВОНА ЗОНА" + getUntil();
            } else {
                return "ОРАНЖЕВА ЗОНА" + getUntil();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

    public static List<Node> getColumnOfToday(List<Node> list) {
        var newList = new ArrayList<Element>();
        ((Element) list.get(0)).children().get(1).text();
        var dayCell = list.get(0);
        return null;
    }

    public static int getTrIndex(Elements elements) {
        var now = LocalTime.now(ZoneId.of("Europe/Kiev"));
        for (int i = 0, j = 3 ; i < 21; i+= 3, j++) {
            if (now.isAfter(LocalTime.of(i, 0)) && now.isBefore(LocalTime.of(i + 3, 0))) {
                return j;
            }
        }
        return 10;
    }

    public static int getTdIndex(Element element) {
        var day = dayOfWeek(LocalDate.now(ZoneId.of("Europe/Kiev")).getDayOfWeek());
        for (int i = 0; i < element.children().size(); i++) {
            if (element.children().get(i).text().contains(day)) {
                if (i == 3) {
                    return 4;
                }
                if (i == 5) {
                    return 7;
                }
                return i;
            }
        }
        return -1;
    }

    public static String getUntil() {
        var now = LocalTime.now(ZoneId.of("Europe/Kiev"));
        for (int i = 0 ; i < 21; i+= 3) {
            if (now.isAfter(LocalTime.of(i, 0)) && now.isBefore(LocalTime.of(i + 3, 0))) {
                var a = i + 3;
                return " ДО " + a + ":00";
            }
        }
        return " ДО " + "00" + ":00";
    }
}
