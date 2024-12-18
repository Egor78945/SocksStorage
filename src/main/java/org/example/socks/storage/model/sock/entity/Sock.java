package org.example.socks.storage.model.sock.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "socks")
@Data
public class Sock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "color")
    private String color;
    @Column(name = "cotton_percent")
    private Integer cottonPercent;
    @Column(name = "count")
    private Long count;

    public Sock(String color, Integer cottonPercent, Long count) {
        this.color = color;
        this.cottonPercent = cottonPercent;
        this.count = count;
    }

    public Sock() {
    }
}
