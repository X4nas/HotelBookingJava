import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {

    private String adminUsername;

    public AdminDashboard(String adminUsername) {
        this.adminUsername = adminUsername;

        setTitle("Admin Dashboard - " + adminUsername);
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel welcomeLabel = new JLabel("Welcome, Admin " + adminUsername + "!");
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(welcomeLabel);

        JButton viewReservationsBtn = new JButton("View All Reservations");
        JButton manageRoomsBtn = new JButton("Add / Edit / Delete Room Info");
        JButton manageUsersBtn = new JButton("Manage Users");
        JButton addReservationBtn = new JButton("Add Reservation");
        JButton logoutBtn = new JButton("Logout");

        // Add buttons to panel
        panel.add(viewReservationsBtn);
        panel.add(manageRoomsBtn);
        panel.add(manageUsersBtn);
        panel.add(addReservationBtn);
        panel.add(logoutBtn);

        // Add listeners
        viewReservationsBtn.addActionListener(e -> new ViewReservationsWindow(MakeReservationForm.getReservations()).setVisible(true));
        manageRoomsBtn.addActionListener(e -> new ManageRoomsWindow().setVisible(true));
        manageUsersBtn.addActionListener(e -> new ManageUsersWindow().setVisible(true));
        addReservationBtn.addActionListener(e -> new MakeReservationForm("Admin").setVisible(true));
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
