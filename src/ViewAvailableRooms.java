import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewAvailableRooms extends JFrame {
    private JTable roomsTable;
    private DefaultTableModel tableModel;

    public ViewAvailableRooms() {
        setTitle("Available Rooms");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columns = {"Room Number", "Bed Type", "Price", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        roomsTable = new JTable(tableModel);

        add(new JScrollPane(roomsTable), BorderLayout.CENTER);

        loadAvailableRooms();
    }

    private void loadAvailableRooms() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT room_number, bed_type, price, status FROM rooms WHERE status = 'Available'")) {

            tableModel.setRowCount(0);  // Clear table

            while (rs.next()) {
                int roomNumber = rs.getInt("room_number");
                String bedType = rs.getString("bed_type");
                double price = rs.getDouble("price");
                String status = rs.getString("status");

                tableModel.addRow(new Object[]{roomNumber, bedType, price, status});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading rooms: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewAvailableRooms().setVisible(true));
    }
}
