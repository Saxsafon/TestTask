import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

public class DataBase {
    HashMap today;
    HashMap yesterday;


    public DataBase() {
        today = createToday();
        yesterday = createYesterday();
    }

    public void sendReport() throws IOException {
        Properties properties = new Properties();
        FileInputStream in = new FileInputStream("ConfigProp");
        properties.load(in);

        Set deleted = functional.getDeleted(today,yesterday);
        Set added = functional.getAdded(today,yesterday);
        Set updated = functional.getUpdated(today,yesterday);

        String message = functional.formatReportMessage(deleted,added,updated,
                properties.getProperty("fileName"));

        functional.sendMessage(message);
    }


    public static HashMap createToday() {
        HashMap today = new HashMap<String, String>();
        today.put("www/http/base1","<head><body>...</body></head>");
        today.put("www/http/base2","<head><body>...</body></head>");
        today.put("www/http/base3","<head><body>...</body></head>");

        today.put("www/http/updated1","<head><body>.AAA.</body></head>");
        today.put("www/http/updated2","<head><body>.AAA.</body></head>");

        today.put("www/http/new1","<head><body>...</body></head>");

        return today;
    }
    public static HashMap createYesterday() {
        HashMap yesterday = new HashMap<String, String>();
        yesterday.put("www/http/base1","<head><body>...</body></head>");
        yesterday.put("www/http/base2","<head><body>...</body></head>");
        yesterday.put("www/http/base3","<head><body>...</body></head>");

        yesterday.put("www/http/updated1","<head><body>.BBB.</body></head>");
        yesterday.put("www/http/updated2","<head><body>.BBB.</body></head>");

        yesterday.put("www/http/deleted1","<head><body>...</body></head>");

        return yesterday;
    }
}
