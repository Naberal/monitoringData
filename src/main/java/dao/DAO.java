package dao;

import java.util.List;
import java.util.UUID;

public interface DAO<E> {
    void save(List<E> list);

    E getById(UUID uuid);
}
