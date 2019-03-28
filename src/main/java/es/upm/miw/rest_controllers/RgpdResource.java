package es.upm.miw.rest_controllers;

import es.upm.miw.business_services.PdfService;
import es.upm.miw.dtos.RgpdDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Base64;

@PreAuthorize("authenticated")
@RestController
@RequestMapping(RgpdResource.RGPD)
public class RgpdResource {
    public static final String RGPD = "/rgpd";

    public static final String PRINTABLE_AGREEMENT = "/agreement";

    @Autowired
    private PdfService pdfService;

    @PostMapping(value = PRINTABLE_AGREEMENT)
    public RgpdDto create(@RequestBody RgpdDto rgpdInput, @AuthenticationPrincipal User activeUser) {
        RgpdDto rgpdOutput = new RgpdDto();
        rgpdOutput.setAgreementType(rgpdInput.getAgreementType());
        byte[] agreement;
        if (activeUser != null) {
            agreement = pdfService.generatePrintableRgpdAgreement("Active user " + activeUser.getUsername());
        } else {
            agreement = pdfService.generatePrintableRgpdAgreement(SecurityContextHolder.getContext().getAuthentication().getName());
        }
        String content = Base64.getEncoder().encodeToString(agreement);
        rgpdOutput.setPrintableAgreement(content);
        return rgpdOutput;
    }

}
