package javaapplication109;

// Import necessary libraries
import java.sql.*;  // For database interaction
import javax.swing.*;  // For creating the login GUI
import java.awt.event.*;  // For handling events like button clicks
import java.io.File;  // For file operations

public class LoginPage {

    private static final NotificationSystem notificationSystem = NotificationSystem.getInstance();
    // Define the database URLs for users and tasks
    private static final String DB_URL = "jdbc:sqlite:users.db";
    private static Connection conn;  // Connection object for the users database
    private static final String TASK_DB_URL = "jdbc:sqlite:tasks.db";
    private static Connection taskConn;  // Connection object for the tasks database

    public static void main(String[] args) {
        createDatabase();  // Call method to create the users database and table
        showLoginPage();  // Call method to show the login page
    }

    // Method to create the tasks database and its table if not already created
    private static void createTaskDatabase() {
        try {
            taskConn = DriverManager.getConnection(TASK_DB_URL);  // Establish connection to tasks database
            String sql = "CREATE TABLE IF NOT EXISTS tasks (id INTEGER PRIMARY KEY, name TEXT NOT NULL, description TEXT, status TEXT NOT NULL, assignedTo TEXT, deadline DATE)";  // SQL query to create tasks table
            Statement stmt = taskConn.createStatement();  // Create a statement
            stmt.execute(sql);  // Execute the query to create the table if it doesn't exist
        } catch (SQLException e) {  // Handle SQL exceptions
            e.printStackTrace();
        }
    }

    // Method to create the users database and its table if not already created
    private static void createDatabase() {
        try {
            conn = DriverManager.getConnection(DB_URL);  // Establish connection to the users database
            System.out.println("Database path: " + new File("users.db").getAbsolutePath());  // Print the database file path
            String sql = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, username TEXT NOT NULL, password TEXT NOT NULL, role TEXT NOT NULL)";  // SQL query to create users table
            Statement stmt = conn.createStatement();  // Create a statement
            stmt.execute(sql);  // Execute the query to create the table if it doesn't exist

            // Check if the users table exists
            String checkSql = "SELECT name FROM sqlite_master WHERE type='table' AND name='users'";
            ResultSet rs = stmt.executeQuery(checkSql);  // Execute the query to check for table existence
            if (rs.next()) {  // If the table exists, print a verification message
                System.out.println("Verified: Users table exists.");
            } else {  // If the table does not exist, print an error message
                System.err.println("Error: Users table does not exist.");
            }

            // Add sample users to the database
            addUser("admin", "admin123", "manager");
            addUser("employee3", "emp147", "designer");
            addUser("employee4", "emp789", "developer");
            addUser("employee5", "emp963", "accountant");
        } catch (SQLException e) {  // Handle SQL exceptions
            e.printStackTrace();
        }
    }

    // Method to add a user to the database
    private static void addUser(String username, String password, String role) {
        try {
            String sql = "INSERT INTO users(username, password, role) VALUES(?, ?, ?)";  // SQL query to insert a user
            PreparedStatement pstmt = conn.prepareStatement(sql);  // Prepare the SQL statement
            pstmt.setString(1, username);  // Set the username parameter
            pstmt.setString(2, password);  // Set the password parameter
            pstmt.setString(3, role);  // Set the role parameter
            pstmt.executeUpdate();  // Execute the update to insert the user
        } catch (SQLException e) {  // Handle SQL exceptions
            e.printStackTrace();
        }
    }

    // Method to show the login page
    private static void showLoginPage() {
        JFrame frame = new JFrame("Login");  // Create a new JFrame for the login page
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Set the default close operation for the frame
        frame.setSize(300, 200);  // Set the size of the frame

        JPanel panel = new JPanel();  // Create a panel to hold the components
        frame.add(panel);  // Add the panel to the frame
        placeComponents(panel);  // Call method to place components on the panel

        frame.setVisible(true);  // Make the frame visible
    }

    // Method to add components (labels, text fields, and buttons) to the panel
    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);  // Set the layout manager to null for manual placement of components

        // Create a label for the username field
        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(10, 20, 80, 25);  // Set the position and size of the label
        panel.add(userLabel);  // Add the label to the panel

        JTextField userText = new JTextField(20);  // Create a text field for the username input
        userText.setBounds(100, 20, 165, 25);  // Set the position and size of the text field
        panel.add(userText);  // Add the text field to the panel

        // Create a label for the password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 50, 80, 25);  // Set the position and size of the label
        panel.add(passwordLabel);  // Add the label to the panel

        JPasswordField passwordText = new JPasswordField(20);  // Create a password field for the password input
        passwordText.setBounds(100, 50, 165, 25);  // Set the position and size of the password field
        panel.add(passwordText);  // Add the password field to the panel

        // Create a login button
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(10, 80, 80, 25);  // Set the position and size of the button
        panel.add(loginButton);  // Add the button to the panel

        // Add an ActionListener to the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {  // Handle button click
                String username = userText.getText();  // Get the entered username
                String password = new String(passwordText.getPassword());  // Get the entered password
                if (checkLogin(username, password)) {  // Check if login credentials are correct
                    String role = getRole(username);  // Get the role of the user

                    // Use the UserFactory to create user objects
                    User user = UserFactory.createUser(role, username);
                    if (user != null) {
                        notificationSystem.notifyTaskAdded(new Task(user.getName() + " logged in") {
                            @Override
                            public String getType() {
                                return "";//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                            }
                        }); // Notify observers

                        if ("manager".equals(role)) {  // If the user is a manager, open the manager interface
                            openManagerInterface();
                        } else if ("designer".equals(role) || "accountant".equals(role) || "developer".equals(role)) {  // If the user is an employee, open the employee interface
                            openEmployeeInterface();
                        }
                    }
                } else {  // If credentials are invalid, show an error message
                    JOptionPane.showMessageDialog(panel, "Invalid credentials!");
                }
            }
        });

    }

    // Method to check if the login credentials are correct
    private static boolean checkLogin(String username, String password) {
        try {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";  // SQL query to check credentials
            PreparedStatement pstmt = conn.prepareStatement(sql);  // Prepare the SQL statement
            pstmt.setString(1, username);  // Set the username parameter
            pstmt.setString(2, password);  // Set the password parameter
            ResultSet rs = pstmt.executeQuery();  // Execute the query and get the result set
            boolean exists = rs.next();  // Check if the result set contains a record
            System.out.println("Login check for " + username + ": " + exists);  // Print login check result
            return exists;  // Return true if the credentials are correct, false otherwise
        } catch (SQLException e) {  // Handle SQL exceptions
            e.printStackTrace();
        }
        return false;  // Return false if an error occurred
    }

    // Method to get the role of a user
    private static String getRole(String username) {
        try {
            String sql = "SELECT role FROM users WHERE username = ?";  // SQL query to get the user's role
            PreparedStatement pstmt = conn.prepareStatement(sql);  // Prepare the SQL statement
            pstmt.setString(1, username);  // Set the username parameter
            ResultSet rs = pstmt.executeQuery();  // Execute the query and get the result set
            if (rs.next()) {  // If the result set contains a record
                String role = rs.getString("role");  // Get the role from the result set
                System.out.println("Role for " + username + ": " + role);  // Print the user's role
                return role;  // Return the user's role
            }
        } catch (SQLException e) {  // Handle SQL exceptions
            e.printStackTrace();
        }
        return null;  // Return null if the role is not found
    }

    // Method to open the manager interface
    private static void openManagerInterface() {
        javafx.application.Application.launch(ManagerUI.class);  // Launch the Manager UI using JavaFX
    }

    // Method to open the employee interface
    private static void openEmployeeInterface() {
        new Employee();  // Create a new Employee object (presumably to show employee UI)
    }
}