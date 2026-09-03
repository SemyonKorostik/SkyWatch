package dev.korostik.skywatch.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Address {
    @Column(name = "country", nullable = false)
    private String country;
    @Column(name = "place_name", nullable = false)
    private String placeName;
}
