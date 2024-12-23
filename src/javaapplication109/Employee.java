package javaapplication109;

// Import necessary packages
import javax.swing.*; // Import Swing components for GUI
import java.awt.*; // Import AWT components for layout and components
import java.awt.event.ActionEvent; // Import for handling action events
import java.awt.event.ActionListener; // Import for listening to action events
import java.sql.Connection; // Import for database connection
import java.sql.Date; // Import for handling SQL Date
import java.sql.DriverManager; // Import for managing database connection
import java.sql.PreparedStatement; // Import for executing SQL statements
import java.sql.ResultSet; // Import for handling SQL query results
import java.sql.SQLException; // Import for handling SQL exceptions
import java.time.LocalDate; // Import for handling LocalDate
import java.time.LocalDateTime; // Import for handling LocalDateTime
import java.time.ZoneOffset; // Import for handling time zone offsets
import java.time.temporal.ChronoUnit; // Import for calculating time difference

public class Employee  {

    // Declare panels for different task states
    private static JPanel newTasksPanel, doingTasksPanel, finishedTasksPanel;

    // Declare connection for database operations
    private static Connection taskConn;

    // Constructor to set up the Employee UI
    public Employee() {
        JFrame frame = new JFrame("Task Manager"); // Create main frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close on exit
        frame.setSize(900, 600); // Set size of the window
        frame.setLayout(new BorderLayout()); // Set layout for the frame

        JLabel header = new JLabel("HELLO, There", SwingConstants.CENTER); // Header label
        header.setFont(new Font("Arial", Font.BOLD, 24)); // Set font style
        header.setForeground(Color.MAGENTA); // Set text color
        frame.add(header, BorderLayout.NORTH); // Add header to the top of the frame

        JPanel mainPanel = new JPanel(new GridLayout(1, 3, 20, 0)); // Create a panel with 3 columns
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Set border for the panel

        newTasksPanel = createTaskColumn("New Tasks"); // Create a column for new tasks
        doingTasksPanel = createTaskColumn("Doing Tasks"); // Create a column for tasks in progress
        finishedTasksPanel = createTaskColumn("Finished Tasks"); // Create a column for completed tasks

        // Add task columns to the main panel
        mainPanel.add(newTasksPanel);
        mainPanel.add(doingTasksPanel);
        mainPanel.add(finishedTasksPanel);

        frame.add(mainPanel, BorderLayout.CENTER); // Add the main panel to the center of the frame
        frame.setVisible(true); // Make the frame visible

        connectToDatabase(); // Connect to the database

        loadTasksForEmployee("employee1"); // Load tasks assigned to "employee1"
    }

    // Method to connect to the database
    private void connectToDatabase() {
        try {
            taskConn = DriverManager.getConnection("jdbc:sqlite:tasks.db"); // Establish database connection
        } catch (SQLException e) {
            e.printStackTrace(); // Print any SQL exceptions
        }
    }

    // Method to create a task column with a title
    private static JPanel createTaskColumn(String title) {
        JPanel columnPanel = new JPanel(); // Create a panel for the column
        columnPanel.setLayout(new BoxLayout(columnPanel, BoxLayout.Y_AXIS)); // Set layout to vertical box

        JLabel columnTitle = new JLabel(title, SwingConstants.CENTER); // Create title label for the column
        columnTitle.setFont(new Font("Arial", Font.BOLD, 18)); // Set font for the title
        columnPanel.add(columnTitle); // Add the title to the column

        columnPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add space below the title
        return columnPanel; // Return the column panel
    }

    // Method to load tasks for a specific employee
    private void loadTasksForEmployee(String employeeName) {
        try {
            String sql = "SELECT * FROM tasks WHERE assignedTo = ?"; // SQL query to select tasks for the employee
            PreparedStatement pstmt = taskConn.prepareStatement(sql); // Prepare the statement
            pstmt.setString(1, employeeName); // Set the employee name in the query
            ResultSet rs = pstmt.executeQuery(); // Execute the query

            while (rs.next()) { // Iterate through the results
                int taskId = rs.getInt("id"); // Get task ID
                String taskName = rs.getString("name"); // Get task name
                String description = rs.getString("description"); // Get task description
                String status = rs.getString("status"); // Get task status
                LocalDate deadline = LocalDateTime.ofEpochSecond(rs.getLong("deadline") / 1000, 0, ZoneOffset.UTC).toLocalDate(); // Convert deadline to LocalDate
                LocalDate addDate = LocalDateTime.ofEpochSecond(rs.getLong("addDate") / 1000, 0, ZoneOffset.UTC).toLocalDate(); // Convert add date to LocalDate
                // Depending on the task status, add the task to the corresponding panel
                if (status.equals("Pending")) {
                    addTaskToColumn(newTasksPanel, taskId, taskName, description, deadline, addDate, "Accept");
                } else if (status.equals("In-Progress")) {
                    addTaskToColumn(doingTasksPanel, taskId, taskName, description, deadline, addDate, "Complete");
                } else if (status.equals("Complete")) {
                    addTaskToColumn(finishedTasksPanel, taskId, taskName, description, deadline, addDate, "Done");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print any SQL exceptions
        }
    }

    // Method to add a task to a specific column
    private static void addTaskToColumn(JPanel columnPanel, int taskId, String taskName, String description, LocalDate deadline, LocalDate addDate, String buttonText) {
        JPanel taskPanel = new JPanel(new BorderLayout()); // Create a panel for the task
        taskPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1)); // Set border for the task panel
        taskPanel.setMaximumSize(new Dimension(250, 100)); // Set maximum size for the panel

        taskPanel.putClientProperty("taskId", taskId); // Store task ID in the panel

        JTextArea taskDescription = new JTextArea(taskName + "\n" + description + "\nDeadline: " + deadline); // Create a text area for task description
        taskDescription.setEditable(false); // Make the text area non-editable
        taskDescription.setFont(new Font("Arial", Font.PLAIN, 12)); // Set font for the description
        taskPanel.add(taskDescription, BorderLayout.CENTER); // Add description to the center of the panel

        JLabel timerLabel = new JLabel(); // Create a label for the timer
        long remainingTime = calculateRemainingTime(addDate, deadline); // Calculate the remaining time
        timerLabel.setText(formatTime(remainingTime)); // Set the remaining time text
        taskPanel.add(timerLabel, BorderLayout.SOUTH); // Add timer label to the bottom of the panel

        JButton actionButton = new JButton(buttonText); // Create a button for the action
        actionButton.setFont(new Font("Arial", Font.BOLD, 14)); // Set font for the button
        if (buttonText.equals("Accept")) {
            actionButton.setForeground(Color.MAGENTA); // Set color for "Accept" button
        } else if (buttonText.equals("Complete")) {
            actionButton.setForeground(Color.BLUE); // Set color for "Complete" button
        } else if (buttonText.equals("Done")) {
            actionButton.setForeground(Color.GREEN); // Set color for "Done" button
            actionButton.setEnabled(false); // Disable the button for "Done"
        }
        actionButton.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 2)); // Set border for the button
        taskPanel.add(actionButton, BorderLayout.EAST); // Add button to the right side of the panel

        // Add action listener for the button
        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (buttonText.equals("Accept")) {
                    moveTask(taskPanel, newTasksPanel, doingTasksPanel, "Complete", "in-Progress", Color.MAGENTA); // Move task to "In-Progress"
                } else if (buttonText.equals("Complete")) {
                    moveTask(taskPanel, doingTasksPanel, finishedTasksPanel, "Done", "Complete", Color.GREEN); // Move task to "Complete"
                }
            }
        });

        columnPanel.add(taskPanel); // Add task panel to the column
        columnPanel.revalidate(); // Revalidate the column
        columnPanel.repaint(); // Repaint the column
    }

    // Method to move a task between columns
    private static void moveTask(JPanel taskPanel, JPanel currentColumn, JPanel targetColumn, String newButtonText, String newStatus, Color buttonColor) {
        currentColumn.remove(taskPanel); // Remove task from current column
        Integer taskId = (Integer) taskPanel.getClientProperty("taskId"); // Get task ID
        if (taskId != null) {
            updateTaskStatusInDatabase(taskId, newButtonText); // Update task status in the database
            updateTaskStatusInDatabase(taskId, newStatus); // Update task status in the database
            updateManagerTaskStatusInDatabase(taskId, newStatus); // Update manager's task status in the database
            System.out.println("Moving task ID " + taskId + " to status " + newStatus); // Print task movement info
        }

        taskPanel.removeAll(); // Remove all components from the task panel

        JTextArea taskDescription = new JTextArea("Task in Progress\nDescription updated...\n"); // Create a new description
        taskDescription.setEditable(false); // Make the description non-editable
        taskDescription.setFont(new Font("Arial", Font.PLAIN, 12)); // Set font for the description
        taskPanel.add(taskDescription, BorderLayout.CENTER); // Add description to the panel

        // Create a new button based on the new status
        JButton button = new JButton(newStatus.equals("In-Progress") ? "Complete" : "Done");
        button.setFont(new Font("Arial", Font.BOLD, 14)); // Set font for the button
        button.setForeground(buttonColor); // Set color for the button
        button.setBorder(BorderFactory.createLineBorder(buttonColor, 2)); // Set border for the button
        taskPanel.add(button, BorderLayout.EAST); // Add the button to the right side of the panel

        button.addActionListener(e -> {
            if (newButtonText.equals("Complete")) {
                moveTask(taskPanel, doingTasksPanel, finishedTasksPanel, "Done", "Complete", Color.GREEN); // Move task to "Done"
            }
        });

        targetColumn.add(taskPanel); // Add task panel to the target column
        targetColumn.revalidate(); // Revalidate the target column
        targetColumn.repaint(); // Repaint the target column

        currentColumn.revalidate(); // Revalidate the current column
        currentColumn.repaint(); // Repaint the current column
    }

    // Method to update the task status in the database for the manager
    private static void updateManagerTaskStatusInDatabase(int taskId, String newStatus) {
        try {
            String sql = "UPDATE tasks SET status = ? WHERE id = ?"; // SQL query to update task status
            PreparedStatement pstmt = taskConn.prepareStatement(sql); // Prepare the statement
            pstmt.setString(1, newStatus); // Set new status in the query
            pstmt.setInt(2, taskId); // Set task ID in the query
            pstmt.executeUpdate(); // Execute the update
        } catch (SQLException e) {
            e.printStackTrace(); // Print any SQL exceptions
        }
    }

    // Method to calculate the remaining time before a task deadline
    private static long calculateRemainingTime(LocalDate addDate, LocalDate deadline) {
        LocalDateTime deadlineDateTime = deadline.atTime(23, 59, 59); // Set the deadline time to the end of the day
        LocalDateTime now = LocalDateTime.now(); // Get the current time
        long seconds = ChronoUnit.SECONDS.between(now, deadlineDateTime); // Calculate the time difference in seconds
        return Math.max(seconds, 0); // Return the remaining time (cannot be negative)
    }

    // Method to format the remaining time in days, hours, and minutes
    private static String formatTime(long totalSeconds) {
        long days = totalSeconds / (24 * 3600); // Calculate days
        long hours = (totalSeconds % (24 * 3600)) / 3600; // Calculate hours
        long minutes = (totalSeconds % 3600) / 60; // Calculate minutes
        return String.format("%02d:%02d:%02d", days, hours, minutes); // Format time as string
    }

    // Method to update the task status in the database
    private static void updateTaskStatusInDatabase(int taskId, String newStatus) {
        try {
            String sql = "UPDATE tasks SET status = ? WHERE id = ?"; // SQL query to update task status
            PreparedStatement pstmt = taskConn.prepareStatement(sql); // Prepare the statement
            pstmt.setString(1, newStatus); // Set new status in the query
            pstmt.setInt(2, taskId); // Set task ID in the query
            pstmt.executeUpdate(); // Execute the update
        } catch (SQLException e) {
            e.printStackTrace(); // Print any SQL exceptions
        }

    }

    // Method to save a task to the database
    private void saveTaskToDatabase(Task task) {
        try {
            // SQL query to insert a new task into the database
            String sql = "INSERT INTO tasks (name, description, status, assignedTo, addDate, deadline) VALUES (?, ?, ?, ?, ?, ?)";
            // Prepare the SQL statement to be executed
            PreparedStatement pstmt = taskConn.prepareStatement(sql);

            // Set the values of the placeholders in the SQL query
            pstmt.setString(1, task.getName()); // Set task name
            pstmt.setString(2, task.getDescription()); // Set task description
            pstmt.setString(3, task.getStatus()); // Set task status
            pstmt.setString(4, task.getResponsibleUsers()); // Set the responsible users for the task
            pstmt.setDate(5, Date.valueOf(task.getAddDate())); // Set the task addition date
            pstmt.setDate(6, Date.valueOf(task.getDeadline())); // Set the task deadline date

            // Execute the update to save the task in the database
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // If an exception occurs, print the stack trace
            e.printStackTrace();
        }
    }

}
