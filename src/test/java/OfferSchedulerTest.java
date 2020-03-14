import model.Offer;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class OfferSchedulerTest {

    private final LocalDate offer1Start = LocalDate.now().minusDays(1);
    private final LocalDate offer1End = LocalDate.now().plusDays(2);
    private final LocalDate offer2Start = LocalDate.now().plusDays(1);
    private final LocalDate offer2End = LocalDate.now().plusDays(10);

    private final Offer offer1 = (b) -> Map.of();
    private final Offer offer2 = (b) -> Map.of();

    final OfferScheduler testee = OfferScheduler.forOfferDurations(Map.of(
            new OfferScheduler.OfferDuration(offer1Start, offer1End), offer1,
            new OfferScheduler.OfferDuration(offer2Start, offer2End), offer2));

    @Test
    void noOffersScheduled() {
        var testee = OfferScheduler.empty();
        assertThat(testee.getOffersForDate(LocalDate.now())).isEmpty();
    }

    @Test
    void getOffersForDate() {

        assertThat(testee.getOffersForDate(LocalDate.now().minusDays(2))).isEmpty();
        assertThat(testee.getOffersForDate(LocalDate.now().minusDays(1))).containsExactly(offer1);
        assertThat(testee.getOffersForDate(LocalDate.now())).containsExactly(offer1);
        assertThat(testee.getOffersForDate(LocalDate.now().plusDays(1))).containsExactlyInAnyOrder(offer1, offer2);
        assertThat(testee.getOffersForDate(LocalDate.now().plusDays(2))).containsExactlyInAnyOrder(offer1, offer2);
        assertThat(testee.getOffersForDate(LocalDate.now().plusDays(3))).containsExactlyInAnyOrder(offer2);
        assertThat(testee.getOffersForDate(LocalDate.now().plusDays(10))).containsExactlyInAnyOrder(offer2);
        assertThat(testee.getOffersForDate(LocalDate.now().plusDays(11))).isEmpty();
    }

    @Test
    void emptyListIfDateMissing() {
        assertThat(testee.getOffersForDate(null)).isEmpty();
    }
}