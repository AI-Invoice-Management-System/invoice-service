package com.example.invoiceservice.Repository;

import com.example.invoiceservice.Predict.Request.PredictionRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace.ANY;

@DataJpaTest
@AutoConfigureTestDatabase(replace = ANY)
class PredictionRepositoryTest {

    @Autowired
    private PredictionRepository predictionRepository;

    @Test
    void saveAndFindByIdRoundtripsPrediction() {
        PredictionRequest request = new PredictionRequest("INV-1", "2026-06-01");

        predictionRepository.save(request);

        Optional<PredictionRequest> found = predictionRepository.findById("INV-1");
        assertThat(found).isPresent();
        assertThat(found.get().getPredictedDate()).isEqualTo("2026-06-01");
    }

    @Test
    void findAllReturnsAllPersistedRows() {
        predictionRepository.save(new PredictionRequest("INV-1", "2026-06-01"));
        predictionRepository.save(new PredictionRequest("INV-2", "2026-07-01"));

        assertThat(predictionRepository.findAll())
                .hasSize(2)
                .extracting(PredictionRequest::getInvoiceId)
                .containsExactlyInAnyOrder("INV-1", "INV-2");
    }

    @Test
    void deleteByIdRemovesPrediction() {
        predictionRepository.save(new PredictionRequest("INV-1", "2026-06-01"));

        predictionRepository.deleteById("INV-1");

        assertThat(predictionRepository.findById("INV-1")).isEmpty();
    }

    @Test
    void findByIdReturnsEmptyForUnknownId() {
        assertThat(predictionRepository.findById("missing")).isEmpty();
    }
}
