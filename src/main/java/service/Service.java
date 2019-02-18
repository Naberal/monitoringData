package service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import model.Order;
import model.UUidModel;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.poi.util.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Service extends TimerTask {
    private String url = "https://online.moysklad.ru/api/remap/1.1/entity/customerorder/";
    private HttpClient client;

    public Service() {
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("admin@max69", "61ae20975e");
        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(AuthScope.ANY, credentials);
        client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
    }

    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();
        List<UUidModel> collect = new ArrayList();
        HttpGet getId = new HttpGet(url);
        try {
            HttpResponse response = client.execute(getId);
            String line = new Scanner(response.getEntity().getContent()).findInLine("\"rows\".*");
            String s = "{".concat(line);
            HashMap<Object, JSONArray> hashMap = JSON.parseObject(s, HashMap.class);
            hashMap.values().stream()
                    .map(e -> e.toJavaList(UUidModel.class))
                    .forEach(collect::addAll);
            orders = collect.stream()
                    .map(this::getData)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return orders;
    }

    private Order getData(UUidModel model) {
        Order order = null;
        HttpGet get = new HttpGet(url + "/" + model.getId().toString());
        try {
            HttpResponse response = client.execute(get);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            IOUtils.copy(response.getEntity().getContent(), stream);
            String dateLine = new Scanner(new ByteArrayInputStream(stream.toByteArray()))
                    .findInLine("\"moment\":\"(20\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]).*\"")
                    .substring(10, 20);
            LocalDate date = LocalDate.parse(dateLine);
            if (date.equals(LocalDate.of(2019, 2, 4))) {//todo LocalDate.now().minusDays(1)
                order = JSON.parseObject(stream.toByteArray(), Order.class);
                Pattern pattern = Pattern.compile("agent.*");
                Matcher matcher = pattern.matcher(stream.toString());
                if (matcher.find()) {
                    String agent = matcher.group();
                    Pattern pattern1 = Pattern.compile("\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b");
                    Matcher matcher1 = pattern1.matcher(agent);
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

    @Override
    public void run() {
        getAll();
    }
}
