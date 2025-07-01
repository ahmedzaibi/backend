package com.example.demo.Controller;

import com.example.demo.Service.FormXMLService;
import com.example.demo.dtos.stepValuesDTO;
import com.example.demo.entity.FormXML;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/forms")
@RequiredArgsConstructor
public class FormXMLController {
    private final FormXMLService formXMLService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadXml(@RequestParam("file") MultipartFile file) {
        try {
            formXMLService.processXmlUpload(file);
            return ResponseEntity.ok("Upload successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }



    @GetMapping("/{gpName}")
    public ResponseEntity<String> getXmlByGuidedProcessName(@PathVariable String gpName) {
        String xmlContent = formXMLService.getXmlByGuidedProcessName(gpName);
        return ResponseEntity.ok(xmlContent);
    }

    @GetMapping("/getxmls")
    public ResponseEntity<List<FormXML>> getxmls() {
        List<FormXML> xmlContent = formXMLService.getAllXml();
        return ResponseEntity.ok(xmlContent);
    }
    private final FormXMLService formService ;


}
