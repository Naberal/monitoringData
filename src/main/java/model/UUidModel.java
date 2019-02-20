package model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.UUID;

@Data
@MappedSuperclass
public class UUidModel implements Serializable {
    @Id
    @Column(name = "id",columnDefinition = "BINARY(16)",unique = true)
    private UUID id;

    public void setId(String id) {
        this.id = UUID.fromString(id);
    }
}
