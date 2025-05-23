import java.util.ArrayList;
import java.util.List;

public class UserManager {
    static List<User> users = new ArrayList<>();

    static {
        // Default users for testing
        users.add(new User("admin", "admin123", "Admin"));
        users.add(new User("user1", "user123", "User"));
    }

    public static List<User> getUsers() {
        return users;
    }

    public static void addUser(User user) {
        users.add(user);
    }

    public static void removeUser(User user) {
        users.remove(user);
    }
}
