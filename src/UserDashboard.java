import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UserDashboard extends JFrame {

    private String username;

    public UserDashboard(String username) {
        this.username = username;

        setTitle("User Dashboard - " + username);
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(welcomeLabel);

        JButton viewRoomsBtn = new JButton("View Available Rooms");
        JButton makeReservationBtn = new JButton("Make a Reservation");
        JButton viewBookingsBtn = new JButton("View My Bookings");
        JButton extendStayBtn = new JButton("Extend Stay");
        JButton logoutBtn = new JButton("Logout");

        // Add buttons to panel
        panel.add(viewRoomsBtn);
        panel.add(makeReservationBtn);
        panel.add(viewBookingsBtn);
        panel.add(extendStayBtn);
        panel.add(logoutBtn);

        // Add listeners
        viewRoomsBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Rooms listing coming soon!"));
        makeReservationBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Reservation form coming soon!"));
        viewBookingsBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Your bookings will be shown here."));
        extendStayBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Stay extension feature coming soon!"));
        logoutBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Logged out.");
            dispose(); // Close this window
            new LoginWindow().setVisible(true); // Go back to login
        });

        add(panel);
    }

    // Test the dashboard independently
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new UserDashboard("TestUser").setVisible(true);
        });
    }
}
