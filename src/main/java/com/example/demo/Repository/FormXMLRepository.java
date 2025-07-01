package com.example.demo.Repository;


import com.example.demo.entity.FormXML;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FormXMLRepository extends JpaRepository<FormXML, Long> {
    List<FormXML> findAll();
    Optional<FormXML> findByName(String name);
}
