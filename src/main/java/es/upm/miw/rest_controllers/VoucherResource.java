package es.upm.miw.rest_controllers;


import es.upm.miw.business_controllers.VoucherController;
import es.upm.miw.dtos.BudgetDto;
import es.upm.miw.dtos.input.VoucherInputDto;
import es.upm.miw.dtos.output.VoucherOutputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(VoucherResource.VOUCHERS)
public class VoucherResource {
    public static final String VOUCHERS = "/vouchers";
    public static final String ID = "/{id}";
    public static final String SEARCH = "/search";

    @Autowired
    private VoucherController voucherController;

    @PreAuthorize("authenticated")
    @PostMapping(produces = {"application/json"})
    public VoucherOutputDto createVoucher(@Valid @RequestBody VoucherInputDto voucher) {
        return this.voucherController.create(voucher);
    }

    @PreAuthorize("authenticated")
    @GetMapping
    public List<VoucherOutputDto> readAll() {
        return this.voucherController.readAll();
    }

    @PutMapping(value = ID)
    public VoucherOutputDto update(@Valid @PathVariable String id) {
        return this.voucherController.update(id);
    }

    @PreAuthorize("authenticated")
    @GetMapping(value = SEARCH, produces = {"application/json"})
    public List<VoucherOutputDto> search(@RequestParam boolean consumed, @RequestParam String dateFrom, @RequestParam String dateTo) {
        List<VoucherOutputDto> dataReturn;
        if (consumed) {
            dataReturn = this.voucherController.findVouchersByDateConsumed(dateFrom, dateTo);

        } else {
            dataReturn = this.voucherController.findVouchersByDateWithinConsumed(dateFrom, dateTo);

        }
        return dataReturn;
    }

    @GetMapping(value = ID)
    public VoucherOutputDto readById(@PathVariable String id) {
        return this.voucherController.readById(id);
    }

}
