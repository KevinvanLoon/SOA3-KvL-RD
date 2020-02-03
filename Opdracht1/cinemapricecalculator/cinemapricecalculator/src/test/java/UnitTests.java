import domain.Movie;
import domain.MovieScreening;
import domain.MovieTicket;
import domain.Order;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Test;

public class UnitTests {

    private static final int PRICE_PER_SEAT = 5;
    private static final double DISCOUNT = 0.9;

    //Discount tests
    @Test
    public void aOrderGetsTenPercentDiscountWhenOrderConsistsOfSixTickets() {
        Order order = generateStudentOrder();
        addMultipleTicketsToOrder(order, 6);

        double price = order.calculatePrice();

        Assert.assertEquals(13.5,price, 0);
    }

    //Premium ticket tests
    @Test
    public void aStudentPaysTwoEurosExtraForAPremiumTicket() {
        Order order = generateStudentOrder();
        order.addSeatReservation(generatePremiumTicket());

        double price = order.calculatePrice();

        Assert.assertEquals(PRICE_PER_SEAT + 2, price, 0);
    }

    @Test
    public void aNonStudentPaysThreeEurosExtraForAPremiumTicket() {
        Order order = generateNonStudentOrder();
        order.addSeatReservation(generatePremiumTicket());

        double price = order.calculatePrice();

        Assert.assertEquals(PRICE_PER_SEAT + 3, price, 0);
    }

    //Free second ticket tests


    private Order generateStudentOrder() {
        return new Order(1, true);
    }

    private Order generateNonStudentOrder() {
        return new Order(1, false);
    }

    private Movie generateMovie() {
        return new Movie("Star Wars");
    }

    private MovieScreening generateMovieScreening() {
        return new MovieScreening(generateMovie(), LocalDateTime.now(), PRICE_PER_SEAT);
    }

    private MovieTicket generatePremiumTicket() {
        return new MovieTicket(generateMovieScreening(), true, 1, 1);
    }

    private MovieTicket generateTicket() {
        return new MovieTicket(generateMovieScreening(), false, 1, 1);
    }

    private void addMultipleTicketsToOrder(Order order, int amount){
        for (int i = 0; i < amount; i++){
            order.addSeatReservation(generateTicket());
        }
    }

    private double calculateDiscount(Order order){
        System.out.println(order.getTickets().size()*PRICE_PER_SEAT*DISCOUNT);
        return (order.getTickets().size() * PRICE_PER_SEAT * DISCOUNT);
    }

}
