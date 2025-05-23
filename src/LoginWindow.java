import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
        add(new JLabel("Login form here.", SwingConstants.CENTER)); // Center the window

        // Panel setup
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // User type
        userTypeCombo = new JComboBox<>(new String[]{"User", "Admin"});
        panel.add(new JLabel("Select Login Type:"));
        panel.add(userTypeCombo);

        // Username field
        usernameField = new JTextField();
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);

        // Password field
        passwordField = new JPasswordField();
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        loginButton = new JButton("Login");
        forgotPasswordButton = new JButton("Forgot Password?");
        buttonPanel.add(loginButton);
        buttonPanel.add(forgotPasswordButton);

        // Add listeners
        loginButton.addActionListener(e -> handleLogin());
        forgotPasswordButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Password recovery feature coming soon!"));

        // Final layout
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String userType = (String) userTypeCombo.getSelectedItem();

        // Stub for actual validation
        // proceed to admin dashboard
        if (username.equals("admin") && password.equals("admin") && userType.equals("Admin")) {
            JOptionPane.showMessageDialog(this, "Admin Login Successful!");
            this.dispose(); // close login window
            new AdminDashboard(username).setVisible(true);
        }
        // proceed to the user dashboard
        if (username.equals("user") && password.equals("user") && userType.equals("User")) {
            JOptionPane.showMessageDialog(this, "User Login Successful!");
            this.dispose(); // close login window
            new UserDashboard(username).setVisible(true);
        }
        else {
            JOptionPane.showMessageDialog(this, "Invalid Credentials!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginWindow().setVisible(true);
        });
    }
}
