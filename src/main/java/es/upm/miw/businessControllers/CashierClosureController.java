package es.upm.miw.businessControllers;

import es.upm.miw.documents.CashierClosure;
import es.upm.miw.dtos.CashierClosingOutputDto;
import es.upm.miw.dtos.CashierClosureInputDto;
import es.upm.miw.dtos.CashierLastOutputDto;
import es.upm.miw.exceptions.BadRequestException;
import es.upm.miw.repositories.CashierClosureRepository;
import es.upm.miw.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Controller
public class CashierClosureController {

    @Autowired
    private CashierClosureRepository cashierClosureRepository;

    @Autowired
    private UserRepository userRepository;

    public void createCashierClosure() throws BadRequestException {
        CashierClosure lastCashierClosure = this.cashierClosureRepository.findFirstByOrderByOpeningDateDesc();
        if (!lastCashierClosure.isClosed()) {
            throw new BadRequestException("Already opened: " + lastCashierClosure.getId());
        }
        this.cashierClosureRepository.save(new CashierClosure(lastCashierClosure.getFinalCash()));
    }

    public CashierLastOutputDto readCashierClosureLast() {
        CashierClosure lastCashierClosure = this.cashierClosureRepository.findFirstByOrderByOpeningDateDesc();
        return new CashierLastOutputDto(lastCashierClosure);
    }

    public CashierClosingOutputDto readTotalsFromLast() throws BadRequestException {
        CashierClosure lastCashierClosure = this.cashierClosureRepository.findFirstByOrderByOpeningDateDesc();
        if (lastCashierClosure.isClosed()) {
            throw new BadRequestException("Cashier already closed: " + lastCashierClosure.getId());
        }
        CashierClosingOutputDto cashierClosingOutputDto = new CashierClosingOutputDto();
        //TODO Calcular total vouches de la ultima caja
        cashierClosingOutputDto.setTotalVoucher(BigDecimal.ZERO);
        //TODO Calcular total sales card de la ultima caja
        cashierClosingOutputDto.setTotalCard(BigDecimal.ZERO);
        //TODO Calcular total sales cash de la ultima caja
        cashierClosingOutputDto.setTotalCash(BigDecimal.ZERO);
        //TODO Calcular total sales de la ultima caja
        cashierClosingOutputDto.setSalesTotal(BigDecimal.ZERO);
        return cashierClosingOutputDto;
    }

    public void close(CashierClosureInputDto cashierClosureInputDto) throws BadRequestException {
        CashierClosure lastCashierClosure = this.cashierClosureRepository.findFirstByOrderByOpeningDateDesc();
        if (lastCashierClosure.isClosed()) {
            throw new BadRequestException("Cashier already closed: " + lastCashierClosure.getId());
        }
        LocalDateTime cashierOpenedDate = cashierClosureRepository.findFirstByOrderByOpeningDateDesc().getOpeningDate();
        //TODO falta calcular el dinero total que queda despues de cerrar, con perdidas y todo lo necesario
        lastCashierClosure.close(cashierClosureInputDto.getSalesCard(), BigDecimal.ZERO, BigDecimal.ZERO,
                cashierClosureInputDto.getFinalCash(), BigDecimal.ZERO, cashierClosureInputDto.getComment());
        this.cashierClosureRepository.save(lastCashierClosure);
    }

}
