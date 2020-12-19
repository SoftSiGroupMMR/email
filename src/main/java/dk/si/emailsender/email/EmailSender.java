package dk.si.emailsender.email;


import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;


public class EmailSender {

    private final Gson gson = new Gson();
    private static Logger logger = LoggerFactory.getLogger(EmailSender.class.getName());


    private static String USER_NAME = "Datamatikers";  // GMail user name (just the part before "@gmail.com")
    private static String PASSWORD = "123Security"; // GMail password

    private static void sendFromGMail(String[] to, String subject, String body) throws MessagingException {
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
        message.setText(body, "utf-8", "html");
        Transport transport = session.getTransport("smtp");
        transport.connect(host, USER_NAME, PASSWORD);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();

    }

    public void sendEmail(String message) throws MessagingException {
        logger.info("Recieved message.");

        JsonObject jsonMsg = gson.fromJson(message, JsonObject.class);
        String[] recipients = {jsonMsg.get("recipient").getAsString()};

        String subject = jsonMsg.get("subject").getAsString();
        String body = jsonMsg.get("body").getAsString();

        sendFromGMail(recipients, subject, body);
        logger.info("Sent to mail");
    }
/*
    public static void main(String[] args) {
        EmailSender email = new EmailSender();
        String test = " <h1>Travel Data for Dora</h1><p>Info about Berlin:</h2><p>Berlin is in Germany (DE).</p><p>The currency of Germany is EUR, Euro.</p><p>The flag ofGermany.</p><img src=\"http://www.oorsprong.org/WebSamples.CountryInfo/Flags/Germany.jpg\" alt=\"img\" >";
        email.sendEmail(test);

    }
    */

}

