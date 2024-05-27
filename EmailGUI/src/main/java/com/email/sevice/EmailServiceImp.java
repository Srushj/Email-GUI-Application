package com.email.sevice;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.email.entity.EmailGUI;
import com.email.repository.EmailRepository;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.util.Properties;

@Service
public class EmailServiceImp {
	
	 @Autowired
	    private EmailRepository emailRepository;

    private Tika tika;  //Used to detect the type of content to add the file.
    public EmailServiceImp() {
        this.tika = new Tika();
    }

    public boolean sendEmail(String recipient, String subject, String message, MultipartFile file) throws MessagingException {

        boolean f = false;
        String from = "srushtijadhav5577@gmail.com";//final
        String host = "smtp.gmail.com";

        //The Gmail SMTP server settings are configured using the Properties object.
        Properties properties = System.getProperties();

        System.out.println("Properties"+properties);

        properties.put("mail.smtp.host",host);
        properties.put("mail.smtp.port","465");
        properties.put("mail.smtp.ssl.enable","true");
        properties.put("mail.smtp.auth","true");

        // A 'Session' instance is created using the SMTP properties and an Authenticator that provides Gmail credentials.
        Session session = Session.getInstance(properties,
                new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("srushtijadhav5577@gmail.com",
                        "bheyxgrebqlxeozq");
            }
        });
        session.setDebug(true);

        //a 'MimeMessage' object is created to build the email.
        MimeMessage m = new MimeMessage(session);
        try{
            m.setFrom(from);
            m.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            m.setSubject(subject);

            //Two MimeBodyPart instances are created:
            //
            //part1: Contains the text message content.
            //part2: Contains the attachment.

            MimeBodyPart part1 = new MimeBodyPart();
            part1.setText(message);

            MimeBodyPart part2 = new MimeBodyPart();
            DataSource source = new ByteArrayDataSource(file.getBytes(), tika.detect(file.getOriginalFilename())); // Use Tika to detect content type
            part2.setDataHandler(new DataHandler(source));
            part2.setFileName(file.getOriginalFilename());

            //The 'MimeMultipart' combines the text part and attachment part.
            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart(part1);
            mimeMultipart.addBodyPart(part2);

            //The content of the email is set to the MimeMultipart.
            m.setContent(mimeMultipart);

            //The email is sent using the Transport.send(m) method.
            Transport.send(m);
            System.out.println("Sent successfully....");
            f = true;
            
            EmailGUI email = new EmailGUI();
            email.setRecipient(recipient);
            email.setSubject(subject);
            email.setMessage(message);
            email.setFile(file.getOriginalFilename());
            emailRepository.save(email);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }
}