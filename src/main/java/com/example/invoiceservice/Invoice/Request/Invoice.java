package com.example.invoiceservice.Invoice.Request;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Invoices")
public class Invoice {
    @Id private String invoiceId;
    private String customerNumber;
    private String customerName;
    private String dueInDate;
    private String totalOpenAmount;
    private String date = Date.valueOf(LocalDate.now()).toString();
}