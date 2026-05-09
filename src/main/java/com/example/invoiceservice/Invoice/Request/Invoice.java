package com.example.invoiceservice.Invoice.Request;

public record Invoice(
        String businessCode,
        String customerNumber,
        String customerName,
        String clearDate,
        String businessYear,
        String docId,
        String postingDate,
        String documentCreateDate,
        String documentCreateDate1,
        String dueInDate,
        String invoiceCurrency,
        String documentType,
        String postingId,
        String areaBusiness,
        String totalOpenAmount,
        String baseLineCreateDate,
        String customerPaymentTerms,
        String invoiceId,
        String isOpen
) {}