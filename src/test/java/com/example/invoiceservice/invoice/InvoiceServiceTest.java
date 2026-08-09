package com.example.invoiceservice.invoice;

import com.example.invoiceservice.invoice.request.Invoice;
import com.example.invoiceservice.repository.InvoiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private InvoiceService invoiceService;

    @Test
    void addInvoiceSavesAndReturnsSuccessMessage() {
        Invoice invoice = new Invoice("INV-1", "C-1", "Acme", "2026-06-01", "100.00", "2026-05-17");

        String result = invoiceService.addInvoice(invoice);

        assertThat(result).isEqualTo("Invoice added successfully");
        verify(invoiceRepository, times(1)).save(invoice);
    }

    @Test
    void deleteInvoiceDelegatesToRepository() {
        invoiceService.deleteInvoice("INV-1");

        verify(invoiceRepository, times(1)).deleteById("INV-1");
    }

    @Test
    void getInvoiceByIdReturnsInvoiceWhenFound() {
        Invoice invoice = new Invoice("INV-1", "C-1", "Acme", "2026-06-01", "100.00", "2026-05-17");
        when(invoiceRepository.findById("INV-1")).thenReturn(Optional.of(invoice));

        Invoice result = invoiceService.getInvoiceById("INV-1");

        assertThat(result).isSameAs(invoice);
    }

    @Test
    void getInvoiceByIdReturnsNullWhenMissing() {
        when(invoiceRepository.findById("missing")).thenReturn(Optional.empty());

        Invoice result = invoiceService.getInvoiceById("missing");

        assertThat(result).isNull();
        verify(invoiceRepository, never()).save(any());
    }

    @Test
    void getAllInvoicesReturnsRepositoryListing() {
        Invoice one = new Invoice("INV-1", "C-1", "Acme", "2026-06-01", "100.00", "2026-05-17");
        Invoice two = new Invoice("INV-2", "C-2", "Globex", "2026-07-01", "200.00", "2026-05-17");
        when(invoiceRepository.findAll()).thenReturn(List.of(one, two));

        List<Invoice> result = invoiceService.getAllInvoices();

        assertThat(result).containsExactly(one, two);
    }

    @Test
    void getAllInvoicesReturnsEmptyListWhenRepoEmpty() {
        when(invoiceRepository.findAll()).thenReturn(List.of());

        assertThat(invoiceService.getAllInvoices()).isEmpty();
    }

    @Test
    void deleteWithNullIdStillDelegatesToRepository() {
        invoiceService.deleteInvoice(null);

        verify(invoiceRepository, times(1)).deleteById(null);
    }

    @Test
    void addInvoiceWithAllNullableFieldsNullStillSaves() {
        Invoice invoice = new Invoice("INV-NULLS", null, null, null, null, null);

        String result = invoiceService.addInvoice(invoice);

        assertThat(result).isEqualTo("Invoice added successfully");
        verify(invoiceRepository, times(1)).save(invoice);
    }

    @Test
    void addInvoiceWithNullInvoiceIdStillCallsSave() {
        Invoice invoice = new Invoice(null, "C-1", "Acme", "2026-06-01", "100.00", "2026-05-17");

        invoiceService.addInvoice(invoice);

        verify(invoiceRepository, times(1)).save(invoice);
    }

    @Test
    void getInvoiceByIdWithNullIdDelegatesToRepository() {
        when(invoiceRepository.findById(null)).thenReturn(Optional.empty());

        Invoice result = invoiceService.getInvoiceById(null);

        assertThat(result).isNull();
        verify(invoiceRepository, times(1)).findById(null);
    }

    @Test
    void getAllInvoicesIncludesInvoicesWithNullFields() {
        Invoice withNulls = new Invoice("INV-1", null, null, null, null, null);
        when(invoiceRepository.findAll()).thenReturn(List.of(withNulls));

        List<Invoice> result = invoiceService.getAllInvoices();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCustomerName()).isNull();
        assertThat(result.get(0).getTotalOpenAmount()).isNull();
    }

    @Test
    void addInvoiceDoesNotTouchRepositoryBeyondSave() {
        Invoice invoice = new Invoice("INV-X", "C-X", "X", "2026-06-01", "10.00", "2026-05-17");

        invoiceService.addInvoice(invoice);

        verify(invoiceRepository).save(invoice);
        verify(invoiceRepository, never()).findAll();
        verify(invoiceRepository, never()).findById(any());
        verify(invoiceRepository, never()).deleteById(any());
    }
}
