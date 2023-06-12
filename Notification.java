//package assignment;
//
//import javax.mail.*;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import java.util.Properties;
//
//public class Notification {
//    private User user;
//    private boolean positionNotificationEnabled;
//    private boolean pnlNotificationEnabled;
//    private double pnlThreshold;
//
//    public Notification(User user) {
//        this.user = user;
//        this.positionNotificationEnabled = true;
//        this.pnlNotificationEnabled = true;
//        this.pnlThreshold = 0.0;
//    }
//
//    public void setPositionNotificationEnabled(boolean positionNotificationEnabled) {
//        this.positionNotificationEnabled = positionNotificationEnabled;
//    }
//
//    public void setPnlNotificationEnabled(boolean pnlNotificationEnabled) {
//        this.pnlNotificationEnabled = pnlNotificationEnabled;
//    }
//
//    public void setPnlThreshold(double pnlThreshold) {
//        this.pnlThreshold = pnlThreshold;
//    }
//
//    public void sendPositionNotification(String position) {
//        if (positionNotificationEnabled) {
//            String message = "You have entered a position: " + position;
//            sendNotification("Position Notification", message);
//        }
//    }
//
//    public void sendPnlNotification(double pnl) {
//        if (pnlNotificationEnabled && (pnl >= pnlThreshold || pnl <= -pnlThreshold)) {
//            String message = "Your P&L has crossed the threshold: " + pnl;
//            sendNotification("P&L Notification", message);
//        }
//    }
//
//    private void sendNotification(String subject, String message) {
//        // JavaMail properties
//        Properties properties = new Properties();
//        properties.put("mail.smtp.auth", "true");
//        properties.put("mail.smtp.starttls.enable", "true");
//        properties.put("mail.smtp.host", "smtp.example.com"); // Update with your SMTP server host
//        properties.put("mail.smtp.port", "587"); // Update with your SMTP server port
//
//        // SMTP server credentials
//        final String username = "your-email@example.com"; // Update with your email address
//        final String password = "your-email-password"; // Update with your email password
//
//        // Create a session with authentication
//        Session session = Session.getInstance(properties, new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(username, password);
//            }
//        });
//
//        try {
//            // Create a new message
//            Message emailMessage = new MimeMessage(session);
//
//            // Set the sender address
//            emailMessage.setFrom(new InternetAddress(username));
//
//            // Set the recipient address
//            emailMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
//
//            // Set the subject and content
//            emailMessage.setSubject(subject);
//            emailMessage.setText(message);
//
//            // Send the email
//            Transport.send(emailMessage);
//        } catch (MessagingException e) {
//            System.err.println("Failed to send email: " + e.getMessage());
//        }
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
///*import javax.mail.*;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import java.util.Properties;
//
//public class Notification {
//    private User user;
//
//    public Notification(User user) {
//        this.user = user;
//    }
//
//    public void sendNotification(String subject, String message) {
//        String to = user.getEmail();
//
//        // Configure the email properties
//        Properties properties = new Properties();
//        properties.put("mail.smtp.host", "your_smtp_host");
//        properties.put("mail.smtp.port", "your_smtp_port");
//        properties.put("mail.smtp.auth", "true");
//        properties.put("mail.smtp.starttls.enable", "true");
//
//        // Set up the authentication for the email account
//        Authenticator authenticator = new Authenticator() {
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication("your_email", "your_password");
//            }
//        };
//
//        // Create the email session
//        Session session = Session.getInstance(properties, authenticator);
//
//        try {
//            // Create a new email message
//            Message emailMessage = new MimeMessage(session);
//            emailMessage.setFrom(new InternetAddress("your_email"));
//            emailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
//            emailMessage.setSubject(subject);
//            emailMessage.setText(message);
//
//            // Send the email
//            Transport.send(emailMessage);
//            System.out.println("Notification sent successfully to: " + to);
//        } catch (MessagingException e) {
//            System.out.println("Failed to send notification to: " + to);
//            e.printStackTrace();
//        }
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
///*import java.util.ArrayList;
//import java.util.List;
//
//
//// Notification class
//public class Notification {
//    private String recipient;
//    private String content;
//    // Additional fields as needed
//
//    public Notification(String recipient, String content) {
//        this.recipient = recipient;
//        this.content = content;
//    }
//
//    // Getters and setters
//}
//
//// Notification service
//class NotificationService {
//    private List<Notification> notifications;
//
//    public NotificationService() {
//        this.notifications = new ArrayList<>();
//    }
//
//    public void sendNotification(String recipient, String content) {
//        Notification notification = new Notification(recipient, content);
//        notifications.add(notification);
//
//        // Send notification using JavaMail or any other method
//        // Code for sending email notification goes here
//        System.out.println("Notification sent to: " + recipient + "\nContent: " + content);
//    }
//}
//
//
//// Trading app class
//class TradingApp {
//    private NotificationService notificationService;
//
//    public TradingApp() {
//        this.notificationService = new NotificationService();
//    }
//
//    public void enterPosition(User user, Stock stock) {
//        // Perform logic for entering a position
//        String content = "You have entered a position for stock: " + stock.getName();
//        notificationService.sendNotification(user.getEmail(), content);
//    }
//
//    public void exitPosition(User user, Stock stock) {
//        // Perform logic for exiting a position
//        String content = "You have exited a position for stock: " + stock.getName();
//        notificationService.sendNotification(user.getEmail(), content);
//    }
//
//    public void checkPnL(User user, double currentPnL, double profitThreshold, double lossThreshold) {
//        // Perform logic for checking P&L
//        if (currentPnL >= profitThreshold) {
//            String content = "Your P&L has crossed the profit threshold of " + profitThreshold;
//            notificationService.sendNotification(user.getEmail(), content);
//        }
//        if (currentPnL <= -lossThreshold) {
//            String content = "Your P&L has crossed the loss threshold of " + lossThreshold;
//            notificationService.sendNotification(user.getEmail(), content);
//        }
//    }
//}
//
//public class Main {
//    public static void main(String[] args) {
//        User user = new User("example@example.com");
//        Stock stock = new Stock("ABC", 10.5);
//
//        TradingApp tradingApp = new TradingApp();
//        tradingApp.enterPosition(user, stock);
//        tradingApp.exitPosition(user, stock);
//        tradingApp.checkPnL(user, 1500, 1000, 500);
//    }
//}
//*/