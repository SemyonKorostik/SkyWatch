package dev.korostik.skywatch.entity;

import jakarta.persistence.Column;

public class Address {
    @Column(name = "country", nullable = false)
    private String country;
    @Column(name = "place_name", nullable = false)
    private String placeName;
}
