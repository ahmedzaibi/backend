package com.example.demo.Repository;

import com.example.demo.entity.GuidedProcess;
import com.example.demo.entity.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StepRepository extends JpaRepository<Step, Long> {
    boolean existsByObjectNameAndGuidedProcess(String objectName, GuidedProcess guidedProcess);

}
