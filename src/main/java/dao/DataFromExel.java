package dao;

import model.Client;
import model.Order;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataFromExel {
    private Workbook workbook;

    public DataFromExel(File file) {
        try {
            InputStream stream = new FileInputStream(file);
            if (file.getName().toLowerCase().endsWith("xlsx")) {
                workbook = new XSSFWorkbook(stream);
            } else if (file.getName().toLowerCase().endsWith("xls")) {
                workbook = new HSSFWorkbook(stream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Client> getClients() {
        List<Client> clients = new ArrayList<>();
        List<String> value = new ArrayList<>();
        Sheet counterparty = workbook.getSheet("counterparty");
        Iterator<Row> rowIterator = counterparty.rowIterator();
        while (rowIterator.hasNext()) {
            Iterator<Cell> cellIterator = rowIterator.next().cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                value.add(cell.getStringCellValue());
            }
            Client client = new Client();
            try {
                client.setId(value.get(0));
                client.setName(value.get(1));
                clients.add(client);
                value.clear();
            } catch (Exception e) {
                value.clear();
                continue;
            }
        }
        return clients;
    }

    public List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();
        List<String> value = new ArrayList<>();
        Sheet order = workbook.getSheet("order");
        Iterator<Row> rowIterator = order.rowIterator();
        while (rowIterator.hasNext()) {
            Iterator<Cell> cellIterator = rowIterator.next().cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                value.add(cell.getStringCellValue());
            }
            Order order1 = new Order();
            try {
                order1.setId(value.get(0));
                order1.setName(value.get(1));
                order1.setDescription(value.get(2));
                order1.setMoment(LocalDateTime.parse(value.get(3)));
                order1.setSum(new BigDecimal(value.get(4)));
                order1.setClientId(value.get(5));
                orders.add(order1);
                value.clear();
            } catch (Exception e) {
                value.clear();
                continue;
            }
        }
        return orders;
    }


}
