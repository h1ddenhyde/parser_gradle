import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.util.Objects;

import static java.lang.Thread.sleep;

public class Parser {
    static long seconds = 1000;
    static long minute = seconds * 60;
    static long hour = minute * 60;


    public static void main(String[] args) throws IOException {
        if (args.length > 0 && "h".equals(args[0])) {
            System.out.println("Default time: 4 hours  \n" +
                    "To set a specific time, enter the number of seconds after <>.jar");
            return;
        }
        long period;
        if (args.length > 0) {
            long arg1 = Long.parseLong(args[0]); // по умолчанию - секунды
            period = seconds * arg1;
        } else {
            period = hour * 4;
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        System.out.println("The process is running; period is " + decimalFormat.format((float) period / minute) + " minutes");
        System.out.println("To stop the process press ctrl+c or close the terminal (Windows)" +
                "To make sure that the process is dead check this pid in the task Manager " + ManagementFactory.getRuntimeMXBean().getName());
        System.out.close();
        System.in.close();

        process(period);
    }

    static void process(long time) {
        Thread run = new Thread(() -> {
            while (true) {
                try {
                    new Parser().run();
                    sleep(time);
                } catch (InterruptedException ex) {
                    System.out.println("end");
                }
            }
        });
        run.start();
    }

    void run() {
        try {
            Document doc = Jsoup.connect("https://carsharing.gde-luchshe.ru/map").maxBodySize(0).userAgent("Google").get();

            String o = doc.outerHtml();

//            String o = "var cars = ([{\"id\":\"423148579\",\"name\":\"Lifan X50 синий\",\"latitude\":\"56.77866\",\"longitude\":\"60.5293732\",\"partner_id\":\"32\",\"group_id\":\"bc58e5c9a549c9728f4e280b51bb64648920e301\",\"content\":\"\\u003cdiv class='map_marker'\\u003eUramobil \\u003cb\\u003eLifan X50 синий\\u003c/b\\u003e (41%)\\u003c/div\\u003e\"},{\"id\":\"423148580\",\"name\":\"Nissan X-Trail\",\"latitude\":\"55.82876\",\"longitude\":\"37.592498\",\"partner_id\":\"11\",\"group_id\":\"d7ebd9bc9fbef8b15c61cb2416a621653c9c9fa6\",\"content\":\"\\u003cdiv class='map_marker'\\u003eYouDrive \\u003cb\\u003eNissan X-Trail\\u003c/b\\u003e (57%)\\u003c/div\\u003e\"},{\"id\":\"423148614\",\"name\":\"smart fortwo\",\"latitude\":\"55.794861\",\"longitude\":\"37.781348\",\"partner_id\":\"11\",\"group_id\":\"ab2e4de41d504cc43f3806516dc98038bb71c1ca\",\"content\":\"\\u003cdiv class='map_marker'\\u003eYouDrive \\u003cb\\u003esmart fortwo\\u003c/b\\u003e (60%)\\u003c/div\\u003e\"},{\"id\":\"423148620\",\"name\":\"Lifan X50 Серебро\",\"latitude\":\"55.2620125\",\"longitude\":\"61.401825\",\"partner_id\":\"32\",\"group_id\":\"e16b1c3fa66a12fb32a032cd12cc770f145273ca\",\"content\":\"\\u003cdiv class='map_marker'\\u003eUramobil \\u003cb\\u003eLifan X50 Серебро\\u003c/b\\u003e (99%)\\u003c/div\\u003e\"}]);";

            String s2 = new InputHandler(o).handle();
            new FileCreator(s2).createFile();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

}

