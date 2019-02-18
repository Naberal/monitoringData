package model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "Khmelyar_Clients")
public class Client extends UUidModel {
    @Column(name = "name")
    private String name;
}
