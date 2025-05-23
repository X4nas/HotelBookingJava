import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ManageUsersWindow extends JFrame {

    private JTable usersTable;
    private DefaultTableModel tableModel;

    public ManageUsersWindow() {
        setTitle("Manage Users");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        tableModel = new DefaultTableModel(new Object[]{"Username", "Role"}, 0);
        usersTable = new JTable(tableModel);
        loadUsers();

        JScrollPane scrollPane = new JScrollPane(usersTable);

        JButton addUserBtn = new JButton("Add User");
        JButton editUserBtn = new JButton("Edit User");
        JButton deleteUserBtn = new JButton("Delete User");

        JPanel btnPanel = new JPanel();
        btnPanel.add(addUserBtn);
        btnPanel.add(editUserBtn);
        btnPanel.add(deleteUserBtn);

        add(scrollPane, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        addUserBtn.addActionListener(e -> openUserForm(null));
        editUserBtn.addActionListener(e -> {
            int selectedRow = usersTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user to edit.");
                return;
            }
            String username = (String) tableModel.getValueAt(selectedRow, 0);
            openUserForm(username); // pass existing username
        });

        deleteUserBtn.addActionListener(e -> {
            int selectedRow = usersTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user to delete.");
                return;
            }
            String username = (String) tableModel.getValueAt(selectedRow, 0);

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete user: " + username + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                deleteUser(username);
                loadUsers();
            }
        });
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost/hotel_booking", "root", "1234");
    }

    private void loadUsers() {
        tableModel.setRowCount(0);
        try (Connection con = getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT username, role FROM users")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getString("username"), rs.getString("role")});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading users: " + e.getMessage());
        }
    }

    private void openUserForm(String existingUsername) {
        JDialog dialog = new JDialog(this, existingUsername == null ? "Add User" : "Edit User", true);
        dialog.setSize(300, 250);
        dialog.setLayout(new GridLayout(5, 2, 10, 10));
        dialog.setLocationRelativeTo(this);

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"Admin", "User"});

        if (existingUsername != null) {
            // Load data from DB
            try (Connection con = getConnection();
                 PreparedStatement pst = con.prepareStatement("SELECT * FROM users WHERE username = ?")) {
                pst.setString(1, existingUsername);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    usernameField.setText(rs.getString("username"));
                    passwordField.setText(rs.getString("password"));
                    roleBox.setSelectedItem(rs.getString("role"));
                    usernameField.setEditable(false); // lock username
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error loading user: " + ex.getMessage());
                return;
            }
        }

        dialog.add(new JLabel("Username:"));
        dialog.add(usernameField);
        dialog.add(new JLabel("Password:"));
        dialog.add(passwordField);
        dialog.add(new JLabel("Role:"));
        dialog.add(roleBox);

        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        dialog.add(saveBtn);
        dialog.add(cancelBtn);

        saveBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String role = (String) roleBox.getSelectedItem();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Username and password cannot be empty.");
                return;
            }

            try (Connection con = getConnection()) {
                if (existingUsername == null) {
                    // Add new user
                    try (PreparedStatement check = con.prepareStatement("SELECT * FROM users WHERE username = ?")) {
                        check.setString(1, username);
                        ResultSet rs = check.executeQuery();
                        if (rs.next()) {
                            JOptionPane.showMessageDialog(dialog, "Username already exists.");
                            return;
                        }
                    }

                    PreparedStatement pst = con.prepareStatement("INSERT INTO users (username, password, role) VALUES (?, ?, ?)");
                    pst.setString(1, username);
                    pst.setString(2, password);
                    pst.setString(3, role);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(dialog, "User added successfully!");
                } else {
                    // Edit user
                    PreparedStatement pst = con.prepareStatement("UPDATE users SET password = ?, role = ? WHERE username = ?");
                    pst.setString(1, password);
                    pst.setString(2, role);
                    pst.setString(3, existingUsername);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(dialog, "User updated successfully!");
                }
                loadUsers();
                dialog.dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Database error: " + ex.getMessage());
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void deleteUser(String username) {
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement("DELETE FROM users WHERE username = ?")) {
            pst.setString(1, username);
            pst.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to delete user: " + e.getMessage());
        }
    }
}
