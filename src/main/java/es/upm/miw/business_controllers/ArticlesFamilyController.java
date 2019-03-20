package es.upm.miw.business_controllers;

import es.upm.miw.documents.FamilyComposite;
import es.upm.miw.documents.FamilyType;
import es.upm.miw.dtos.ArticleFamilyMinimumDto;
import es.upm.miw.dtos.FamilyCompositeDto;
import es.upm.miw.exceptions.BadRequestException;
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

    public void deleteFamilyCompositeItem(String description) {
        familyCompositeRepository.delete(familyCompositeRepository.findByDescription(description));
    }

    public FamilyCompositeDto createFamilyComposite(FamilyCompositeDto familyCompositeDto, String description) {
        FamilyComposite familyToBeAttached = familyCompositeRepository.findByDescription(description);
        if (familyToBeAttached == null) {
            throw new BadRequestException("No valid description provided");
        }
        FamilyComposite compositeCreated = familyCompositeRepository.save(new FamilyComposite(
                familyCompositeDto.getFamilyType(),
                familyCompositeDto.getReference(),
                familyCompositeDto.getDescription()));
        familyToBeAttached.getFamilyCompositeList().add(compositeCreated);
        familyCompositeRepository.save(familyToBeAttached);
        return familyCompositeDto;
    }
}
