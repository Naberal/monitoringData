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

public class ExcelData {
    private Workbook workbook;

    public ExcelData(File file) {
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
        List<Client> clientsList = new ArrayList<>();
        for (List<String> value : getData("counterparty")) {
            Client client = new Client();
            try {
                client.setId(value.get(0));
                client.setName(value.get(1));
                clientsList.add(client);
                value.clear();
            } catch (Exception e) {
                value.clear();
            }
        }
        return clientsList;
    }

    public List<Order> getOrders() {
        List<Order> ordersList = new ArrayList<>();
        for (List<String> values : getData("orders")) {
            Order order = new Order();
            try {
                order.setId(values.get(0));
                order.setName(values.get(1));
                order.setDescription(values.get(2));
                order.setMoment(LocalDateTime.parse(values.get(3)));
                order.setSum(new BigDecimal(values.get(4)));
                order.setClientId(values.get(5));
                ordersList.add(order);
                values.clear();
            } catch (Exception e) {
                values.clear();
            }
        }
        return ordersList;
    }

    private List<List<String>> getData(String name) {
        List<List<String>> listObjects = new ArrayList<>();
        Sheet sheet = workbook.getSheet(name);
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            List<String> object = new ArrayList<>();
            Iterator<Cell> cellIterator = rowIterator.next().cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                object.add(cell.getStringCellValue());
            }
            listObjects.add(object);
        }
        return listObjects;
    }
}
