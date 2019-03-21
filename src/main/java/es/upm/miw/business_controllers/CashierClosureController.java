package es.upm.miw.business_controllers;

import es.upm.miw.documents.CashierClosure;
import es.upm.miw.dtos.input.CashMovementInputDto;
import es.upm.miw.dtos.input.CashierClosureInputDto;
import es.upm.miw.dtos.output.CashierLastOutputDto;
import es.upm.miw.dtos.output.CashierStateOutputDto;
import es.upm.miw.exceptions.BadRequestException;
import es.upm.miw.exceptions.ConflictException;
import es.upm.miw.repositories.CashierClosureRepository;
import es.upm.miw.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;

@Controller
public class CashierClosureController {

    @Autowired
    private CashierClosureRepository cashierClosureRepository;

    @Autowired
    private UserRepository userRepository;

    public void createCashierClosure() {
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

    public CashierStateOutputDto readTotalsFromLast() {
        CashierClosure lastCashierClosure = this.cashierClosureRepository.findFirstByOrderByOpeningDateDesc();
        if (lastCashierClosure.isClosed()) {
            throw new BadRequestException("Cashier already closed: " + lastCashierClosure.getId());
        }
        BigDecimal salesTotal = lastCashierClosure.getSalesCard().add(lastCashierClosure.getSalesCash())
                .add(lastCashierClosure.getUsedVouchers());

        BigDecimal finalCash = lastCashierClosure.getInitialCash().add(lastCashierClosure.getSalesCash())
                .add(lastCashierClosure.getDeposit()).subtract(lastCashierClosure.getWithdrawal());

        return new CashierStateOutputDto(salesTotal, lastCashierClosure.getSalesCard(), finalCash,
                lastCashierClosure.getUsedVouchers());
    }

    public void close(CashierClosureInputDto cashierClosureInputDto) {
        CashierClosure lastCashierClosure = this.cashierClosureRepository.findFirstByOrderByOpeningDateDesc();
        if (lastCashierClosure.isClosed()) {
            throw new BadRequestException("Cashier already closed: " + lastCashierClosure.getId());
        }
        lastCashierClosure.close(cashierClosureInputDto.getFinalCard(), cashierClosureInputDto.getFinalCash(),
                cashierClosureInputDto.getComment());
        this.cashierClosureRepository.save(lastCashierClosure);
    }

    public void deposit(CashMovementInputDto cashMovementInputDto){
        CashierClosure lastCashierClosure = this.cashierClosureRepository.findFirstByOrderByOpeningDateDesc();
        if (lastCashierClosure.isClosed()) {
            throw new BadRequestException("Cashier already closed: " + lastCashierClosure.getId());
        }
        lastCashierClosure.deposit(cashMovementInputDto.getCash(), cashMovementInputDto.getComment());
        this.cashierClosureRepository.save(lastCashierClosure);
    }

    public void withdrawal(CashMovementInputDto cashMovementInputDto){
        CashierClosure lastCashierClosure = this.cashierClosureRepository.findFirstByOrderByOpeningDateDesc();
        if (lastCashierClosure.isClosed()) {
            throw new BadRequestException("Cashier already closed: " + lastCashierClosure.getId());
        }
        BigDecimal finalCash = lastCashierClosure.getInitialCash()
                .add(lastCashierClosure.getSalesCash())
                .add(lastCashierClosure.getDeposit());
        if(finalCash.compareTo(cashMovementInputDto.getCash()) == -1) {
            throw new ConflictException("You cant withdrawal. There arent enougth cash: " + lastCashierClosure.getId());
        }
        lastCashierClosure.withdrawal(cashMovementInputDto.getCash(), cashMovementInputDto.getComment());
        this.cashierClosureRepository.save(lastCashierClosure);
    }

}
