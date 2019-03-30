package es.upm.miw.rest_controllers;

import es.upm.miw.business_services.PdfService;
import es.upm.miw.documents.RgpdAgreement;
import es.upm.miw.documents.RgpdAgreementType;
import es.upm.miw.documents.User;
import es.upm.miw.dtos.RgpdDto;
import es.upm.miw.exceptions.UnauthorizedException;
import es.upm.miw.repositories.RgpdAgreementRepository;
import es.upm.miw.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@PreAuthorize("authenticated")
@RestController
@RequestMapping(RgpdResource.RGPD)
public class RgpdResource {
    public static final String RGPD = "/rgpd";

    public static final String PRINTABLE_AGREEMENT = "/agreement";

    public static final String USER_AGREEMENT = "/useragreement";

    @Autowired
    private PdfService pdfService;

    @Autowired
    private RgpdAgreementRepository rgpdAgreementRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = PRINTABLE_AGREEMENT)
    public RgpdDto createPrintableAgreement(@RequestBody RgpdDto rgpdInput) {
        RgpdDto rgpdOutput = new RgpdDto();
        rgpdOutput.setAgreementType(rgpdInput.getAgreementType());
        byte[] agreement;
        agreement = pdfService.generatePrintableRgpdAgreement(SecurityContextHolder.getContext().getAuthentication().getName());
        String content = Base64.getEncoder().encodeToString(agreement);
        rgpdOutput.setPrintableAgreement(content);
        return rgpdOutput;
    }

    @GetMapping(value = USER_AGREEMENT)
    public RgpdDto getUserAgreement() {
        RgpdDto result = new RgpdDto();
        List<RgpdAgreement> rgpdAgreementList = rgpdAgreementRepository.findByAssignee(getAuthenticathedUser().getId());
        if (rgpdAgreementList.size() > 0) {
            RgpdAgreement rgpdAgreement = rgpdAgreementList.get(0);
            String content = Base64.getEncoder().encodeToString(rgpdAgreement.getAgreement());
            result.setAccepted(true);
            result.setPrintableAgreement(content);
            switch (rgpdAgreement.getType()) {
                case BASIC:
                    result.setAgreementType("1");
                    break;
                case MEDIUM:
                    result.setAgreementType("2");
                    break;
                case ADVANCE:
                    result.setAgreementType("3");
                    break;
            }
        } else {
            result.setAccepted(false);
        }
        return result;
    }

    @PostMapping(value = USER_AGREEMENT)
    public RgpdDto saveUserAgreement(@RequestBody RgpdDto dto) {
        byte[] agreement = Base64.getDecoder().decode(dto.getPrintableAgreement());
        RgpdAgreement rgpd = new RgpdAgreement();
        rgpd.setAgreement(agreement);
        rgpd.setAssignee(this.getAuthenticathedUser());
        switch (dto.getPrintableAgreement()) {
            case "1":
                rgpd.setType(RgpdAgreementType.BASIC);
                break;

            case "2":
                rgpd.setType(RgpdAgreementType.MEDIUM);
                break;
            case "3":
                rgpd.setType(RgpdAgreementType.ADVANCE);
                break;
        }

        this.rgpdAgreementRepository.save(rgpd);
        return dto;
    }

    @DeleteMapping(value = USER_AGREEMENT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserAgreement() {
        List<RgpdAgreement> rgpdAgreementList = this.rgpdAgreementRepository.findByAssignee(getAuthenticathedUser().getId());
        for (RgpdAgreement rgpdAgreement : rgpdAgreementList)
            this.rgpdAgreementRepository.delete(rgpdAgreement);
    }

    private User getAuthenticathedUser() {
        Optional<User> optional = userRepository.findByMobile(SecurityContextHolder.getContext().getAuthentication().getName());
        if (optional.isPresent())
            return optional.get();
        throw new UnauthorizedException("Usuario no encontrado.");
    }
}
