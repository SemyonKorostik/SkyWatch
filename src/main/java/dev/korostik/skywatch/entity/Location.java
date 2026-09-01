package dev.korostik.skywatch.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "Location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Location_id_gen")
    @SequenceGenerator(name = "Location_id_gen", sequenceName = "location_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "latitude", nullable = false, precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 11, scale = 8)
    private BigDecimal longitude;

//    private Address address;

    /*добавить поля для адреса и обернуть в объект для читабельности*/

    @Column(name = "time_zone_offset", nullable = false)
    private Short timeZoneOffset;

}