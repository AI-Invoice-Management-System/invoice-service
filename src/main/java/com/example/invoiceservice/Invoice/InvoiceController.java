package com.example.invoiceservice.Invoice;

import com.example.invoiceservice.Invoice.Request.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/invoices")
public class InvoiceController {
    @Autowired InvoiceService invoiceService;

    @PostMapping("/add")
    public String addInvoice(@RequestBody Invoice invoice) {
        return invoiceService.addInvoice(invoice);
    }
}
