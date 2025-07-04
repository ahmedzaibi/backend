package com.example.demo.Repository;

import com.example.demo.entity.GuidedProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuidedProcessRepository extends JpaRepository<GuidedProcess, Long> {
    Optional<GuidedProcess> findByName(String name);

}



