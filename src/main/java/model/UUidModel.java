package model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public class UUidModel {
    @Id
    @Column(name = "id", unique = true)
    private String id;
}
