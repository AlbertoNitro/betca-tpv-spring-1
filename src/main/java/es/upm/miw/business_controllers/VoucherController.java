package es.upm.miw.business_controllers;

import es.upm.miw.dtos.input.VoucherInputDto;
import es.upm.miw.dtos.output.VoucherOutputDto;

import es.upm.miw.business_services.Encrypting;
import es.upm.miw.documents.Voucher;
import es.upm.miw.dtos.input.VoucherInputDto;
import es.upm.miw.dtos.output.VoucherOutputDto;
import es.upm.miw.exceptions.BadRequestException;
import es.upm.miw.exceptions.NotFoundException;
import es.upm.miw.repositories.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class VoucherController {
    @Autowired
    private VoucherRepository voucherRepository;


    public VoucherOutputDto create(@Valid VoucherInputDto voucherdto) {
        this.validate(voucherdto, "Value");
        String id;
        do {
            id = new Encrypting().shortId64UrlSafe();
        } while (this.voucherRepository.existsById(id));
        Voucher voucher = new Voucher(id, voucherdto.getValue());
        voucherRepository.save(voucher);
        VoucherOutputDto voucherOutput = new VoucherOutputDto(voucher);
        return voucherOutput;
    }

    private void validate(Object property, String message) {
        if (property == null) {
            throw new BadRequestException(message + "Is Null");
        }
    }


}

