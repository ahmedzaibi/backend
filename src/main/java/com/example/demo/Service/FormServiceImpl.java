package com.example.demo.Service;

import com.example.demo.Repository.FormXMLRepository;
import com.example.demo.Repository.GuidedProcessRepository;
import com.example.demo.Repository.StepRepository;
import com.example.demo.dtos.stepValuesDTO;
import com.example.demo.entity.Champ;
import com.example.demo.entity.FormXML;
import com.example.demo.entity.GuidedProcess;
import com.example.demo.entity.Step;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FormServiceImpl implements FormXMLService {
@Autowired
private GuidedProcessRepository guidedProcessRepository ;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private StepRepository stepRepository;
    private final FormXMLRepository formXMLRepository;



    public void processXmlUpload(MultipartFile file) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file.getInputStream());
        document.getDocumentElement().normalize();

        // 1. Extract objectName from <OBJECT Name="...">
        NodeList objectList = document.getElementsByTagName("OBJECT");
        String objectName = null;
        if (objectList.getLength() > 0) {
            Element objectElement = (Element) objectList.item(0);
            objectName = objectElement.getAttribute("Name");
        }
        if (objectName == null || objectName.isEmpty()) {
            throw new RuntimeException("OBJECT Name attribute is missing.");
        }

        // 2. Extract BP value from <BP v="..." />
        NodeList gproperties = document.getElementsByTagName("GPROPERTIES");
        String bpValue;
        if (gproperties.getLength() > 0) {
            Element gprop = (Element) gproperties.item(0);
            NodeList bpList = gprop.getElementsByTagName("BP");
            if (bpList.getLength() > 0) {
                Element bpElement = (Element) bpList.item(0);
                bpValue = bpElement.getAttribute("v");
            } else {
                bpValue = null;
            }
        } else {
            bpValue = null;
        }
        if (bpValue == null || bpValue.isEmpty()) {
            throw new RuntimeException("BP value is missing in GPROPERTIES.");
        }

        // 3. Save or update FormXML by objectName (used as unique name now)
        FormXML form = formXMLRepository.findByName(objectName)
                .orElse(new FormXML());

        form.setName(objectName); // use objectName as name now
        form.setXmlContent(new String(file.getBytes(), StandardCharsets.UTF_8));
        formXMLRepository.save(form);

        // 4. Handle GuidedProcess
        GuidedProcess gp = guidedProcessRepository.findByName(bpValue)
                .orElseGet(() -> {
                    GuidedProcess newGp = new GuidedProcess();
                    newGp.setName(bpValue);
                    return guidedProcessRepository.save(newGp);
                });

        // 5. Check if Step already exists in the GuidedProcess
        boolean stepExists = stepRepository.existsByObjectNameAndGuidedProcess(objectName, gp);
        if (stepExists) {
            return; // Don't add duplicate step
        }

        // 6. Create and save Step
        Step step = new Step();
        step.setObjectName(objectName); // extracted from OBJECT tag
        step.setForm(form);
        step.setGuidedProcess(gp);
        stepRepository.save(step);
    }

    @Override
    public String getXmlByGuidedProcessName(String gpName) {
        GuidedProcess gp = guidedProcessRepository.findByName(gpName)
                .orElseThrow(() -> new RuntimeException("GuidedProcess with name '" + gpName + "' not found"));

        Step step = gp.getSteps().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No Step found for GuidedProcess '" + gpName + "'"));

        FormXML form = step.getForm();
        if (form == null) {
            throw new RuntimeException("No FormXML associated with Step for GuidedProcess '" + gpName + "'");
        }

        return form.getXmlContent();
    }

    @Override
    @Transactional
    public void createTable(stepValuesDTO sVD) {
        String tableName = sVD.getFormLabel();
        List<Champ> champs = sVD.getFormValues();

        StringBuilder sqlCreate = new StringBuilder("CREATE TABLE IF NOT EXISTS `" + tableName + "` (id BIGINT AUTO_INCREMENT PRIMARY KEY");
        for (Champ champ : champs) {
            String columnName = champ.getChampLabel().replaceAll("\\s+", "_");
            String columnType = champ.getChampType().toLowerCase();
            sqlCreate.append(", `" + columnName + "` " + columnType);
        }
        sqlCreate.append(")");

        jdbcTemplate.execute(sqlCreate.toString());

        if (tableExists(tableName)) {


            StringBuilder sqlInsert = new StringBuilder("INSERT INTO `" + tableName + "` (");

            for (int i = 0; i < champs.size(); i++) {
                sqlInsert.append("`").append(champs.get(i).getChampLabel().replaceAll("\\s+", "_")).append("`");
                if (i < champs.size() - 1) sqlInsert.append(", ");
            }

            sqlInsert.append(") VALUES (");

            for (int i = 0; i < champs.size(); i++) {
                sqlInsert.append("'").append(champs.get(i).getChampValue()).append("'");
                if (i < champs.size() - 1) sqlInsert.append(", ");
            }

            sqlInsert.append(")");

            jdbcTemplate.execute(sqlInsert.toString());
        }
    }

    @Override
    public List<FormXML> getAllXml() {
        return formXMLRepository.findAll();
    }

    private boolean tableExists(String tableName) {
        try {
            jdbcTemplate.queryForObject("SELECT 1 FROM `" + tableName + "` LIMIT 1", Integer.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
