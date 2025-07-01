package com.example.demo.Service;

import com.example.demo.dtos.stepValuesDTO;
import com.example.demo.entity.FormXML;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FormXMLService {
   public void processXmlUpload(MultipartFile file) throws Exception;
   void createTable (stepValuesDTO sVD) ;
   public String getXmlByGuidedProcessName(String gpName);
   List<FormXML> getAllXml();
}
