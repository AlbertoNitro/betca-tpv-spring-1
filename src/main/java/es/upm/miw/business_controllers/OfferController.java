package es.upm.miw.business_controllers;

import es.upm.miw.documents.Offer;
import es.upm.miw.dtos.input.OfferInputDto;
import es.upm.miw.dtos.output.OfferOutputDto;
import es.upm.miw.repositories.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class OfferController {

    @Autowired
    private OfferRepository offerRepository;

    public List<OfferOutputDto> readAll() {
        List<OfferOutputDto> offerOutputDtoList = new ArrayList<>();
        for (Offer offer : this.offerRepository.findAll()) {
            offerOutputDtoList.add(new OfferOutputDto(offer.getId(), offer.getOffername(), offer.getEndDate(), offer.getArticleLine()));
        }
        return offerOutputDtoList;
    }

    public List<OfferOutputDto> search(String id, String offername, String idArticle, String status) {
        LocalDate now = LocalDate.now();
        Instant instant = now.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Date dateNow = Date.from(instant);
        return this.offerRepository.findByIdOffernameEndDateArticleId(id, offername, status, dateNow, idArticle);
    }

    public OfferOutputDto create(OfferInputDto offerInputDto) {
        offerInputDto.setEndDate(offerInputDto.getEndDate().plusDays(1));
        Offer offer = new Offer(Integer.toString(offerInputDto.hashCode()), offerInputDto.getOffername() , offerInputDto.getEndDate(), offerInputDto.getArticleLine());
        this.offerRepository.save(offer);
        return new OfferOutputDto(offer.getId(), offer.getOffername(), offer.getEndDate(), offer.getArticleLine());
    }

    public void delete(String offerId) {
        Optional<Offer> offer = this.offerRepository.findById(offerId);
        if (offer.isPresent()) {
            this.offerRepository.delete(offer.get());
        }
    }
}
