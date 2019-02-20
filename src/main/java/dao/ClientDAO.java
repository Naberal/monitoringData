package dao;

import model.Client;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.UUID;

public class ClientDAO implements DAO<Client> {
    private SessionFactory factory;

    public ClientDAO() {
        this.factory = new Configuration().configure().buildSessionFactory();
    }

    @Override
    public void save(List<Client> list) {
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            for (Client client : list) {
                session.save(client);
            }
            transaction.commit();
        }
    }

    @Override
    public Client getById(UUID uuid) {
        Client client;
        try (Session session = factory.openSession()) {
            client = session.get(Client.class, uuid);
        }
        return client;
    }
}
