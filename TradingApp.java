package assignment;

import java.util.List;

public class TradingApp {
    private List<User> users;
    private TradingEngine tradingEngine;

    //assigning user 
    public TradingApp(List<User> users, TradingEngine tradingEngine) {
        this.users = users;
        this.tradingEngine = tradingEngine;
    }

    //allow user to login
    public User login(String email, String password) {
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    //user placeing order
    public void placeOrder(User user, Order order) {
        tradingEngine.executeOrder(order, user.getPortfolio());
    }
}
