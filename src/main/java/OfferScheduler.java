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

    private static final OfferDuration TWO_SOUP_HALF_PRICE_BREAD_DURATION = OfferDuration.between(LocalDate.now().minusDays(1), LocalDate.now().plusDays(7));
    private static final OfferDuration TEN_PERCENT_OFF_APPLES_DURATION = OfferDuration.between(LocalDate.now().plusDays(3), LocalDate.now().plusMonths(1).with(TemporalAdjusters.lastDayOfMonth()));
    private static final Buy2GetHalfOffOffer BUY_2_SOUP_GET_HALF_PRICE_BREAD_OFFER = Buy2GetHalfOffOffer.forItems(Items.SOUP, Items.BREAD);
    private static final PercentageDiscountOffer TEN_PERCENT_OFF_APPLES_OFFER = PercentageDiscountOffer.buildOffer(Items.APPLES, 10);

    private final Map<OfferDuration, Offer> offerDurations;

    private OfferScheduler(Map<OfferDuration, Offer> offerDurations) {
        this.offerDurations = offerDurations;
    }

    public static OfferScheduler forOfferDurations(Map<OfferDuration, Offer> offerDurations) {
        return new OfferScheduler(offerDurations);
    }

    public static OfferScheduler empty() {
        return new OfferScheduler(Map.of());
    }

    public static OfferScheduler liveOffers() {
        return new OfferScheduler(Map.of(TWO_SOUP_HALF_PRICE_BREAD_DURATION, BUY_2_SOUP_GET_HALF_PRICE_BREAD_OFFER,
                TEN_PERCENT_OFF_APPLES_DURATION, TEN_PERCENT_OFF_APPLES_OFFER));
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
