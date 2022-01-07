package in.co.sabailai.technician.models;

public class VendorNotiGetSet {

    String booking_id, message, date;

    public VendorNotiGetSet(String booking_id, String message, String date) {
        this.booking_id = booking_id;
        this.message = message;
        this.date = date;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public VendorNotiGetSet setBooking_id(String booking_id) {
        this.booking_id = booking_id;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public VendorNotiGetSet setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getDate() {
        return date;
    }

    public VendorNotiGetSet setDate(String date) {
        this.date = date;
        return this;
    }
}
