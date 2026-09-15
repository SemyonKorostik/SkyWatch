package dev.korostik.skywatch.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Location_id_gen")
    @SequenceGenerator(name = "Location_id_gen", sequenceName = "location_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Embedded
    private Address address;

    @Column(name = "time_zone_id", nullable = false)
    private String timeZoneId;

}