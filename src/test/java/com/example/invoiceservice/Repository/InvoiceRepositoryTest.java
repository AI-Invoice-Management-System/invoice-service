package com.example.invoiceservice.Repository;

import com.example.invoiceservice.Invoice.Request.Invoice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace.ANY;

@DataJpaTest
@AutoConfigureTestDatabase(replace = ANY)
class InvoiceRepositoryTest {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Test
    void saveAndFindByIdRoundtripsInvoice() {
        Invoice invoice = new Invoice("INV-1", "C-1", "Acme", "2026-06-01", "100.00", "2026-05-17");

        invoiceRepository.save(invoice);

        Optional<Invoice> found = invoiceRepository.findById("INV-1");
        assertThat(found).isPresent();
        assertThat(found.get().getCustomerName()).isEqualTo("Acme");
        assertThat(found.get().getTotalOpenAmount()).isEqualTo("100.00");
    }

    @Test
    void findAllReturnsEveryPersistedInvoice() {
        invoiceRepository.save(new Invoice("INV-1", "C-1", "Acme", "2026-06-01", "100.00", "2026-05-17"));
        invoiceRepository.save(new Invoice("INV-2", "C-2", "Globex", "2026-07-01", "200.00", "2026-05-17"));

        List<Invoice> all = invoiceRepository.findAll();

        assertThat(all).hasSize(2)
                .extracting(Invoice::getInvoiceId)
                .containsExactlyInAnyOrder("INV-1", "INV-2");
    }

    @Test
    void deleteByIdRemovesInvoice() {
        invoiceRepository.save(new Invoice("INV-1", "C-1", "Acme", "2026-06-01", "100.00", "2026-05-17"));

        invoiceRepository.deleteById("INV-1");

        assertThat(invoiceRepository.findById("INV-1")).isEmpty();
    }

    @Test
    void findByIdReturnsEmptyForUnknownId() {
        assertThat(invoiceRepository.findById("does-not-exist")).isEmpty();
    }

    @Test
    void savesInvoiceWithAllOptionalFieldsNull() {
        Invoice invoice = new Invoice("INV-NULLS", null, null, null, null, null);

        invoiceRepository.save(invoice);

        Optional<Invoice> found = invoiceRepository.findById("INV-NULLS");
        assertThat(found).isPresent();
        assertThat(found.get().getCustomerNumber()).isNull();
        assertThat(found.get().getCustomerName()).isNull();
        assertThat(found.get().getDueInDate()).isNull();
        assertThat(found.get().getTotalOpenAmount()).isNull();
        assertThat(found.get().getDate()).isNull();
    }

    @Test
    void savesInvoiceWithPartiallyNullFields() {
        Invoice invoice = new Invoice("INV-PARTIAL", "C-1", null, "2026-06-01", null, "2026-05-17");

        invoiceRepository.save(invoice);

        Optional<Invoice> found = invoiceRepository.findById("INV-PARTIAL");
        assertThat(found).isPresent();
        assertThat(found.get().getCustomerNumber()).isEqualTo("C-1");
        assertThat(found.get().getCustomerName()).isNull();
        assertThat(found.get().getDueInDate()).isEqualTo("2026-06-01");
        assertThat(found.get().getTotalOpenAmount()).isNull();
        assertThat(found.get().getDate()).isEqualTo("2026-05-17");
    }

    @Test
    void updatesFieldToNull() {
        invoiceRepository.save(new Invoice("INV-1", "C-1", "Acme", "2026-06-01", "100.00", "2026-05-17"));

        Invoice update = new Invoice("INV-1", "C-1", null, "2026-06-01", null, "2026-05-17");
        invoiceRepository.save(update);

        Optional<Invoice> found = invoiceRepository.findById("INV-1");
        assertThat(found).isPresent();
        assertThat(found.get().getCustomerName()).isNull();
        assertThat(found.get().getTotalOpenAmount()).isNull();
    }

    @Test
    void savingSameIdOverwritesExistingRow() {
        invoiceRepository.save(new Invoice("INV-1", "C-1", "Acme", "2026-06-01", "100.00", "2026-05-17"));
        invoiceRepository.save(new Invoice("INV-1", "C-1", "Acme Updated", "2026-06-01", "150.00", "2026-05-17"));

        Optional<Invoice> found = invoiceRepository.findById("INV-1");
        assertThat(found).isPresent();
        assertThat(found.get().getCustomerName()).isEqualTo("Acme Updated");
        assertThat(found.get().getTotalOpenAmount()).isEqualTo("150.00");
        assertThat(invoiceRepository.count()).isEqualTo(1);
    }
}
