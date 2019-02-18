package dao;

import model.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.UUID;

public class SaveOrders {
    private SessionFactory factory;

    public SaveOrders() {
        this.factory = new Configuration().configure().buildSessionFactory();
    }

    public void save(List<Order> list) {
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            for (Order order : list) {
                session.save(order);
            }
            transaction.commit();
        }
    }
    public Order findByID(UUID uuid) {
        Order order=null;
        try (Session session = factory.openSession()) {
            order = session.get(Order.class, uuid);
        }
        return order;
    }

}
