package com.softwarebayya.AJAY.repo;

import com.softwarebayya.AJAY.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository< Movie , Long > {

}
