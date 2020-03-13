import model.Buy2GetHalfOffOffer;
import model.Items;
import model.Offer;
import model.PercentageDiscountOffer;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OfferScheduler {

    private static final Buy2GetHalfOffOffer buy2SoupGetHalfPriceBreadOffer = Buy2GetHalfOffOffer.forItems(Items.SOUP, Items.BREAD);
    private static final PercentageDiscountOffer tenPercentOffApplesOffer = PercentageDiscountOffer.buildOffer(Items.APPLES, 10);

    private Map<OfferDuration, Offer> offerDurations;

    public OfferScheduler(Map<OfferDuration, Offer> offerDurations) {
        this.offerDurations = offerDurations;
    }

    public static OfferScheduler empty() {
        return new OfferScheduler(Map.of());
    }

    public static OfferScheduler liveOffers() {
        return new OfferScheduler(Map.of(OfferDuration.between(LocalDate.now().minusDays(1), LocalDate.now().plusDays(7)),
                buy2SoupGetHalfPriceBreadOffer, OfferDuration.between(LocalDate.now().plusDays(3), LocalDate.now().plusMonths(1).with(TemporalAdjusters.lastDayOfMonth())),
                tenPercentOffApplesOffer));
    }

    public static OfferScheduler forOffers(Offer... offers) {
        return new OfferScheduler(Arrays.stream(offers)
                .collect(Collectors.toMap(o -> OfferDuration.between(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1)),
                        o -> o)));
    }

    //assume that start and end dates are intended to be inclusive for the offer validity
    public Collection<Offer> getOffersForDate(LocalDate date) {
        return date == null ? List.of() : offerDurations.entrySet().stream()
                .filter(e -> date.isAfter(e.getKey().start.minusDays(1)) &&
                        date.isBefore(e.getKey().end.plusDays(1)))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public static class OfferDuration {

        private final LocalDate start;
        private final LocalDate end;

        public OfferDuration(LocalDate start, LocalDate end) {

            this.start = start;
            this.end = end;
        }

        public static OfferDuration between(LocalDate start, LocalDate end) {
            return new OfferDuration(start, end);
        }

        @Override
        public String toString() {
            return "OfferDuration{" +
                    "start=" + start +
                    ", end=" + end +
                    '}';
        }
    }
}
