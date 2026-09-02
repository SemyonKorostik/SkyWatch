package dev.korostik.skywatch.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Location_id_gen")
    @SequenceGenerator(name = "Location_id_gen", sequenceName = "location_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "latitude", nullable = false, precision = 10, scale = 8)
    private Float latitude;

    @Column(name = "longitude", nullable = false, precision = 11, scale = 8)
    private Float longitude;

    @Embedded
    private Address address;

    @Column(name = "time_zone_offset", nullable = false)
    private Integer timeZoneOffset;

}