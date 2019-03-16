package es.upm.miw.business_controllers;

import es.upm.miw.documents.FamilyType;
import es.upm.miw.dtos.ArticleFamilyMinimumDto;
import es.upm.miw.repositories.FamilyCompositeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ArticlesFamilyController {

    @Autowired
    private FamilyCompositeRepository familyCompositeRepository;

    public List<ArticleFamilyMinimumDto> readAllFamilyCompositeByFamilyType(FamilyType familyType) {
        return familyCompositeRepository.findAllFamilyCompositeByFamilyType(familyType);
    }

    public void deleteFamilyCompositeItem (String description){
        familyCompositeRepository.delete(familyCompositeRepository.findByDescription(description));
    }
}
