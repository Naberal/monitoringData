package service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import dao.SaveOrders;
import model.Order;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Service extends TimerTask {


//    private Properties properties = new Properties();

    private String url = "https://online.moysklad.ru/api/remap/1.1/entity/customerorder/";
    private HttpClient client;

    public Service() {
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("admin@max69", "61ae20975e");
        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(AuthScope.ANY, credentials);
        client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
    }

    public List<Order> getAll() {
        List<Order> collect = new ArrayList();
        HttpGet getAll = new HttpGet(url);
        try {
            HttpResponse response = client.execute(getAll);
            String line = new Scanner(response.getEntity().getContent()).findInLine("\"rows\".*");
            String s = "{".concat(line);
            HashMap<Object, JSONArray> hashMap = JSON.parseObject(s, HashMap.class);
            hashMap.values().stream()
                    .map(e -> e.toJavaList(Order.class))
                    .forEach(collect::addAll);
            List<Order> collect1 = collect.stream()
                    .filter(e -> e.getMoment().equals(LocalDate.parse("2019-02-04")))
                    .filter(e -> new SaveOrders().findByID(e.getId()).equals(null))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return collect;
    }

//    public List<Order> () {
//        List<UUidModel> models=getUUid();
//        List<Order> orders = new ArrayList<>();
//        for (UUidModel model : models) {
//            HttpGet get = new HttpGet(url + "/" + model.getId().toString());
//            try {
//                HttpResponse response = client.execute(get);
//                String line = new Scanner(response.getEntity().getContent())
//                        .findInLine("\"moment\":\"(20\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]).*\"")
//                        .substring(10,20);
//                LocalDate date = LocalDate.parse(line);
//                System.out.println(date);
////                HashMap<Object, JSONArray> hashMap = JSON.parseObject(s, HashMap.class);
////                hashMap.values().stream()
////                        .map(e -> e.toJavaList(UUidModel.class))
////                        .forEach(collect::addAll);
//            } catch (ClientProtocolException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//        return orders;
//    }

    @Override
    public void run() {
        getAll();
    }
}
