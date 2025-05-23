import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminDashboard extends JFrame {

    private String adminUsername;

    public AdminDashboard(String adminUsername) {
        this.adminUsername = adminUsername;

        setTitle("Admin Dashboard - " + adminUsername);
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(7, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel welcomeLabel = new JLabel("Welcome, Admin " + adminUsername + "!");
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(welcomeLabel);

        JButton viewReservationsBtn = new JButton("View All Reservations");
        JButton manageRoomsBtn = new JButton("Add / Edit / Delete Room Info");
        JButton manageUsersBtn = new JButton("Manage Users");
        JButton addBookingBtn = new JButton("Add Booking");
        JButton addReservationBtn = new JButton("Add Reservation");
        JButton logoutBtn = new JButton("Logout");

        // Add buttons to panel
        panel.add(viewReservationsBtn);
        panel.add(manageRoomsBtn);
        panel.add(manageUsersBtn);
        panel.add(addBookingBtn);
        panel.add(addReservationBtn);
        panel.add(logoutBtn);

        // Add listeners
        viewReservationsBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Reservation list feature coming soon."));
        manageRoomsBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Room management window coming soon."));
        manageUsersBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "User management interface coming soon."));
        addBookingBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Manual booking form coming soon."));
        addReservationBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Admin reservation form coming soon."));
        logoutBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Logged out.");
            dispose(); // Close this window
            new LoginWindow().setVisible(true); // Return to login
        });

        add(panel);
    }

    // Test independently
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AdminDashboard("admin").setVisible(true);
        });
    }
}
