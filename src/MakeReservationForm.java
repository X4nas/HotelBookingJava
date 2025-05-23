import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MakeReservationForm extends JFrame {

    private String username;

    private JTable reservationsTable;
    private DefaultTableModel tableModel;

    private JTextField tfRoomNumber;
    private JTextField tfGuestName;
    private JTextField tfPhone;
    private JTextField tfCheckInDate;
    private JTextField tfCheckOutDate;

    public MakeReservationForm() {
        this.username = null;
        initializeUI();
    }

    public MakeReservationForm(String username) {
        this.username = username;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Make Reservation" + (username != null ? " - " + username : ""));
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table to show existing reservations
        String[] columns = {"Reservation ID", "Room No", "Guest Name", "Phone", "Check-In", "Check-Out"};
        tableModel = new DefaultTableModel(columns, 0);
        reservationsTable = new JTable(tableModel);
        add(new JScrollPane(reservationsTable), BorderLayout.CENTER);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Room Number:"));
        tfRoomNumber = new JTextField();
        formPanel.add(tfRoomNumber);

        formPanel.add(new JLabel("Guest Name:"));
        tfGuestName = new JTextField();
        formPanel.add(tfGuestName);

        formPanel.add(new JLabel("Phone Number:"));
        tfPhone = new JTextField();
        formPanel.add(tfPhone);

        formPanel.add(new JLabel("Check-In Date (YYYY-MM-DD):"));
        tfCheckInDate = new JTextField(LocalDate.now().toString());
        formPanel.add(tfCheckInDate);

        formPanel.add(new JLabel("Check-Out Date (YYYY-MM-DD):"));
        tfCheckOutDate = new JTextField(LocalDate.now().plusDays(1).toString());
        formPanel.add(tfCheckOutDate);

        JButton btnReserve = new JButton("Make Reservation");
        formPanel.add(btnReserve);

        JButton btnRefresh = new JButton("Refresh");
        formPanel.add(btnRefresh);

        add(formPanel, BorderLayout.SOUTH);

        // Load reservations on startup
        loadReservationsFromDB();

        // Button Listeners
        btnReserve.addActionListener(e -> makeReservation());
        btnRefresh.addActionListener(e -> loadReservationsFromDB());
    }

    private void loadReservationsFromDB() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT reservation_id, room_number, guest_name, phone, check_in, check_out FROM reservations")) {

            tableModel.setRowCount(0);

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("reservation_id"),
                        rs.getInt("room_number"),
                        rs.getString("guest_name"),
                        rs.getString("phone"),
                        rs.getTimestamp("check_in").toString(),
                        rs.getTimestamp("check_out").toString()
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading reservations: " + e.getMessage());
        }

    }

    private void makeReservation() {
        String roomStr = tfRoomNumber.getText().trim();
        String guestName = tfGuestName.getText().trim();
        String phone = tfPhone.getText().trim();
        String checkInStr = tfCheckInDate.getText().trim();
        String checkOutStr = tfCheckOutDate.getText().trim();

        if (roomStr.isEmpty() || guestName.isEmpty() || phone.isEmpty() || checkInStr.isEmpty() || checkOutStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        int roomNumber;
        try {
            roomNumber = Integer.parseInt(roomStr);
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Invalid room number.");
            return;
        }

        // Validate date format
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate checkInDate, checkOutDate;
        try {
            checkInDate = LocalDate.parse(checkInStr, dtf);
            checkOutDate = LocalDate.parse(checkOutStr, dtf);
            if (!checkOutDate.isAfter(checkInDate)) {
                JOptionPane.showMessageDialog(this, "Check-Out date must be after Check-In date.");
                return;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.");
            return;
        }

        // Insert reservation and update room status
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check if room is available
            PreparedStatement checkRoom = conn.prepareStatement("SELECT status FROM rooms WHERE room_number = ?");
            checkRoom.setInt(1, roomNumber);
            ResultSet rs = checkRoom.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "Room number does not exist.");
                return;
            }

            String roomStatus = rs.getString("status");
            if (!roomStatus.equalsIgnoreCase("Available")) {
                JOptionPane.showMessageDialog(this, "Room is not available.");
                return;
            }

            // Insert reservation
            String insertSql = "INSERT INTO reservations (room_number, guest_name, phone, check_in, check_out) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setInt(1, roomNumber);
            insertStmt.setString(2, guestName);
            insertStmt.setString(3, phone);
            insertStmt.setDate(4, Date.valueOf(checkInDate));
            insertStmt.setDate(5, Date.valueOf(checkOutDate));
            insertStmt.executeUpdate();

            // Update room status to Occupied
            PreparedStatement updateRoom = conn.prepareStatement("UPDATE rooms SET status = 'Occupied' WHERE room_number = ?");
            updateRoom.setInt(1, roomNumber);
            updateRoom.executeUpdate();

            JOptionPane.showMessageDialog(this, "Reservation successful!");

            // Refresh table and clear fields
            loadReservationsFromDB();
            clearForm();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error processing reservation.");
        }
    }

    private void clearForm() {
        tfRoomNumber.setText("");
        tfGuestName.setText("");
        tfPhone.setText("");
        tfCheckInDate.setText(LocalDate.now().toString());
        tfCheckOutDate.setText(LocalDate.now().plusDays(1).toString());
    }

    // Test main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MakeReservationForm("TestUser").setVisible(true);
        });
    }
}
