package com.softwarebayya.AJAY.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Movie {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String director;

    @ElementCollection
    private List<String> actors=new ArrayList<>();
}
