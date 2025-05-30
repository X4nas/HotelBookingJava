import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton forgotPasswordButton;
    private JComboBox<String> userTypeCombo;

    public LoginWindow() {
        setTitle("Login");
        setSize(350, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        userTypeCombo = new JComboBox<>(new String[]{"User", "Admin"});
        panel.add(new JLabel("Select Login Type:"));
        panel.add(userTypeCombo);

        usernameField = new JTextField();
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);

        passwordField = new JPasswordField();
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        loginButton = new JButton("Login");
        forgotPasswordButton = new JButton("Forgot Password? ");
        buttonPanel.add(loginButton);
        buttonPanel.add(forgotPasswordButton);

        loginButton.addActionListener(e -> handleLogin());
        forgotPasswordButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Password recovery feature coming soon!"));

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) userTypeCombo.getSelectedItem();  // "Admin" or "User"

        String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND role = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, role + " Login Successful!");
                    this.dispose();

                    if ("Admin".equalsIgnoreCase(role)) {
                        new AdminDashboard(username).setVisible(true);
                    } else {
                        new UserDashboard(username).setVisible(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Credentials!");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginWindow().setVisible(true));
    }
}
