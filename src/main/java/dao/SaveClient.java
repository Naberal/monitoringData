package dao;

import model.Client;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.UUID;

public class SaveClient {
    private SessionFactory factory;

    public SaveClient() {
        this.factory = new Configuration().configure().buildSessionFactory();
    }

    public void save(List<Client> list) {
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            for (Client client : list) {
                session.save(client);
            }
            transaction.commit();
        }
    }

    public Client findByID(UUID uuid) {
        Client client = null;
        try (Session session = factory.openSession()) {
            client = session.get(Client.class, uuid);
        }
        return client;
    }
}
