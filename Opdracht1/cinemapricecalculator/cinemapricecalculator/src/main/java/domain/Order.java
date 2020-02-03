package domain;

import org.json.JSONArray;
import org.json.JSONObject;
import sun.security.krb5.internal.Ticket;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

public class Order {
    private int orderNr;
    private boolean isStudentOrder;

    private ArrayList<MovieTicket> tickets;

    public Order(int orderNr, boolean isStudentOrder) {
        this.orderNr = orderNr;
        this.isStudentOrder = isStudentOrder;

        tickets = new ArrayList<MovieTicket>();
    }

    public int getOrderNr() {
        return orderNr;
    }

    public ArrayList<MovieTicket> getTickets() {
        return tickets;
    }

    public void addSeatReservation(MovieTicket ticket) {
        tickets.add(ticket);
    }

    public double calculatePrice() {
        boolean discountGroup = (this.tickets.size() >= 6);
        double price = 0;
        int counter = 0;

        for (MovieTicket mt : this.tickets) {
            boolean isWeekend = this.isWeekend(mt.getMovieScreening().getDateAndTime());
            counter++;
            if (counter == 2 && (this.isStudentOrder || !isWeekend)) {
                counter = 0;
                continue;
            }
            price += calculatePrice(mt.getPrice(), mt.isPremiumTicket(), discountGroup, this.isStudentOrder);
        }
        return price;
    }

    public void export(TicketExportFormat exportFormat) {
        String fileName = this.generateFileName(this.orderNr, exportFormat);
        if (exportFormat == TicketExportFormat.JSON) {
            JSONArray array = this.transformTicketsToJSONArray(this.tickets);
            writeToJsonFile(array, fileName);
        }
        if (exportFormat == TicketExportFormat.PLAINTEXT) {
            ArrayList<String> array = this.transformTicketsToStringList(this.tickets);
            writeToTextFile(array,fileName);
        }
    }

    private String generateFileName(int orderNr, TicketExportFormat format) {
        String fileType = TicketExportFormat.JSON == format ? ".json" : ".txt";
        return "Order_" + Integer.toString(orderNr) + fileType;
    }

    private JSONArray transformTicketsToJSONArray(ArrayList<MovieTicket> tickets) {
        JSONArray jsonArray = new JSONArray();
        for (MovieTicket t : tickets) {
            JSONObject jsonObject = new JSONObject(t);
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    private ArrayList<String> transformTicketsToStringList(ArrayList<MovieTicket> tickets) {
        ArrayList<String> lines = new ArrayList<>();
        for (MovieTicket t :
                this.tickets) {
            lines.add((t.toString()));
        }
        return lines;
    }

    private boolean isWeekend(LocalDateTime date) {
        return (date.getDayOfWeek().getValue() >= 5);
    }

    private double calculatePrice(double price, boolean isPremium, boolean discount, boolean isStudent) {
        if (isPremium && isStudent) {
            price = this.calculatePremiumTicketForStudents(price);
        } else if (isPremium) {
            price = this.calculatePremiumTicket(price);
        }

        if (discount) {
            price = (price * 0.9);
        }

        return price;
    }

    private double calculatePremiumTicketForStudents(double price) {
        return (price + 2);
    }

    private double calculatePremiumTicket(double price) {
        return (price + 3);
    }

    private void writeToJsonFile(JSONArray array, String fileName){
        try {
            Path file = Paths.get(fileName);
            Files.write(file, Collections.singleton(array.toString()), StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void writeToTextFile(ArrayList<String> array, String fileName){
        try {
            Path file = Paths.get(fileName);
            Files.write(file, array, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
