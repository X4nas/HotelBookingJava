import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewBookingsWindow extends JFrame {
    private String userIdentifier;  // phone or guest_name
    private JTable bookingsTable;
    private DefaultTableModel tableModel;

    public ViewBookingsWindow(String userIdentifier) {
        this.userIdentifier = userIdentifier;
        setTitle("My Bookings - " + userIdentifier);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] columns = {"Reservation ID", "Room Number", "Guest Name", "Phone", "Check-In", "Check-Out"};
        tableModel = new DefaultTableModel(columns, 0);
        bookingsTable = new JTable(tableModel);
        bookingsTable.setFillsViewportHeight(true);

        add(new JScrollPane(bookingsTable), BorderLayout.CENTER);

        loadUserBookings();
    }

    private void loadUserBookings() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT reservation_id, room_number, guest_name, phone, check_in, check_out " +
                             "FROM reservations WHERE guest_name = ? OR phone = ?")) {

            ps.setString(1, userIdentifier);
            ps.setString(2, userIdentifier);

            try (ResultSet rs = ps.executeQuery()) {
                tableModel.setRowCount(0); // clear previous

                while (rs.next()) {
                    int resId = rs.getInt("reservation_id");
                    int roomNum = rs.getInt("room_number");
                    String guestName = rs.getString("guest_name");
                    String phone = rs.getString("phone");
                    Timestamp checkIn = rs.getTimestamp("check_in");
                    Timestamp checkOut = rs.getTimestamp("check_out");

                    tableModel.addRow(new Object[]{resId, roomNum, guestName, phone, checkIn, checkOut});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading bookings: " + e.getMessage());
        }
    }

    // For quick test
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewBookingsWindow("1234567890").setVisible(true));
    }
}
