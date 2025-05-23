import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManageRoomsWindow extends JFrame {
    private DefaultTableModel tableModel;

    public ManageRoomsWindow() {
        setTitle("Manage Rooms");
        setSize(600, 400);
        setLocationRelativeTo(null);

        String[] columnNames = {"Room No", "Type", "Price"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        JPanel panel = new JPanel();
        JButton addBtn = new JButton("Add Room");
        JButton deleteBtn = new JButton("Delete Selected");
        panel.add(addBtn);
        panel.add(deleteBtn);

        addBtn.addActionListener(e -> {
            String roomNo = JOptionPane.showInputDialog(this, "Room Number:");
            String type = JOptionPane.showInputDialog(this, "Room Type:");
            String price = JOptionPane.showInputDialog(this, "Room Price:");
            if (roomNo != null && type != null && price != null && !roomNo.isEmpty() && !type.isEmpty() && !price.isEmpty())
                tableModel.addRow(new Object[]{roomNo, type, price});
            else
                JOptionPane.showMessageDialog(this, "All fields are required to add a room.");
        });

        deleteBtn.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected != -1) {
                tableModel.removeRow(selected);
            } else {
                JOptionPane.showMessageDialog(this, "Select a room to delete.");
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);
    }
}
