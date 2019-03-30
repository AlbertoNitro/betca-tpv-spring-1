package es.upm.miw.business_controllers;

import es.upm.miw.dtos.input.VoucherInputDto;
import es.upm.miw.dtos.output.VoucherOutputDto;

import es.upm.miw.business_services.Encrypting;
import es.upm.miw.documents.Voucher;
import es.upm.miw.exceptions.BadRequestException;
import es.upm.miw.exceptions.NotFoundException;
import es.upm.miw.repositories.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class VoucherController {
    @Autowired
    private VoucherRepository voucherRepository;


    public VoucherOutputDto create(@Valid VoucherInputDto voucherInput) {
        this.validate(voucherInput, "Value");
        String id;
        do {
            id = new Encrypting().shortId64UrlSafe();
        } while (this.voucherRepository.existsById(id));
        Voucher voucher = new Voucher(id, voucherInput.getValue());
        voucherRepository.save(voucher);
        VoucherOutputDto voucherOutput = new VoucherOutputDto(voucher);
        return voucherOutput;
    }

    private void validate(Object property, String message) {
        if (property == null) {
            throw new BadRequestException(message + "Is Null");
        }
    }

    public List<VoucherOutputDto> readAll() {
        List<Voucher> lVouchers = this.voucherRepository.findAllVouchers();
        return lVouchers.stream()
                .sorted(Comparator.comparing(Voucher::getCreationDate)).map(VoucherOutputDto::new).collect(Collectors.toList());

    }

    public VoucherOutputDto update(String code) {
        this.validate(code, "Code");
        Voucher voucher = new Voucher();
        if (this.voucherRepository.existsById(code)) {
            voucher = this.voucherRepository.findById(code)
                    .orElseThrow(() -> new NotFoundException("Voucher (" + code + ")"));
        }
        if (!voucher.isUsed()) {
            voucher.use();
        }
        this.voucherRepository.save(voucher);
        return new VoucherOutputDto(voucher);
    }


}

