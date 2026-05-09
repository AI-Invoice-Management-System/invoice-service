package com.example.invoiceservice.Invoice;

import com.example.invoiceservice.Invoice.Request.Invoice;
import org.springframework.stereotype.Component;

@Component
public class InvoiceService {
    public String addInvoice(Invoice invoice) {
        return "Invoice added successfully";
    }
}
