package com.example.demo.Controller;

import com.example.demo.Repository.FormXMLRepository;
import com.example.demo.Repository.GuidedProcessRepository;
import com.example.demo.Repository.StepRepository;
import com.example.demo.Service.GuidedProcessService; // Import the new service
import com.example.demo.entity.FormXML;
import com.example.demo.entity.GuidedProcess;
import com.example.demo.entity.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/gp")
@CrossOrigin
public class GuidedProcessController {

    @Autowired
    private GuidedProcessRepository gpRepo;

    // Inject the new service
    @Autowired
    private GuidedProcessService guidedProcessService;

    // These repositories are no longer needed for direct use in the controller
    // but can remain if other methods use them.
    @Autowired private StepRepository stepRepo;
    @Autowired private FormXMLRepository formRepo;

    @GetMapping("/getgps")
    public List<GuidedProcess> getAll() {
        return gpRepo.findAll();
    }

    @PostMapping
    public GuidedProcess create(@RequestBody Map<String, String> payload) {
        GuidedProcess gp = new GuidedProcess();
        gp.setName(payload.get("name"));
        return gpRepo.save(gp);
    }

    // New DELETE endpoint
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteGuidedProcess(@PathVariable Long id) {
        try {
            guidedProcessService.deleteGuidedProcessAndAssociations(id);
            return ResponseEntity.noContent().build(); // Standard successful response for DELETE
        } catch (RuntimeException e) {
            // If the GP is not found, return a 404
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<GuidedProcess> getById(@PathVariable Long id) {
        return gpRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/addStep")
    public ResponseEntity<?> addStep(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        Long xmlFormId = Long.parseLong(payload.get("xmlFormId"));
        Optional<GuidedProcess> optionalGp = gpRepo.findById(id);
        Optional<FormXML> optionalForm = formRepo.findById(xmlFormId);

        if (optionalGp.isEmpty() || optionalForm.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid GP or Form ID");
        }

        GuidedProcess gp = optionalGp.get();
        FormXML form = optionalForm.get();

        Step step = new Step();
        step.setGuidedProcess(gp);
        step.setForm(form);
        step.setOrderIndex(gp.getSteps().size() + 1);

        stepRepo.save(step);
        return ResponseEntity.ok().build();
    }
}