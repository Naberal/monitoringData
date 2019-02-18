package model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "Khmelyar_Orders")
public class Order extends UUidModel {
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "moment")
    private LocalDateTime moment;
    @Column(name = "sum")
    private BigDecimal sum;
    @ManyToOne(targetEntity = Client.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;
    @Transient
    private UUID clientId;
}
