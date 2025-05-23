import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class MakeReservationForm extends JFrame {
    private JComboBox<Integer> roomComboBox;
    private JTextField nameField, phoneField;
    private JTextField checkInField, checkOutField;  // Format: yyyy-MM-dd

    public MakeReservationForm() {
        setTitle("Make a Reservation");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        panel.add(new JLabel("Select Room Number:"));
        roomComboBox = new JComboBox<>();
        loadAvailableRooms();
        panel.add(roomComboBox);

        panel.add(new JLabel("Your Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Phone Number:"));
        phoneField = new JTextField();
        panel.add(phoneField);

        panel.add(new JLabel("Check-In Date (yyyy-MM-dd):"));
        checkInField = new JTextField();
        panel.add(checkInField);

        panel.add(new JLabel("Check-Out Date (yyyy-MM-dd):"));
        checkOutField = new JTextField();
        panel.add(checkOutField);

        JButton reserveBtn = new JButton("Reserve");
        reserveBtn.addActionListener(e -> makeReservation());
        panel.add(reserveBtn);

        add(panel);
    }

    private void loadAvailableRooms() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT room_number FROM rooms WHERE status = 'Available'")) {

            roomComboBox.removeAllItems();
            while (rs.next()) {
                roomComboBox.addItem(rs.getInt("room_number"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading available rooms: " + e.getMessage());
        }
    }

    private void makeReservation() {
        if (roomComboBox.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No rooms available for reservation.");
            return;
        }

        int roomNumber = (Integer) roomComboBox.getSelectedItem();
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String checkIn = checkInField.getText().trim();
        String checkOut = checkOutField.getText().trim();

        // Validate inputs
        if (name.isEmpty() || phone.isEmpty() || checkIn.isEmpty() || checkOut.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        try {
            LocalDate checkInDate = LocalDate.parse(checkIn);
            LocalDate checkOutDate = LocalDate.parse(checkOut);

            if (checkOutDate.isBefore(checkInDate)) {
                JOptionPane.showMessageDialog(this, "Check-Out date must be after Check-In date.");
                return;
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Please enter dates in yyyy-MM-dd format.");
            return;
        }

        // Insert reservation and update room status
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            // Insert into reservations
            String insertSQL = "INSERT INTO reservations (room_number, guest_name, phone_number, check_in_date, check_out_date) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
                ps.setInt(1, roomNumber);
                ps.setString(2, name);
                ps.setString(3, phone);
                ps.setDate(4, Date.valueOf(checkIn));
                ps.setDate(5, Date.valueOf(checkOut));
                ps.executeUpdate();
            }

            // Update room status
            String updateSQL = "UPDATE rooms SET status = 'Occupied' WHERE room_number = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateSQL)) {
                ps.setInt(1, roomNumber);
                ps.executeUpdate();
            }

            conn.commit();
            JOptionPane.showMessageDialog(this, "Reservation successful!");
            dispose();  // Close form

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error making reservation: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MakeReservationForm().setVisible(true));
    }
}
