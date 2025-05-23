import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewBookingsWindow extends JFrame {
    private String username;
    private JTable bookingsTable;
    private DefaultTableModel tableModel;

    public ViewBookingsWindow(String username) {
        this.username = username;
        setTitle("My Bookings - " + username);
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(new JLabel("User bookings will be displayed here.", SwingConstants.CENTER));

        String[] columns = {"Reservation ID", "Room Number", "Guest Name", "Phone", "Check-In", "Check-Out"};
        tableModel = new DefaultTableModel(columns, 0);
        bookingsTable = new JTable(tableModel);

        add(new JScrollPane(bookingsTable), BorderLayout.CENTER);

        loadUserBookings();
    }

    private void loadUserBookings() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT reservation_id, room_number, guest_name, phone_number, check_in_date, check_out_date " +
                             "FROM reservations WHERE guest_name = ? OR phone_number = ?")) {

            ps.setString(1, username);
            ps.setString(2, username);

            try (ResultSet rs = ps.executeQuery()) {
                tableModel.setRowCount(0); // clear previous

                while (rs.next()) {
                    int resId = rs.getInt("reservation_id");
                    int roomNum = rs.getInt("room_number");
                    String guestName = rs.getString("guest_name");
                    String phone = rs.getString("phone_number");
                    Date checkIn = rs.getDate("check_in_date");
                    Date checkOut = rs.getDate("check_out_date");

                    tableModel.addRow(new Object[]{resId, roomNum, guestName, phone, checkIn, checkOut});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading bookings: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewBookingsWindow("TestUser").setVisible(true));
    }
}
