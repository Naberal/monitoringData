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
        List<Client> clients = excelData.getClients();
        List<Order> orders = excelData.getOrders();
        for (Order order:orders){
            for (Client client:clients){
                if(client.getId().equals(order.getClientId())){
                    order.setClient(client);
                }
            }
        }
        new ClientDAO().save(clients);
        new OrderDAO().save(orders);
    }
}
