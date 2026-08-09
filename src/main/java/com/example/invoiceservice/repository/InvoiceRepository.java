package com.example.invoiceservice.repository;

import com.example.invoiceservice.invoice.request.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {

}
