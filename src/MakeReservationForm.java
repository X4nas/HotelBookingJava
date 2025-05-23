import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class Reservation {
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

public class MakeReservationForm extends JFrame {

    private JTextField guestNameField, phoneField, checkInField, checkOutField;
    private JComboBox<String> roomTypeBox;
    private JButton saveButton;
    private static java.util.List<Reservation> reservations = new ArrayList<>();

    public MakeReservationForm(String userRole) {
        setTitle(userRole + " - Make Reservation");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        panel.add(new JLabel("Guest Name:"));
        guestNameField = new JTextField();
        panel.add(guestNameField);

        panel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        panel.add(phoneField);

        panel.add(new JLabel("Room Type:"));
        roomTypeBox = new JComboBox<>(new String[]{"Single", "Double", "Deluxe"});
        panel.add(roomTypeBox);

        panel.add(new JLabel("Check-in Date (yyyy-mm-dd):"));
        checkInField = new JTextField();
        panel.add(checkInField);

        panel.add(new JLabel("Check-out Date (yyyy-mm-dd):"));
        checkOutField = new JTextField();
        panel.add(checkOutField);

        saveButton = new JButton("Save Reservation");
        panel.add(saveButton);

        // Empty cell for layout
        panel.add(new JLabel(""));

        saveButton.addActionListener(e -> saveReservation());

        add(panel);
    }

    private void saveReservation() {
        String guest = guestNameField.getText();
        String phone = phoneField.getText();
        String roomType = (String) roomTypeBox.getSelectedItem();
        String checkIn = checkInField.getText();
        String checkOut = checkOutField.getText();

        if (guest.isEmpty() || phone.isEmpty() || checkIn.isEmpty() || checkOut.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        Reservation res = new Reservation(guest, phone, roomType, checkIn, checkOut);
        reservations.add(res);

        JOptionPane.showMessageDialog(this, "Reservation saved successfully!");

        // Reset fields
        guestNameField.setText("");
        phoneField.setText("");
        checkInField.setText("");
        checkOutField.setText("");
    }

    // Optional: test this form directly
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MakeReservationForm("Admin").setVisible(true));
    }
}
