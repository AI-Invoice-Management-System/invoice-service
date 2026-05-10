package com.example.invoiceservice.Invoice;

import com.example.invoiceservice.Invoice.Request.Invoice;
import com.example.invoiceservice.Repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;

    public String addInvoice(Invoice invoice) {
        invoiceRepository.save(invoice);
        return "Invoice added successfully";
    }
}
