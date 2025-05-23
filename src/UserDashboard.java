import javax.swing.*;
import java.awt.*;
import java.util.List;  // Import java.util.List here

public class UserDashboard extends JFrame {

    private String userPhone; // Use phone for bookings lookup

    public UserDashboard(String userPhone) {
        this.userPhone = userPhone;

        setTitle("User Dashboard - " + userPhone);
        setSize(400, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 1, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel welcomeLabel = new JLabel("Welcome, " + userPhone + "!");
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton viewRoomsBtn = new JButton("View Available Rooms");
        JButton makeReservationBtn = new JButton("Make a Reservation");
        JButton viewBookingsBtn = new JButton("View My Bookings");
        JButton logoutBtn = new JButton("Logout");

        // Add listeners
        viewRoomsBtn.addActionListener(e -> new ViewAvailableRooms().setVisible(true));
        makeReservationBtn.addActionListener(e -> new MakeReservationForm(userPhone).setVisible(true));
        logoutBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Logged out.");
            dispose();
            new LoginWindow().setVisible(true);
        });

        // Single listener for viewBookingsBtn
        viewBookingsBtn.addActionListener(e -> {
            List<Reservation> bookings = Reservation.getReservationsByPhone(userPhone);
            if (bookings == null || bookings.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No bookings found for phone: " + userPhone);
            } else {
                // If your ViewBookingsWindow only accepts phone String, just:
                new ViewBookingsWindow(userPhone).setVisible(true);

                // OR if you want to pass bookings directly,
                // you must create a constructor that accepts List<Reservation> in ViewBookingsWindow
                // new ViewBookingsWindow(bookings).setVisible(true);
            }
        });

        // Add components
        panel.add(welcomeLabel);
        panel.add(viewRoomsBtn);
        panel.add(makeReservationBtn);
        panel.add(viewBookingsBtn);
        panel.add(logoutBtn);

        add(panel);
    }

    // For testing independently
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserDashboard("1234567890").setVisible(true));  // example phone number
    }
}
