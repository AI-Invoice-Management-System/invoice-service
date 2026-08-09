package com.example.invoiceservice.invoice;

import com.example.invoiceservice.invoice.request.Invoice;
import com.example.invoiceservice.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
public class InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;

    public String addInvoice(Invoice invoice) {
        invoiceRepository.save(invoice);
        return "Invoice added successfully";
    }

    public void deleteInvoice(String invoiceId) {
        invoiceRepository.deleteById(invoiceId);
    }

    public Invoice getInvoiceById(String invoiceId) {
        return invoiceRepository.findById(invoiceId).orElse(null);
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }
}
