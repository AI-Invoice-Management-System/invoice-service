package com.example.invoiceservice.repository;

import com.example.invoiceservice.predict.request.PredictionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PredictionRepository extends JpaRepository<PredictionRequest, String> {

}
