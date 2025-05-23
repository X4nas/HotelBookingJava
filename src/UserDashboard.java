import javax.swing.*;
import java.awt.*;

public class UserDashboard extends JFrame {

    private String username;

    public UserDashboard(String username) {
        this.username = username;

        setTitle("User Dashboard - " + username);
        setSize(400, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 1, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton viewRoomsBtn = new JButton("View Available Rooms");
        JButton makeReservationBtn = new JButton("Make a Reservation");
        JButton viewBookingsBtn = new JButton("View My Bookings");
        JButton logoutBtn = new JButton("Logout");

        // Add listeners
        viewRoomsBtn.addActionListener(e -> new ViewAvailableRooms().setVisible(true));
        makeReservationBtn.addActionListener(e -> new MakeReservationForm(username).setVisible(true));
        viewBookingsBtn.addActionListener(e -> new ViewBookingsWindow(username).setVisible(true));
        logoutBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Logged out.");
            dispose();
            new LoginWindow().setVisible(true);
        });

        // Add components
        panel.add(welcomeLabel);
        panel.add(viewRoomsBtn);
        panel.add(makeReservationBtn);
        panel.add(viewBookingsBtn);
        panel.add(logoutBtn);

        add(panel);
    }

    // Test the dashboard independently
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserDashboard("TestUser").setVisible(true));
    }
}
