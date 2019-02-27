package controller;

import dao.ClientDAO;
import dao.OrderDAO;
import model.Client;
import model.Order;
import service.Service;

import java.time.Instant;
import java.util.*;

public class TimerController extends TimerTask {
    private Connection connection = new Connection();

    public void getNewOrders() {
        new Timer().schedule(this, Date.from(Instant.now()), (long) (1.16 * Math.pow(10, 8)));
    }

    @Override
    public void run() {
        Service service = new Service(connection.httpConnection());
        List<Order> orders = service.getNewOrders(connection.getUrl());
        for (Order order : orders) {
            Client byID = new ClientDAO().getById(order.getClientId());
            order.setClient(byID);
        }
        new OrderDAO().save(orders);
    }
}
