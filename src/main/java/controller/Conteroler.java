package controller;

import dao.DataFromExel;
import dao.SaveClient;
import dao.SaveOrders;
import model.Client;
import model.Order;

import java.io.File;
import java.util.List;

public class Conteroler {
    private File file;

    public Conteroler(File file) {
        this.file = file;
    }

    public void savaFromExel() {
        DataFromExel dataFromExel = new DataFromExel(file);
        SaveClient saveClient = new SaveClient();
        List<Client> clients = dataFromExel.getClients();
        saveClient.save(clients);
        List<Order> orders = dataFromExel.getOrders();
        for (Order order : orders) {
            Client byID = saveClient.findByID(order.getClientId());
            order.setClient(byID);
        }
        new SaveOrders().save(orders);
    }
}
