
package dashboard;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class Email {

    public static void sendEmail(String receiver, String text) {
        String sender = "eisraqrejab2409@gmail.com";
        
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("eisraqrejab2409@gmail.com", "ujhazsyzmsudakug");
            }
        });

        
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));

            message.setSubject("STOCK MARKET ALERT");
            message.setText(text);
            Transport.send(message);

            System.out.println("Sent message successfully......");

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
        
        
    }
    
        public static void main(String[] args) {
            // TODO code application logic here
            Scanner scanner = new Scanner(System.in);
             System.out.print("Enter your email: ");
            String email = scanner.nextLine();

            System.out.print("Enter your profit or loss amount: ");
            double ProfitorLoss = scanner.nextDouble();

         if (ProfitorLoss >= 1000) {
        Email.sendEmail(email, "RM1000 profit threshold crossed");
    } else if (ProfitorLoss <= -500) {
        Email.sendEmail(email, "RM500 loss threshold crossed");
    }
 
}
        
}