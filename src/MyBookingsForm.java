import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MyBookingsForm extends JFrame {
    public MyBookingsForm(String userPhone) {
        setTitle("My Bookings");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnNames = {"Reservation ID", "Room Number", "Guest Name", "Phone", "Check-In", "Check-Out"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT reservation_id, room_number, guest_name, phone, check_in, check_out FROM reservations WHERE phone = ?"
             )) {

            stmt.setString(1, userPhone);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("reservation_id");
                int room = rs.getInt("room_number");
                String name = rs.getString("guest_name");
                String phone = rs.getString("phone");
                Timestamp checkIn = rs.getTimestamp("check_in");
                Timestamp checkOut = rs.getTimestamp("check_out");

                model.addRow(new Object[]{id, room, name, phone, checkIn, checkOut});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading bookings: " + e.getMessage());
        }

        setVisible(true);
    }
}
