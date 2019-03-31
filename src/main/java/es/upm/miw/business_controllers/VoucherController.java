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
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
        Voucher voucherBBDD = readVoucher(code);
        if (!voucherBBDD.isUsed()) {
            voucherBBDD.use();
        }
        Voucher v = this.voucherRepository.save(voucherBBDD);
        VoucherOutputDto voutput = new VoucherOutputDto(v);
        return voutput;
    }

    private Voucher readVoucher(String code) {
        return (this.voucherRepository.findById(code)
                .orElseThrow(() -> new NotFoundException("Voucher code (" + code + ")")));
    }

    public List<VoucherOutputDto> findVouchersByDateWithinConsumed(String dateFrom, String dateTo) {
        this.validate(dateFrom, "DateFrom");
        this.validate(dateTo, "Date to");
        List<Voucher> listVouchers = this.voucherRepository.findByCreationDateBetweenAndDateOfUseIsNull(LocalDateTime.parse(dateFrom), LocalDateTime.parse(dateTo));
        return listVouchers.stream()
                .sorted(Comparator.comparing(Voucher::getCreationDate)).map(VoucherOutputDto::new).collect(Collectors.toList());
    }

    public List<VoucherOutputDto> findVouchersByDateConsumed(String dateFrom, String dateTo) {
        this.validate(dateFrom, "DateFrom");
        this.validate(dateTo, "Date to");
        List<Voucher> listVouchers = this.voucherRepository.findByCreationDateBetweenAndDateOfUseIsNotNull(LocalDateTime.parse(dateFrom), LocalDateTime.parse(dateTo));
        return listVouchers.stream()
                .sorted(Comparator.comparing(Voucher::getCreationDate)).map(VoucherOutputDto::new).collect(Collectors.toList());

    }

    public VoucherOutputDto readById(String id) {
        this.validate(id, "id");
        Optional<Voucher> voucher = voucherRepository.findById(id);
        if (!voucher.isPresent())
            throw new NotFoundException("Voucher id (" + id + ")");
        else
            return new VoucherOutputDto(voucher.get());


    }
}

