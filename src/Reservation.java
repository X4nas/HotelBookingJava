public class Reservation {
    String guestName, phone, roomType, checkIn, checkOut;

    public Reservation(String guestName, String phone, String roomType, String checkIn, String checkOut) {
        this.guestName = guestName;
        this.phone = phone;
        this.roomType = roomType;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public String toString() {
        return "Guest: " + guestName + ", Phone: " + phone + ", Room Type: " + roomType + ", Check-In: " + checkIn + ", Check-Out: " + checkOut;
    }
}
