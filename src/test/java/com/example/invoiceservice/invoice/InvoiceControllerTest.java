package com.example.invoiceservice.invoice;

import com.example.invoiceservice.invoice.request.Invoice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InvoiceController.class)
class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private InvoiceService invoiceService;

    @Test
    void addInvoiceReturnsSuccessMessage() throws Exception {
        Invoice invoice = new Invoice("INV-1", "C-1", "Acme", "2026-06-01", "100.00", "2026-05-17");
        when(invoiceService.addInvoice(any(Invoice.class))).thenReturn("Invoice added successfully");

        mockMvc.perform(post("/invoices/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invoice)))
                .andExpect(status().isOk())
                .andExpect(content().string("Invoice added successfully"));

        verify(invoiceService, times(1)).addInvoice(any(Invoice.class));
    }

    @Test
    void deleteInvoiceDelegatesToService() throws Exception {
        mockMvc.perform(post("/invoices/delete")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("INV-1"))
                .andExpect(status().isOk());

        verify(invoiceService, times(1)).deleteInvoice(eq("INV-1"));
    }

    @Test
    void getAllInvoicesReturnsListAsJson() throws Exception {
        Invoice one = new Invoice("INV-1", "C-1", "Acme", "2026-06-01", "100.00", "2026-05-17");
        Invoice two = new Invoice("INV-2", "C-2", "Globex", "2026-07-01", "200.00", "2026-05-17");
        when(invoiceService.getAllInvoices()).thenReturn(List.of(one, two));

        mockMvc.perform(get("/invoices/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].invoiceId").value("INV-1"))
                .andExpect(jsonPath("$[1].invoiceId").value("INV-2"));
    }

    @Test
    void getAllInvoicesReturnsEmptyArrayWhenNone() throws Exception {
        when(invoiceService.getAllInvoices()).thenReturn(List.of());

        mockMvc.perform(get("/invoices/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getInvoiceByIdReturnsInvoiceJson() throws Exception {
        Invoice invoice = new Invoice("INV-1", "C-1", "Acme", "2026-06-01", "100.00", "2026-05-17");
        when(invoiceService.getInvoiceById("INV-1")).thenReturn(invoice);

        mockMvc.perform(get("/invoices/get/INV-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invoiceId").value("INV-1"))
                .andExpect(jsonPath("$.customerName").value("Acme"))
                .andExpect(jsonPath("$.totalOpenAmount").value("100.00"));
    }

    @Test
    void getInvoiceByIdReturnsEmptyBodyWhenMissing() throws Exception {
        when(invoiceService.getInvoiceById("missing")).thenReturn(null);

        mockMvc.perform(get("/invoices/get/missing"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}
