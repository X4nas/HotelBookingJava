import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ManageRoomsWindow extends JFrame {
    private JTable roomsTable;
    private DefaultTableModel tableModel;

    public ManageRoomsWindow() {
        setTitle("Manage Rooms");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columnNames = {"Room No", "Type", "Price", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        roomsTable = new JTable(tableModel);

        JPanel panel = new JPanel();
        JButton addBtn = new JButton("Add Room");
        JButton deleteBtn = new JButton("Delete Selected");
        panel.add(addBtn);
        panel.add(deleteBtn);

        // Add room to DB and table
        addBtn.addActionListener(e -> {
            String roomNo = JOptionPane.showInputDialog(this, "Room Number:");
            String type = JOptionPane.showInputDialog(this, "Room Type:");
            String priceStr = JOptionPane.showInputDialog(this, "Room Price:");
            String status = JOptionPane.showInputDialog(this, "Room Status (Available/Occupied):");

            if (roomNo != null && type != null && priceStr != null && status != null &&
                    !roomNo.isEmpty() && !type.isEmpty() && !priceStr.isEmpty() && !status.isEmpty()) {
                try (Connection conn = DatabaseConnection.getConnection()) {
                    String query = "INSERT INTO rooms (room_number, bed_type, price, status) VALUES (?, ?, ?, ?)";
                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setInt(1, Integer.parseInt(roomNo));
                    pstmt.setString(2, type);
                    pstmt.setDouble(3, Double.parseDouble(priceStr));
                    pstmt.setString(4, status);
                    pstmt.executeUpdate();
                    tableModel.addRow(new Object[]{roomNo, type, priceStr, status});
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error adding room to database.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "All fields are required to add a room.");
            }
        });

        // Delete from table and DB
        deleteBtn.addActionListener(e -> {
            int selected = roomsTable.getSelectedRow();
            if (selected != -1) {
                int confirm = JOptionPane.showConfirmDialog(this, "Delete selected room from database?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    int roomNumber = Integer.parseInt(roomsTable.getValueAt(selected, 0).toString());
                    try (Connection conn = DatabaseConnection.getConnection()) {
                        String query = "DELETE FROM rooms WHERE room_number = ?";
                        PreparedStatement pstmt = conn.prepareStatement(query);
                        pstmt.setInt(1, roomNumber);
                        pstmt.executeUpdate();
                        tableModel.removeRow(selected);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error deleting room from database.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a room to delete.");
            }
        });

        add(new JScrollPane(roomsTable), BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        loadRoomsFromDB(); // Load data on startup
    }

    private void loadRoomsFromDB() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM rooms")) {

            tableModel.setRowCount(0); // Clear table

            while (rs.next()) {
                int roomNumber = rs.getInt("room_number");
                String bedType = rs.getString("bed_type");
                double price = rs.getDouble("price");
                String status = rs.getString("status");

                tableModel.addRow(new Object[]{roomNumber, bedType, price, status});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading rooms from database.");
        }
    }
}
