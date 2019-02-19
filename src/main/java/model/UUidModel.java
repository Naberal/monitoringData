package model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@Data
@MappedSuperclass
public class UUidModel {
    @Id
    @GeneratedValue()
    @Column(name = "id", unique = true)
    private UUID id;

    public void setId(String id) {
        this.id = UUID.fromString(id);
    }

}
