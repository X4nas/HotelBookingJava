import javax.swing.*;
import java.util.List;

public class ViewReservationsWindow extends JFrame {
    public ViewReservationsWindow(List<Reservation> reservations) {
        setTitle("All Reservations");
        setSize(600, 400);
        setLocationRelativeTo(null);

        String[] columnNames = {"Guest Name", "Phone", "Room Type", "Check-In", "Check-Out"};
        String[][] data = new String[reservations.size()][5];

        for (int i = 0; i < reservations.size(); i++) {
            Reservation r = reservations.get(i);
            data[i][0] = r.guestName;
            data[i][1] = r.phone;
            data[i][2] = r.roomType;
            data[i][3] = r.checkIn;
            data[i][4] = r.checkOut;
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
    }
}
