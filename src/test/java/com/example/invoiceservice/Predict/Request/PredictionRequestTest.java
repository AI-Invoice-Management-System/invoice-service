package com.example.invoiceservice.Predict.Request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PredictionRequestTest {

    @Test
    void noArgsConstructorLeavesFieldsNull() {
        PredictionRequest request = new PredictionRequest();

        assertThat(request.getInvoiceId()).isNull();
        assertThat(request.getPredictedDate()).isNull();
    }

    @Test
    void allArgsConstructorPopulatesFields() {
        PredictionRequest request = new PredictionRequest("INV-1", "2026-06-01");

        assertThat(request.getInvoiceId()).isEqualTo("INV-1");
        assertThat(request.getPredictedDate()).isEqualTo("2026-06-01");
    }

    @Test
    void settersUpdateFields() {
        PredictionRequest request = new PredictionRequest();
        request.setInvoiceId("INV-2");
        request.setPredictedDate("2026-07-01");

        assertThat(request.getInvoiceId()).isEqualTo("INV-2");
        assertThat(request.getPredictedDate()).isEqualTo("2026-07-01");
    }

    @Test
    void equalsAndHashCodeHonorAllFields() {
        PredictionRequest a = new PredictionRequest("INV-1", "2026-06-01");
        PredictionRequest b = new PredictionRequest("INV-1", "2026-06-01");
        PredictionRequest c = new PredictionRequest("INV-1", "2026-06-02");

        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
        assertThat(a).isNotEqualTo(c);
    }
}
