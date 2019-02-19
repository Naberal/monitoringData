package controller;

import dao.ClientDAO;
import dao.ExcelData;
import dao.OrderDAO;
import model.Client;
import model.Order;

import java.io.File;
import java.util.List;

public class ExcelController {
    private File file;

    public ExcelController(File file) {
        this.file = file;
    }

    public void saveExcelData() {
        ExcelData excelData = new ExcelData(file);
        ClientDAO clientDAO = new ClientDAO();
        List<Client> clients = excelData.getClients();
        clientDAO.save(clients);
        List<Order> orders = excelData.getOrders();
        for (Order order : orders) {
            Client byID = clientDAO.getById(order.getClientId());
            order.setClient(byID);
        }
        new OrderDAO().save(orders);
    }
}
