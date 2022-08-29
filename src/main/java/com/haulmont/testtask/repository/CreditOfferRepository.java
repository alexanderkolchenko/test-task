package com.haulmont.testtask.repository;

import com.haulmont.testtask.models.CreditOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CreditOfferRepository extends JpaRepository<CreditOffer, UUID> {

}
