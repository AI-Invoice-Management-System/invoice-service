package com.example.invoiceservice.Invoice;

import com.example.invoiceservice.Invoice.Request.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {
    @Autowired InvoiceService invoiceService;

    @PostMapping("/add")
    public String addInvoice(@RequestBody Invoice invoice) {
        return invoiceService.addInvoice(invoice);
    }

    @PostMapping("/delete")
    public void deleteInvoice(@RequestBody String invoiceId) {
        invoiceService.deleteInvoice(invoiceId);
    }

    @GetMapping("/all")
    public List<Invoice> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

    @GetMapping("/get/{invoiceId}")
    public Invoice getInvoiceById(@PathVariable String invoiceId) {
        return invoiceService.getInvoiceById(invoiceId);
    }
}
