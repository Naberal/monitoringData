package controller;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Connection {
    private HttpClient client;
    private Properties properties = new Properties();

    public Connection() {
        try {
            properties.load(new FileInputStream(new File("src/main/resources/application.properties")
                    .getAbsoluteFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HttpClient httpConnection() {
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(properties.getProperty("userName")
                , properties.getProperty("password"));
        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(AuthScope.ANY, credentials);
        client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
        return client;
    }

    public String getUrl() {
        return properties.getProperty("url");
    }
}
