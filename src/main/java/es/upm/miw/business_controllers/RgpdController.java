package es.upm.miw.business_controllers;

import es.upm.miw.business_services.PdfService;
import es.upm.miw.documents.RgpdAgreement;
import es.upm.miw.documents.RgpdAgreementType;
import es.upm.miw.documents.User;
import es.upm.miw.dtos.RgpdDto;
import es.upm.miw.repositories.RgpdAgreementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class RgpdController {

    @Autowired
    private PdfService pdfService;

    @Autowired
    private RgpdAgreementRepository rgpdAgreementRepository;

    public RgpdDto createPrintableAgreement(User user, RgpdAgreementType type) {
        RgpdDto result = new RgpdDto();
        byte[] agreement = pdfService.generatePrintableRgpdAgreement(user, type);
        result.setPrintableAgreement(agreement);
        result.setAgreementType(type.toString());
        return result;
    }

    public RgpdDto getUserAgreement(User user) {
        List<RgpdAgreement> rgpdAgreementList = rgpdAgreementRepository.findByAssignee(user.getId());
        if (!rgpdAgreementList.isEmpty())
            return new RgpdDto(rgpdAgreementList.get(0));
        else
            return new RgpdDto();
    }

    public RgpdDto saveUserAgreement(User user, RgpdAgreementType type, byte[] agreement){
        RgpdAgreement rgpd = new RgpdAgreement();
        rgpd.setAgreement(agreement);
        rgpd.setAssignee(user);
        rgpd.setType(type);
        rgpd = this.rgpdAgreementRepository.save(rgpd);
        return new RgpdDto(rgpd);
    }

    public void deleteUserAgreement(User user){
        List<RgpdAgreement> rgpdAgreementList = this.rgpdAgreementRepository.findByAssignee(user.getId());
        for (RgpdAgreement rgpdAgreement : rgpdAgreementList)
            this.rgpdAgreementRepository.delete(rgpdAgreement);
    }
}
