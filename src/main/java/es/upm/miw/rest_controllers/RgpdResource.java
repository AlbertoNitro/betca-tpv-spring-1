package es.upm.miw.rest_controllers;

import es.upm.miw.business_controllers.RgpdController;
import es.upm.miw.documents.RgpdAgreementType;
import es.upm.miw.documents.User;
import es.upm.miw.dtos.RgpdDto;
import es.upm.miw.exceptions.UnauthorizedException;
import es.upm.miw.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Base64;
import java.util.Optional;

@PreAuthorize("authenticated")
@RestController
@RequestMapping(RgpdResource.RGPD)
public class RgpdResource {
    public static final String RGPD = "/rgpd";

    public static final String PRINTABLE_AGREEMENT = "/agreement";

    public static final String USER_AGREEMENT = "/useragreement";

    @Autowired
    private RgpdController rgpdController;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = PRINTABLE_AGREEMENT)
    public RgpdDto createPrintableAgreement(@RequestBody RgpdDto rgpdInput) {
        RgpdDto rgpdOutput = rgpdController.createPrintableAgreement(getAuthenticathedUser(), getAgreementTypeFromDto(rgpdInput));
        rgpdOutput.setAgreementType(rgpdInput.getAgreementType());
        return rgpdOutput;
    }

    @GetMapping(value = USER_AGREEMENT)
    public RgpdDto getUserAgreement() {
        return rgpdController.getUserAgreement(getAuthenticathedUser());
    }

    @PostMapping(value = USER_AGREEMENT)
    public RgpdDto saveUserAgreement(@RequestBody RgpdDto dto) {
        byte[] agreement = Base64.getDecoder().decode(dto.getPrintableAgreement());
        return this.rgpdController.saveUserAgreement(getAuthenticathedUser(), getAgreementTypeFromDto(dto), agreement);
    }

    @DeleteMapping(value = USER_AGREEMENT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserAgreement() {
        this.rgpdController.deleteUserAgreement(getAuthenticathedUser());
    }

    private User getAuthenticathedUser() {
        Optional<User> optional = userRepository.findByMobile(SecurityContextHolder.getContext().getAuthentication().getName());
        if (optional.isPresent())
            return optional.get();
        throw new UnauthorizedException("Usuario no encontrado.");
    }

    private RgpdAgreementType getAgreementTypeFromDto(RgpdDto dto) {
        RgpdAgreementType type = null;
        if (dto.getAgreementType().equalsIgnoreCase("1"))
            type = RgpdAgreementType.BASIC;
        if (dto.getAgreementType().equalsIgnoreCase("2"))
            type = RgpdAgreementType.MEDIUM;
        if (dto.getAgreementType().equalsIgnoreCase("3"))
            type = RgpdAgreementType.ADVANCE;
        return type;
    }
}
