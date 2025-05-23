import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.jdatepicker.impl.*;

public class MakeReservationForm extends JFrame {

    private JTextField guestNameField, phoneField;
    private JComboBox<String> roomTypeBox;
    private JButton saveButton;
    private static List<Reservation> reservations = new ArrayList<>();
    private JDatePickerImpl checkInPicker, checkOutPicker;

    public MakeReservationForm(String userRole) {
        setTitle(userRole + " - Make Reservation");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        panel.add(new JLabel("Guest Name:"));
        guestNameField = new JTextField();
        panel.add(guestNameField);

        panel.add(new JLabel("Phone (10 digits):"));
        phoneField = new JTextField();
        panel.add(phoneField);

        panel.add(new JLabel("Room Type:"));
        roomTypeBox = new JComboBox<>(new String[]{"Single", "Double", "Deluxe"});
        panel.add(roomTypeBox);

        panel.add(new JLabel("Check-in Date:"));
        checkInPicker = createDatePicker();
        panel.add(checkInPicker);

        panel.add(new JLabel("Check-out Date:"));
        checkOutPicker = createDatePicker();
        panel.add(checkOutPicker);

        saveButton = new JButton("Save Reservation");
        panel.add(saveButton);
        panel.add(new JLabel("")); // Empty cell

        saveButton.addActionListener(e -> saveReservation());

        add(panel);
    }

    public static java.util.List<Reservation> loadReservationsFromDB() {
        java.util.List<Reservation> reservations = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT guest_name, phone, room_type, check_in, check_out FROM reservations";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                reservations.add(new Reservation(
                        rs.getString("guest_name"),
                        rs.getString("phone"),
                        rs.getString("room_type"),
                        rs.getDate("check_in").toString(),
                        rs.getDate("check_out").toString()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reservations;
    }


    private void saveReservation() {
        String guest = guestNameField.getText();
        String phone = phoneField.getText();
        String roomType = (String) roomTypeBox.getSelectedItem();
        String checkIn = checkInPicker.getJFormattedTextField().getText();
        String checkOut = checkOutPicker.getJFormattedTextField().getText();

        if (guest.isEmpty() || phone.isEmpty() || checkIn.isEmpty() || checkOut.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        if (!phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Phone number must be 10 digits.");
            return;
        }

        // Save to DB
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO reservations (guest_name, phone, room_type, check_in, check_out) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, guest);
            ps.setString(2, phone);
            ps.setString(3, roomType);
            ps.setDate(4, java.sql.Date.valueOf(checkIn));
            ps.setDate(5, java.sql.Date.valueOf(checkOut));
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Reservation saved successfully!");

            // Clear fields
            guestNameField.setText("");
            phoneField.setText("");
            checkInPicker.getJFormattedTextField().setText("");
            checkOutPicker.getJFormattedTextField().setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving reservation: " + ex.getMessage());
        }
    }


    private JDatePickerImpl createDatePicker() {
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        return new JDatePickerImpl(datePanel, new DateLabelFormatter());
    }

    public static List<Reservation> getReservations() {
        return reservations;
    }
}
