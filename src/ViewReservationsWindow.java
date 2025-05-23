import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewReservationsWindow extends JFrame {
    private DefaultTableModel tableModel;
    private JTable reservationsTable;

    public ViewReservationsWindow() {
        setTitle("All Reservations");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columns = {"Reservation ID", "Room No", "Guest Name", "Phone", "Check-In", "Check-Out"};
        tableModel = new DefaultTableModel(columns, 0);
        reservationsTable = new JTable(tableModel);

        add(new JScrollPane(reservationsTable), BorderLayout.CENTER);

        loadReservationsFromDB();
    }

    private void loadReservationsFromDB() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT reservation_id, room_number, guest_name, phone, check_in_date, check_out_date FROM reservations")) {

            tableModel.setRowCount(0);

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("reservation_id"),
                        rs.getInt("room_number"),
                        rs.getString("guest_name"),
                        rs.getString("phone"),
                        rs.getDate("check_in_date").toString(),
                        rs.getDate("check_out_date").toString()
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading reservations from database.");
        }
    }
}
