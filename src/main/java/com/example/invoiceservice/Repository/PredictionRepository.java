package com.example.invoiceservice.Repository;

import com.example.invoiceservice.Predict.Request.PredictionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PredictionRepository extends JpaRepository<PredictionRequest, String> {

}
