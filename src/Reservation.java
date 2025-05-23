import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Reservation {
    private int reservationId;
    private int roomNumber;
    private String guestName;
    private String phone;
    private String email;
    private Timestamp checkIn;
    private Timestamp checkOut;
    private String bedType;

    // Constructor, getters, setters omitted for brevity

    public static List<Reservation> getReservationsByPhone(String phone) {
        List<Reservation> bookings = new ArrayList<>();
        String query = "SELECT reservation_id, room_number, guest_name, phone, email, check_in, check_out, bed_type FROM reservations WHERE phone = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, phone);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Reservation res = new Reservation();
                res.reservationId = rs.getInt("reservation_id");
                res.roomNumber = rs.getInt("room_number");
                res.guestName = rs.getString("guest_name");
                res.phone = rs.getString("phone");
                res.email = rs.getString("email");
                res.checkIn = rs.getTimestamp("check_in");
                res.checkOut = rs.getTimestamp("check_out");
                res.bedType = rs.getString("bed_type");
                bookings.add(res);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
}
