package dk.si.emailsender.email;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;


public class EmailSender {

    private static String USER_NAME = "Datamatikers";  // GMail user name (just the part before "@gmail.com")
    private static String PASSWORD = "123Security"; // GMail password

    private static void sendFromGMail(String[] to, String subject, String body) {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", USER_NAME);
        props.put("mail.smtp.password", PASSWORD);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(USER_NAME));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for (int i = 0; i < to.length; i++) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for (int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, USER_NAME, PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (AddressException ae) {
            ae.printStackTrace();
        } catch (MessagingException me) {
            me.printStackTrace();
        }
    }

    //html=TRUE, inline=FALSE
    //<img src="https://blog.mailtrap.io/wp-content/uploads/2018/11/blog-illustration-email-embedding-images.png?w=640" alt="img" />

    public void sendEmail(String body) {
        String subject = "Håber det virker";
        String[] recipients = {"Rasmusljacobsen@live.com"};


        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(body);
        String prettyJsonString = gson.toJson(je);

        sendFromGMail(recipients, subject, prettyJsonString);


    }


}

