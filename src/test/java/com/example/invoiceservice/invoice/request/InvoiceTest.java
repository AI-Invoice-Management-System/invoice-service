package com.example.invoiceservice.invoice.request;

import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class InvoiceTest {

    @Test
    void noArgsConstructorDefaultsDateToToday() {
        Invoice invoice = new Invoice();

        assertThat(invoice.getDate())
                .isEqualTo(Date.valueOf(LocalDate.now()).toString());
        assertThat(invoice.getInvoiceId()).isNull();
        assertThat(invoice.getCustomerNumber()).isNull();
        assertThat(invoice.getCustomerName()).isNull();
        assertThat(invoice.getDueInDate()).isNull();
        assertThat(invoice.getTotalOpenAmount()).isNull();
    }

    @Test
    void allArgsConstructorPopulatesEveryField() {
        Invoice invoice = new Invoice(
                "INV-1", "C-1", "Acme", "2026-06-01", "100.00", "2026-05-17"
        );

        assertThat(invoice.getInvoiceId()).isEqualTo("INV-1");
        assertThat(invoice.getCustomerNumber()).isEqualTo("C-1");
        assertThat(invoice.getCustomerName()).isEqualTo("Acme");
        assertThat(invoice.getDueInDate()).isEqualTo("2026-06-01");
        assertThat(invoice.getTotalOpenAmount()).isEqualTo("100.00");
        assertThat(invoice.getDate()).isEqualTo("2026-05-17");
    }

    @Test
    void settersUpdateFields() {
        Invoice invoice = new Invoice();
        invoice.setInvoiceId("INV-2");
        invoice.setCustomerNumber("C-2");
        invoice.setCustomerName("Globex");
        invoice.setDueInDate("2026-07-01");
        invoice.setTotalOpenAmount("250.50");
        invoice.setDate("2026-05-17");

        assertThat(invoice.getInvoiceId()).isEqualTo("INV-2");
        assertThat(invoice.getCustomerNumber()).isEqualTo("C-2");
        assertThat(invoice.getCustomerName()).isEqualTo("Globex");
        assertThat(invoice.getDueInDate()).isEqualTo("2026-07-01");
        assertThat(invoice.getTotalOpenAmount()).isEqualTo("250.50");
        assertThat(invoice.getDate()).isEqualTo("2026-05-17");
    }

    @Test
    void equalsAndHashCodeHonorAllFields() {
        Invoice a = new Invoice("INV-1", "C-1", "Acme", "2026-06-01", "100.00", "2026-05-17");
        Invoice b = new Invoice("INV-1", "C-1", "Acme", "2026-06-01", "100.00", "2026-05-17");
        Invoice c = new Invoice("INV-2", "C-1", "Acme", "2026-06-01", "100.00", "2026-05-17");

        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
        assertThat(a).isNotEqualTo(c);
    }

    @Test
    void toStringIncludesFieldValues() {
        Invoice invoice = new Invoice("INV-1", "C-1", "Acme", "2026-06-01", "100.00", "2026-05-17");

        assertThat(invoice.toString())
                .contains("INV-1", "C-1", "Acme", "2026-06-01", "100.00");
    }

    @Test
    void allArgsConstructorAcceptsNullForEveryField() {
        Invoice invoice = new Invoice(null, null, null, null, null, null);

        assertThat(invoice.getInvoiceId()).isNull();
        assertThat(invoice.getCustomerNumber()).isNull();
        assertThat(invoice.getCustomerName()).isNull();
        assertThat(invoice.getDueInDate()).isNull();
        assertThat(invoice.getTotalOpenAmount()).isNull();
        assertThat(invoice.getDate()).isNull();
    }

    @Test
    void settersAcceptNullValues() {
        Invoice invoice = new Invoice("INV-1", "C-1", "Acme", "2026-06-01", "100.00", "2026-05-17");

        invoice.setCustomerNumber(null);
        invoice.setCustomerName(null);
        invoice.setDueInDate(null);
        invoice.setTotalOpenAmount(null);
        invoice.setDate(null);

        assertThat(invoice.getInvoiceId()).isEqualTo("INV-1");
        assertThat(invoice.getCustomerNumber()).isNull();
        assertThat(invoice.getCustomerName()).isNull();
        assertThat(invoice.getDueInDate()).isNull();
        assertThat(invoice.getTotalOpenAmount()).isNull();
        assertThat(invoice.getDate()).isNull();
    }

    @Test
    void allArgsConstructorOverridesDefaultDateWithNull() {
        Invoice invoice = new Invoice("INV-1", "C-1", "Acme", "2026-06-01", "100.00", null);

        assertThat(invoice.getDate()).isNull();
    }

    @Test
    void toStringHandlesNullFieldsWithoutThrowing() {
        Invoice invoice = new Invoice(null, null, null, null, null, null);

        assertThat(invoice.toString()).contains("null");
    }

    @Test
    void equalsTreatsAllNullInvoicesAsEqual() {
        Invoice a = new Invoice(null, null, null, null, null, null);
        Invoice b = new Invoice(null, null, null, null, null, null);

        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsDistinguishesNullFromValue() {
        Invoice withId = new Invoice("INV-1", null, null, null, null, null);
        Invoice nullId = new Invoice(null, null, null, null, null, null);

        assertThat(withId).isNotEqualTo(nullId);
    }

    @Test
    void invoiceWithOnlyIdSetIsValidObject() {
        Invoice invoice = new Invoice();
        invoice.setInvoiceId("INV-ONLY-ID");

        assertThat(invoice.getInvoiceId()).isEqualTo("INV-ONLY-ID");
        assertThat(invoice.getCustomerNumber()).isNull();
        assertThat(invoice.getCustomerName()).isNull();
        assertThat(invoice.getDueInDate()).isNull();
        assertThat(invoice.getTotalOpenAmount()).isNull();
    }
}
