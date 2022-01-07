package in.co.sabailai.technician.models;

public class OngoingGetSet {

    String order_id, service_id, service_title, date, time_slot, contact_person, contact_no, apart_noplot_no, address, area, city, State, status,longitute,latitute;

    public OngoingGetSet(String order_id, String service_id, String service_title, String date, String time_slot, String contact_person, String contact_no, String apart_noplot_no, String address, String area, String city, String state, String status,String latitute,String longitute) {
        this.order_id = order_id;
        this.service_id = service_id;
        this.service_title = service_title;
        this.date = date;
        this.time_slot = time_slot;
        this.contact_person = contact_person;
        this.contact_no = contact_no;
        this.apart_noplot_no = apart_noplot_no;
        this.address = address;
        this.area = area;
        this.city = city;
        State = state;
        this.status = status;
        this.longitute = longitute;
        this.latitute = latitute;
    }


    public String getService_id() {
        return service_id;
    }

    public OngoingGetSet setService_id(String service_id) {
        this.service_id = service_id;
        return this;
    }

    public String getService_title() {
        return service_title;
    }

    public OngoingGetSet setService_title(String service_title) {
        this.service_title = service_title;
        return this;
    }

    public String getDate() {
        return date;
    }

    public OngoingGetSet setDate(String date) {
        this.date = date;
        return this;
    }

    public String getTime_slot() {
        return time_slot;
    }

    public OngoingGetSet setTime_slot(String time_slot) {
        this.time_slot = time_slot;
        return this;
    }

    public String getContact_person() {
        return contact_person;
    }

    public OngoingGetSet setContact_person(String contact_person) {
        this.contact_person = contact_person;
        return this;
    }

    public String getContact_no() {
        return contact_no;
    }

    public OngoingGetSet setContact_no(String contact_no) {
        this.contact_no = contact_no;
        return this;
    }

    public String getApart_noplot_no() {
        return apart_noplot_no;
    }

    public OngoingGetSet setApart_noplot_no(String apart_noplot_no) {
        this.apart_noplot_no = apart_noplot_no;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public OngoingGetSet setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getArea() {
        return area;
    }

    public OngoingGetSet setArea(String area) {
        this.area = area;
        return this;
    }

    public String getCity() {
        return city;
    }

    public OngoingGetSet setCity(String city) {
        this.city = city;
        return this;
    }

    public String getState() {
        return State;
    }

    public OngoingGetSet setState(String state) {
        State = state;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public OngoingGetSet setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getOrder_id() {
        return order_id;
    }

    public OngoingGetSet setOrder_id(String order_id) {
        this.order_id = order_id;
        return this;
    }

    public String getLongitute() {
        return longitute;
    }

    public OngoingGetSet setLongitute(String longitute) {
        this.longitute = longitute;
        return this;
    }

    public String getLatitute() {
        return latitute;
    }

    public OngoingGetSet setLatitute(String latitute) {
        this.latitute = latitute;
        return this;
    }
}
