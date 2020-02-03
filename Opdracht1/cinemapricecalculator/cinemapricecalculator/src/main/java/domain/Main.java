package domain;

import java.time.LocalDateTime;

public class Main {
    public static void main(String [] args) {
        LocalDateTime date = LocalDateTime.now();
        System.out.println(date.getDayOfWeek().getValue());

        Order order = new Order(1,false);
        Movie movie = new Movie("Star Wars");
        MovieScreening movieScreening = new MovieScreening(movie,LocalDateTime.now(), 3.0);
        MovieTicket movieTicket = new MovieTicket(movieScreening, true, 1, 1);
        MovieTicket movieTicket2 = new MovieTicket(movieScreening, true, 2, 1);

        order.addSeatReservation(movieTicket);
        order.addSeatReservation(movieTicket2);

        order.export(TicketExportFormat.JSON);
    }
}
