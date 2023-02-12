package com.example.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.cglib.core.Local;

import java.io.IOException;
import java.time.*;
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
            case MONDAY : return "Понеділок";
            case TUESDAY : return "Вівторок";
            case WEDNESDAY: return "Середа";
            case THURSDAY: return "Четвер";
            case FRIDAY : return "П'ятниця";
            case SATURDAY : return "Субота";
            case SUNDAY : return "Неділя";
            default : return null;
        }
    }
    public static String resp(LocalDateTime time) {
        String html = "https://www.toe.com.ua/index.php/component/content/article?id=1803";
        try {
            Document doc = Jsoup.connect(html).get();
            Element tableElements = doc.selectFirst("table");
            Elements tableHeaderEles = tableElements.select("tr");
            var tr = getTrIndex(tableHeaderEles, time);
            var td = getTdIndex(tableHeaderEles.first(), time);
            var style = tableHeaderEles.get(tr).children().get(td).attributes().get("style");
            return getColor(style) + " ЗОНА";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

    public static String todaysGraph(LocalDateTime time) {
        String html = "https://www.toe.com.ua/index.php/component/content/article?id=1803";
        try {
            var sb = new StringBuilder();
            sb.append("\n");
            Document doc = Jsoup.connect(html).get();
            Element tableElements = doc.selectFirst("table");
            Elements tableHeaderEles = tableElements.select("tr");
            var td = getTdIndex(tableHeaderEles.first(), time);
            for (int i = 3; i < tableHeaderEles.size(); i++) {
                var tr = tableHeaderEles.get(i);
                var style = tableHeaderEles.get(i).children().get(td).attributes().get("style");
                sb.append(tr.text());
                sb.append(" - ");
                sb.append(getColorEmoji(style));
                sb.append("\n");

            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

    public static String getColorEmoji(String style) {
        if (style.contains("#00ff00")) {
            return "🟩";
        } else if (style.contains("#ff3300")) {
            return "🟥";
        } else {
            return "🟧";
        }
    }

    public static void main(String[] args) {
        var a = '\uD83D';

        return;
    }
    public static String getColor(String style) {
        if (style.contains("#00ff00")) {
            return "ЗЕЛЕНА";
        } else if (style.contains("#ff3300")) {
            return "ЧЕРВОНА";
        } else {
            return "ОРАНЖЕВА";
        }
    }

    public static List<Node> getColumnOfToday(List<Node> list) {
        var newList = new ArrayList<Element>();
        ((Element) list.get(0)).children().get(1).text();
        var dayCell = list.get(0);
        return null;
    }

    public static int getTrIndex(Elements elements, LocalDateTime time) {
        var now = LocalTime.of(time.getHour(), time.getMinute(), time.getSecond());
        for (int i = 0, j = 3 ; i < 21; i+= 3, j++) {
            if (now.isAfter(LocalTime.of(i, 0)) && now.isBefore(LocalTime.of(i + 3, 0))) {
                return j;
            }
        }
        return 10;
    }

    public static int getTdIndex(Element element, LocalDateTime time) {
        var day = dayOfWeek(time.getDayOfWeek());
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

    public static String getUntil(LocalDateTime time) {
        var now = LocalTime.of(time.getHour(), time.getMinute(), time.getSecond());
        for (int i = 0 ; i < 21; i+= 3) {
            if (now.isAfter(LocalTime.of(i, 0)) && now.isBefore(LocalTime.of(i + 3, 0))) {
                var a = i + 3;
                return " ДО " + a + ":00";
            }
        }
        return " ДО " + "00" + ":00";
    }

//    public static int indexOfToday(Elements elements, LocalDateTime time) {
//        elements.filter(e -> e.outerHtml().contains())
//    }
}
