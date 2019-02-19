package controller;

import dao.ClientDAO;
import dao.OrderDAO;
import model.Client;
import model.Order;
import service.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class TimerController extends TimerTask {
    private Timer timer;
    private Properties properties = new Properties();

    public void getNewOrders() {
        try {
            properties.load(new FileInputStream(new File("src/main/resources/application.properties")
                    .getAbsoluteFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        timer = new Timer();
        timer.schedule(this, (long) (1.16 * Math.pow(10, 8)));
    }

    @Override
    public void run() {
        Service service = new Service(properties);
        List<Order> orders = service.getNewOrders();
        for (Order order : orders) {
            Client byID = new ClientDAO().getById(order.getClientId());
            order.setClient(byID);
        }
        new OrderDAO().save(orders);
    }
}
