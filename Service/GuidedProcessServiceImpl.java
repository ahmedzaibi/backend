package com.example.demo.Service;

import com.example.demo.Repository.FormXMLRepository;
import com.example.demo.Repository.GuidedProcessRepository;
import com.example.demo.entity.FormXML;
import com.example.demo.entity.GuidedProcess;
import com.example.demo.entity.Step;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuidedProcessServiceImpl implements GuidedProcessService {

    private final GuidedProcessRepository guidedProcessRepository;
    private final FormXMLRepository formXMLRepository;

    @Override
    @Transactional
    public void deleteGuidedProcessAndAssociations(Long id) {
        // 1. Find the GuidedProcess by its ID. If it doesn't exist, throw an exception.
        GuidedProcess gp = guidedProcessRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("GuidedProcess not found with id: " + id));

        // 2. Before deleting the process, collect all associated FormXML entities that need to be deleted.
        // We do this first because once the steps are deleted, we lose the link to the forms.
        List<FormXML> formsToDelete = gp.getSteps().stream()
                .map(Step::getForm)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 3. Delete the GuidedProcess. Because of the `cascade = CascadeType.ALL` setting
        // on the `steps` list in your GuidedProcess entity, JPA will automatically
        // delete all associated Step entities.
        guidedProcessRepository.delete(gp);

        // 4. Now, delete the FormXML entities that were associated with the now-deleted steps.
        // This is safe because no other steps should be pointing to them.
        if (!formsToDelete.isEmpty()) {
            formXMLRepository.deleteAll(formsToDelete);
        }
    }
}