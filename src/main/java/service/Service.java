package service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import model.Order;
import model.UUidModel;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.poi.util.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Service {
    private HttpClient client;

    public Service(HttpClient client) {
        this.client = client;
    }

    public List<Order> getNewOrders(String url) {
        List<Order> orders = new ArrayList<>();
        List<UUidModel> collect = new ArrayList();
        HttpGet getIds = new HttpGet(url);
        try {
            HttpResponse response = client.execute(getIds);
            String line = new Scanner(response.getEntity().getContent()).findInLine("\"rows\".*");
            String jsonLine = "{".concat(line);
            HashMap<Object, JSONArray> hashMap = JSON.parseObject(jsonLine, HashMap.class);
            hashMap.values().stream()
                    .map(e -> e.toJavaList(UUidModel.class))
                    .forEach(collect::addAll);
            collect.stream()
                    .map(e -> getOrder(e, url))
                    .forEach(orders::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return orders;
    }

    private Order getOrder(UUidModel model, String url) {
        Order order = new Order();
        HttpGet get = new HttpGet(url + "/" + model.getId().toString());
        try {
            HttpResponse response = client.execute(get);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            IOUtils.copy(response.getEntity().getContent(), stream);
            String dateLine = new Scanner(new ByteArrayInputStream(stream.toByteArray()))
                    .findInLine("\"moment\":\"(20\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]).*\"")
                    .substring(10, 20);
            LocalDate date = LocalDate.parse(dateLine);
            if (date.equals(LocalDate.now().minusDays(1))) {
                order = JSON.parseObject(stream.toByteArray(), Order.class);
                Matcher matcher = Pattern.compile("agent.*").matcher(stream.toString());
                if (matcher.find()) {
                    String agent = matcher.group();
                    Matcher matcher1 = Pattern.compile
                            ("\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b").matcher(agent);
                    if (matcher1.find()) {
                        order.setClientId(matcher1.group());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        get.reset();
        return order;
    }
}
