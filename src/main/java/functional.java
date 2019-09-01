import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.*;

public class functional {

    public static void sendMessage (String text) throws IOException {
        Properties properties = new Properties();
        FileInputStream in = new FileInputStream("ConfigProp");
        properties.load(in);

        Properties mailproperties = new Properties();
        FileInputStream inn = new FileInputStream("mail.properties");
        mailproperties.load(inn);

        Session session = Session.getInstance(mailproperties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(properties.getProperty("username"), properties.getProperty("password"));
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(properties.getProperty("from")));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(properties.getProperty("to")));

            message.setSubject("Report");
            message.setText(text);

            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public static String formatReportMessage(Set deleted,Set added, Set updated,String direction) throws IOException {


        String listOfdeleted = String.join(", ", deleted);
        String listOfadded = String.join(", ", added);
        String listOfupdated =  String.join(", ", updated);

        String fileName = "src" + direction;
        String message = MessageFormat.format(readFile(fileName),
                listOfdeleted,listOfadded,listOfupdated);
        return message;
    }

    public static String readFile( String fileName) throws IOException {
        String text = new String();

        Path path = Paths.get(fileName);
        Scanner scanner = new Scanner(path);

        while(scanner.hasNextLine()){
            text += scanner.nextLine();
            text += "\n";
        }
        scanner.close();

        return text;
    }

    public static Set<String> getDeleted(HashMap today, HashMap yesterday) {
        Set deleted = new HashSet();
        deleted.addAll(yesterday.keySet());
        deleted.removeAll(today.keySet());

        return deleted;
    }

    public static Set<String> getAdded(HashMap today, HashMap yesterday) {
        Set added = new HashSet();
        added.addAll(today.keySet());
        added.removeAll(yesterday.keySet());

        return added;
    }

    public static Set<String> getUpdated(HashMap today, HashMap yesterday) {
        Set updated = new HashSet();

        Set cleaned_today = today.keySet();
        cleaned_today.removeAll(getAdded(today,yesterday));

        for (Object i : cleaned_today) {
            if (today.get(i) != yesterday.get(i)) {
                updated.add(i);
            }
        }

        return updated;
    }

}
