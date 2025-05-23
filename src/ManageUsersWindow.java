import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

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
            User user = findUserByUsername(username);
            openUserForm(user);
        });

        deleteUserBtn.addActionListener(e -> {
            int selectedRow = usersTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user to delete.");
                return;
            }
            String username = (String) tableModel.getValueAt(selectedRow, 0);
            User user = findUserByUsername(username);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete user: " + username + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                UserManager.removeUser(user);
                loadUsers();
            }
        });
    }

    private User findUserByUsername(String username) {
        return UserManager.getUsers().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    private void loadUsers() {
        tableModel.setRowCount(0);
        for (User u : UserManager.getUsers()) {
            tableModel.addRow(new Object[]{u.getUsername(), u.getRole()});
        }
    }

    private void openUserForm(User user) {
        JDialog dialog = new JDialog(this, user == null ? "Add User" : "Edit User", true);
        dialog.setSize(300, 250);
        dialog.setLayout(new GridLayout(5, 2, 10, 10));
        dialog.setLocationRelativeTo(this);

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"Admin", "User"});

        if (user != null) {
            usernameField.setText(user.getUsername());
            passwordField.setText(user.getPassword());
            roleBox.setSelectedItem(user.getRole());
            usernameField.setEditable(false); // username not editable on edit
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

            if (user == null) {
                // Adding new user - check if username exists
                if (UserManager.getUsers().stream().anyMatch(u -> u.getUsername().equals(username))) {
                    JOptionPane.showMessageDialog(dialog, "Username already exists.");
                    return;
                }
                UserManager.addUser(new User(username, password, role));
            } else {
                // Editing existing user
                user.setPassword(password);
                user.setRole(role);
            }
            loadUsers();
            dialog.dispose();
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }
}
