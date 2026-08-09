package com.example.invoiceservice.predict.request;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Predictions")
public class PredictionRequest {
    @Id private String invoiceId;
    private String predictedDate;
}
