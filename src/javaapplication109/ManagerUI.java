package javaapplication109;

import javafx.scene.paint.Color; // Import the Color class for handling color values
import javafx.animation.KeyFrame; // Import KeyFrame for creating animation frames
import javafx.animation.Timeline; // Import Timeline to control animation timing
import javafx.util.Duration; // Import Duration for setting the time duration in animations
import java.time.LocalDate; // Import LocalDate to handle date values
import java.time.LocalDateTime; // Import LocalDateTime to handle both date and time values
import java.time.temporal.ChronoUnit; // Import ChronoUnit to handle time unit operations
import javafx.application.Application; // Import Application class for JavaFX app structure
import javafx.geometry.Insets; // Import Insets for setting padding around nodes
import javafx.scene.Scene; // Import Scene to manage the UI layout
import javafx.scene.control.*; // Import various controls like buttons, text fields, etc.
import javafx.scene.layout.*; // Import layout managers like VBox, HBox, etc.
import javafx.stage.Stage; // Import Stage for the window of the application
import java.sql.Connection; // Import Connection for database connection
import java.sql.Date; // Import Date to handle SQL date values
import java.sql.DriverManager; // Import DriverManager to manage database drivers
import java.sql.PreparedStatement; // Import PreparedStatement for executing SQL queries
import java.sql.ResultSet; // Import ResultSet to retrieve results from queries
import java.sql.SQLException; // Import SQLException for handling SQL errors
import java.sql.Statement; // Import Statement to execute SQL queries
import java.time.ZoneOffset; // Import ZoneOffset for handling timezone differences
import java.util.ArrayList; // Import ArrayList for storing dynamic lists of data
import java.util.List; // Import List interface for managing collections of tasks

public class ManagerUI extends Application { // Define the ManagerUI class extending Application
    private TaskManager taskManager = TaskManager.getInstance();
    private static Connection taskConn; // Static connection variable for database access
    private List<Task> taskList = new ArrayList<>(); // List to store tasks in the app
    private List<Task> originalTaskList = new ArrayList<>(); // Original list of tasks for reference

    private VBox tasksListPending = new VBox(10); // VBox to display pending tasks
    private VBox tasksListInProgress = new VBox(10); // VBox to display tasks in progress
    private VBox tasksListComplete = new VBox(10); // VBox to display completed tasks

    private TextField taskNameField, taskForField, roleField; // Text fields for task details
    private TextArea descriptionArea; // Text area for task description
    private DatePicker addDatePicker, deadlinePicker; // DatePickers for task dates
    private ComboBox<String> taskStatusCombo; // ComboBox for selecting task status
    private TextField searchField; // TextField for searching tasks
    private ComboBox<String> roleCombo; // ComboBox for selecting task role
    private ComboBox<String> employeeComboBox; // ComboBox for selecting employees

    @Override
    public void start(Stage primaryStage) { // Main entry point for the application
        connectToDatabase(); // Connect to the database
        createTasksTable(); // Create the tasks table in the database if it doesn't exist

        Label helloLabel = new Label("HELLO"); // Create a label for the greeting
        helloLabel.setStyle("-fx-font-size: 24px;"); // Set style for the hello label
        Label managerLabel = new Label("MANAGER"); // Create a label for the manager title
        managerLabel.setStyle("-fx-font-size: 36px; -fx-text-fill: purple;"); // Style the manager label
        employeeComboBox = new ComboBox<>(); // Create a ComboBox for employee selection
        loadEmployeeNames(); // Load employee names into the ComboBox
        taskNameField = new TextField(); // Create a text field for task name
        taskNameField.setPromptText("Task Name"); // Set prompt text for task name field

        addDatePicker = new DatePicker(); // Create a date picker for the add date
        deadlinePicker = new DatePicker(); // Create a date picker for the deadline

        taskForField = new TextField(); // Create a text field for the "task for" field
        taskForField.setPromptText("Task for.."); // Set prompt text for "task for" field

        roleCombo = new ComboBox<>(); // Create a ComboBox for selecting task role
        roleCombo.getItems().addAll("Manager", "Developer", "Accountant", "HR", "Designer"); // Add roles to the combo box
        roleCombo.setPromptText("Select Role"); // Set prompt text for the role combo box

        taskStatusCombo = new ComboBox<>(); // Create a ComboBox for selecting task status
        taskStatusCombo.getItems().addAll("Pending", "In-Progress", "Complete"); // Add statuses to the combo box
        taskStatusCombo.setPromptText("Task Status"); // Set prompt text for the task status combo box

        descriptionArea = new TextArea(); // Create a text area for the task description
        descriptionArea.setPromptText("Description"); // Set prompt text for description area

        Button addButton = new Button("ADD"); // Create a button to add tasks
        addButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 16px;"); // Style the add button
        addButton.setOnAction(e -> addTask()); // Set action for the add button to add a task

        VBox leftPanel = new VBox(10, helloLabel, managerLabel, taskNameField, // Create the left panel with task fields and buttons
                new HBox(10, addDatePicker, deadlinePicker), // Create a horizontal box for date pickers
                new HBox(10, taskForField, roleCombo, taskStatusCombo), // Create a horizontal box for task fields
                descriptionArea, addButton); // Add description area and button to the layout
        leftPanel.setPadding(new Insets(20)); // Set padding around the left panel

        searchField = new TextField(); // Create a text field for searching tasks
        searchField.setPromptText("Search Tasks"); // Set prompt text for the search field
        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchTasks(newValue)); // Add listener for search field

        TabPane tabPane = new TabPane(); // Create a TabPane to organize task views
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE); // Disable tab closing
        tabPane.setPrefWidth(800); // Set preferred width of the TabPane

        tasksListPending = new VBox(10); // Initialize the VBox for pending tasks
        tasksListInProgress = new VBox(10); // Initialize the VBox for in-progress tasks
        tasksListComplete = new VBox(10); // Initialize the VBox for completed tasks

        Tab pendingTab = new Tab("Pending", tasksListPending); // Create a tab for pending tasks
        Tab inProgressTab = new Tab("In-Progress", tasksListInProgress); // Create a tab for in-progress tasks
        Tab completeTab = new Tab("Complete", tasksListComplete); // Create a tab for completed tasks

        tabPane.getTabs().addAll(pendingTab, inProgressTab, completeTab); // Add tabs to the TabPane

        VBox searchAndTabs = new VBox(10, searchField, tabPane); // Create a VBox for search field and tab pane
        searchAndTabs.setPadding(new Insets(20)); // Set padding around the search and tab container

        HBox mainLayout = new HBox(20, leftPanel, searchAndTabs); // Create the main layout with left panel and search/tabs
        mainLayout.setPadding(new Insets(20)); // Set padding around the main layout

        Scene scene = new Scene(mainLayout, 1200, 600); // Create a scene with the main layout
        primaryStage.setScene(scene); // Set the scene to the primary stage
        primaryStage.setTitle("Task Manager UI"); // Set the title of the window
        primaryStage.show(); // Show the window

        displayTasks(); // Display tasks in the UI
    }

    // Method to load the employee usernames into the combo box
    private void loadEmployeeNames() {
        try {
            // SQL query to fetch all usernames where role is 'employee'
            String sql = "SELECT username FROM users WHERE role = 'employee'";

            // Create a statement object to execute the SQL query
            Statement stmt = taskConn.createStatement();

            // Execute the query and store the result in a ResultSet
            ResultSet rs = stmt.executeQuery(sql);

            // Loop through the ResultSet to get each employee's username
            while (rs.next()) {
                // Add the username to the combo box
                employeeComboBox.getItems().add(rs.getString("username"));
            }
        } catch (SQLException e) {
            // Print the exception if there's an SQL error
            e.printStackTrace();
        }
    }

// Method to connect to the tasks database
    private void connectToDatabase() {
        try {
            // Establish a connection to the tasks database (tasks.db)
            taskConn = DriverManager.getConnection("jdbc:sqlite:tasks.db");
        } catch (SQLException e) {
            // Print the exception if there's an error connecting to the database
            e.printStackTrace();
        }
    }

// Method to connect to the users database
    private void connectToUserDatabase() {
        try {
            // Establish a connection to the users database (users.db)
            taskConn = DriverManager.getConnection("jdbc:sqlite:users.db");
        } catch (SQLException e) {
            // Print the exception if there's an error connecting to the database
            e.printStackTrace();
        }
    }

// Method to create the 'tasks' table if it doesn't already exist
    private void createTasksTable() {
        try {
            // SQL query to create the tasks table with necessary columns
            String sql = "CREATE TABLE IF NOT EXISTS tasks (id INTEGER PRIMARY KEY, name TEXT NOT NULL, description TEXT, status TEXT NOT NULL, assignedTo TEXT, role TEXT, deadline DATE, addDate DATE)";

            // Create a statement object to execute the SQL query
            Statement stmt = taskConn.createStatement();

            // Execute the query to create the table
            stmt.execute(sql);
        } catch (SQLException e) {
            // Print the exception if there's an error creating the table
            e.printStackTrace();
        }
    }

// Method to print the list of tasks from the database
    private void printTasks() {
        // Retrieve the list of tasks from the database
        List<Task> tasks = getTasksFromDatabase();

        // Loop through each task and print its details
        for (Task task : tasks) {
            // Print task ID, name, description, and role
            System.out.println("ID: " + task.getId() + ", Name: " + task.getName() + ", Description: " + task.getDescription() + ", Role: " + task.getRole());
        }
    }

// Method to check and print the tasks' names and statuses from the database
    private void checkDatabase() {
        // Retrieve the list of tasks from the database
        List<Task> tasks = getTasksFromDatabase();

        // Loop through each task and print its name and status
        for (Task task : tasks) {
            // Print task name and status
            System.out.println("Task: " + task.getName() + ", Status: " + task.getStatus());
        }
    }

// Declare a variable for the task being edited, initially set to null
    private Task taskToEdit = null;

// Declare a flag to indicate if the system is in editing mode, initially set to false
    private boolean isEditing = false;

// Method to add a new task or update an existing one
    private void addTask() {
        // Get the values from the input fields
        String taskName = taskNameField.getText();
        String description = descriptionArea.getText();
        String status = "Pending";  // Set the default status as "Pending"
        String taskFor = taskForField.getText();
        String role = roleCombo.getValue();
        LocalDate deadline = deadlinePicker.getValue();
        LocalDate addDate = addDatePicker.getValue();
        
        // Print task details to the console
        System.out.println("Adding task: " + taskName + ", Assigned to: " + taskFor + ", Role: " + role);

        // Check if any required fields are empty
        if (taskName.isEmpty() || description.isEmpty() || taskFor.isEmpty() || role == null || deadline == null || addDate == null) {
            // Show an error alert if any fields are empty
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        // Add the task to the database and get its ID
        int id = addTaskToDatabase(taskName, description, status, taskFor, role, deadline, addDate);

        // Create a new Task object with the provided details
        Task newTask = new Task(id, taskName, description, status, taskFor, role, deadline, addDate) {
            // Override the getType() method to return an empty string (custom behavior for the Task object)
            @Override
            public String getType() {
                return "";
            }
        };

        // Check if we are editing an existing task
        if (isEditing && taskToEdit != null) {
            // Find the index of the task to be edited in the task list
            int index = taskList.indexOf(taskToEdit);

            // If the task is found in the list, update it
            if (index != -1) {
                // Replace the old task with the new one in the task list
                taskList.set(index, newTask);

                // Update the task in the original list as well
                for (int i = 0; i < originalTaskList.size(); i++) {
                    if (originalTaskList.get(i) == taskToEdit) {
                        originalTaskList.set(i, newTask);
                        break;
                    }
                }
                // Show a success alert for task update
                showAlert("Task Updated", "The task has been updated successfully.");
            }
        } else {
            // If not editing, add the new task to the task list
            taskList.add(newTask);
            originalTaskList.add(newTask);
            // Show a success alert for task addition
            showAlert("Task Added", "The task has been added successfully.");
        }

        // Clear the input fields
        taskNameField.clear();
        descriptionArea.clear();
        taskForField.clear();
        roleCombo.setValue(null);
        addDatePicker.setValue(null);
        deadlinePicker.setValue(null);

        // Reset the editing state and task to edit
        isEditing = false;
        taskToEdit = null;

        // Update the task display
        displayTasks();
    }

    // Method to add a new task to the database and return its generated ID
    private int addTaskToDatabase(String taskName, String description, String status, String taskFor, String role, LocalDate deadline, LocalDate addDate) {
        // Declare a variable to store the generated ID of the task
        int generatedId = 0;

        try {
            // SQL query to check if a task with the same name and assigned person already exists
            String checkSql = "SELECT COUNT(*) FROM tasks WHERE name = ? AND assignedTo = ?";

            // Prepare the SQL query to prevent SQL injection
            PreparedStatement checkStmt = taskConn.prepareStatement(checkSql);

            // Set the parameters for the SQL query
            checkStmt.setString(1, taskName);
            checkStmt.setString(2, taskFor);

            // Execute the query and get the result set
            ResultSet checkRs = checkStmt.executeQuery();

            // Check if any rows are returned (i.e., the task already exists)
            if (checkRs.next() && checkRs.getInt(1) > 0) {
                // If task exists, print a message and return 0 (no new task added)
                System.out.println("Task already exists: " + taskName);
                return generatedId;
            }

            // SQL query to insert a new task into the tasks table
            String sql = "INSERT INTO tasks(name, description, status, assignedTo, role, deadline, addDate) VALUES(?, ?, ?, ?, ?, ?, ?)";

            // Prepare the SQL insert statement
            PreparedStatement pstmt = taskConn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Set the parameters for the insert statement
            pstmt.setString(1, taskName);
            pstmt.setString(2, description);
            pstmt.setString(3, status);
            pstmt.setString(4, taskFor);
            pstmt.setString(5, role);
            pstmt.setDate(6, Date.valueOf(deadline));
            pstmt.setDate(7, Date.valueOf(addDate));

            // Execute the insert statement
            pstmt.executeUpdate();

            // Retrieve the generated keys (i.e., the ID of the newly inserted task)
            ResultSet rs = pstmt.getGeneratedKeys();

            // If the result set has a generated key, assign it to the generatedId variable
            if (rs.next()) {
                generatedId = rs.getInt(1);
                // Print the ID of the newly added task
                System.out.println("Task added with ID: " + generatedId);
            }
        } catch (SQLException e) {
            // If an exception occurs, print the stack trace
            e.printStackTrace();
        }

        // Return the generated ID of the new task (0 if no task was added)
        return generatedId;
    }

    // Method to display tasks in different lists based on their status
    private void displayTasks() {
        // Clear the existing children (tasks) from the UI elements
        tasksListPending.getChildren().clear();
        tasksListInProgress.getChildren().clear();
        tasksListComplete.getChildren().clear();

        // Retrieve the list of tasks from the database
        List<Task> tasks = getTasksFromDatabase();

        // Loop through the list of tasks to display each one
        for (Task task : taskList) {
            // Create a label for the task name and description
            Label taskLabel = new Label(task.getName() + "\n" + task.getDescription());
            taskLabel.setWrapText(true); // Set the label to wrap text if it's too long

            // Create a label to display a timer for the task
            Label timerLabel = new Label();
            startTimer(task, timerLabel); // Start the timer for this task

            // Create an "UPDATE" button for the task
            Button updateButton = new Button("UPDATE");
            updateButton.setStyle("-fx-text-fill: blue;"); // Set the button text color to blue
            updateButton.setOnAction(e -> updateTask(task)); // Set the action to update the task when clicked

            // Create a "DELETE" button for the task
            Button deleteButton = new Button("DELETE");
            deleteButton.setStyle("-fx-text-fill: red;"); // Set the button text color to red
            deleteButton.setOnAction(e -> deleteTask(task)); // Set the action to delete the task when clicked

            // Create a container for the task's buttons
            HBox taskButtons = new HBox(10, updateButton, deleteButton);

            // Create a container for the task's label and timer
            VBox taskInfo = new VBox(5, taskLabel, timerLabel);

            // Create a container to hold both the task's info and buttons
            HBox taskItem = new HBox(10, taskInfo, taskButtons);
            taskItem.setStyle("-fx-border-color: lightgray; -fx-padding: 10; -fx-border-radius: 5px;"); // Style the task container

            // Add the task item to the appropriate list based on its status
            if (task.getStatus().equals("Pending")) {
                tasksListPending.getChildren().add(taskItem);
            } else if (task.getStatus().equals("In-Progress")) {
                tasksListInProgress.getChildren().add(taskItem);
            } else if (task.getStatus().equals("Complete")) {
                tasksListComplete.getChildren().add(taskItem);
            }
        }
    }

    // Method to create a task item (UI component) for a given task
    private VBox createTaskItem(Task task) {
        // Create a new VBox to hold the task's UI components
        VBox taskPanel = new VBox();
        taskPanel.setStyle("-fx-border-color: lightgray; -fx-padding: 10; -fx-border-radius: 5px;"); // Style the task panel

        // Create a label to display the task's name and description
        Label taskDescription = new Label(task.getName() + "\n" + task.getDescription());
        taskDescription.setWrapText(true); // Set the label to wrap text if needed
        taskPanel.getChildren().add(taskDescription); // Add the label to the task panel

        // Create an "Update" button for the task
        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> updateTask(task)); // Set the action to update the task when clicked

        // Create a "Delete" button for the task
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> deleteTask(task)); // Set the action to delete the task when clicked

        // Create a container (HBox) to hold the update and delete buttons
        HBox buttonBox = new HBox(10, updateButton, deleteButton);
        taskPanel.getChildren().add(buttonBox); // Add the button container to the task panel

        // Create a label to display the task's timer (initially set to 0)
        Label timerLabel = new Label("Timer: 0s");
        taskPanel.getChildren().add(timerLabel); // Add the timer label to the task panel

        // Start the timer for the task
        startTimer(task, timerLabel);

        // Return the created task panel (VBox)
        return taskPanel;
    }

    // Method to set the task fields for editing
    private void updateTask(Task task) {
        // Set the task to be edited (this will be used later to update the task)
        taskToEdit = task;

        // Set the text fields with the current task's details for editing
        taskNameField.setText(task.getName()); // Set task name in the name field
        descriptionArea.setText(task.getDescription()); // Set task description in the description area
        taskForField.setText(task.getResponsibleUsers()); // Set responsible users in the task for field
        roleCombo.setValue(task.getRole()); // Set the role combo box to the task's role
        deadlinePicker.setValue(task.getDeadline()); // Set the deadline date picker to the task's deadline
        addDatePicker.setValue(task.getAddDate()); // Set the add date picker to the task's add date

        // Set the editing flag to true, indicating that the user is editing a task
        isEditing = true;
    }

    // Method to delete a task from the database and update the UI
    private void deleteTask(Task task) {
        try {
            // SQL query to delete the task based on its ID
            String sql = "DELETE FROM tasks WHERE id = ?";

            // Prepare the SQL statement to execute the query
            PreparedStatement pstmt = taskConn.prepareStatement(sql);

            // Set the task ID in the query
            pstmt.setInt(1, task.getId());

            // Execute the delete query
            pstmt.executeUpdate();

            // Show a confirmation alert that the task has been deleted successfully
            showAlert("Task Deleted", "The task has been deleted successfully.");

            // Remove the deleted task from both task lists (taskList and originalTaskList)
            taskList.remove(task);
            originalTaskList.remove(task);

            // Refresh the task display on the UI
            displayTasks();
        } catch (SQLException e) {
            // If an error occurs, print the stack trace
            e.printStackTrace();
        }
    }

    // Method to start a timer for a task and update the timer label every second
    private void startTimer(Task task, Label timerLabel) {
        // Create a Timeline to run the timer every second
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            // Calculate the remaining time for the task
            long remainingTime = calculateRemainingTime(task.getAddDate(), task.getDeadline());

            // Update the timer label with the formatted remaining time
            timerLabel.setText(formatTime(remainingTime));
        }));

        // Set the timeline to run indefinitely
        timeline.setCycleCount(Timeline.INDEFINITE);

        // Start the timeline (begin the timer)
        timeline.play();
    }

    // Method to calculate the remaining time (in seconds) between the current time and the task's deadline
    private long calculateRemainingTime(LocalDate addDate, LocalDate deadline) {
        // Convert the deadline to LocalDateTime with a time of 23:59:59 (end of day)
        LocalDateTime deadlineDateTime = deadline.atTime(23, 59, 59);

        // Get the current time as LocalDateTime
        LocalDateTime now = LocalDateTime.now();

        // Calculate the remaining time in seconds between now and the deadline
        long seconds = ChronoUnit.SECONDS.between(now, deadlineDateTime);

        // Return the remaining time, ensuring it's never negative (use Math.max)
        return Math.max(seconds, 0);
    }

// Method to format the remaining time (in seconds) into a readable string (days:hours:minutes)
    private String formatTime(long totalSeconds) {
        // Calculate the number of days
        long days = totalSeconds / (24 * 3600);

        // Calculate the number of hours
        long hours = (totalSeconds % (24 * 3600)) / 3600;

        // Calculate the number of minutes
        long minutes = (totalSeconds % 3600) / 60;

        // Return the formatted time as a string in the format: "DD:HH:MM"
        return String.format("%02d:%02d:%02d", days, hours, minutes);
    }

// Method to return a color for a button based on the task's status
    private Color getButtonColor(String status) {
        // Switch statement to determine the color based on the status of the task
        switch (status) {
            case "Pending": // If status is Pending, return magenta color
                return Color.MAGENTA;
            case "In-Progress": // If status is In-Progress, return blue color
                return Color.BLUE;
            case "Complete": // If status is Complete, return green color
                return Color.GREEN;
            default: // For any other status, return gray color
                return Color.GRAY;
        }
    }

// Method to handle the task button click event and update task status
    private void handleTaskButtonClick(VBox taskPanel, Task task, String buttonText) {
        // If the button text is "Accept", move the task to "In-Progress" and update the database
        if (buttonText.equals("Accept")) {
            moveTask(taskPanel, tasksListPending, tasksListInProgress, "In-Progress", Color.MAGENTA);
            updateTaskStatusInDatabase(task.getId(), "In-Progress");
        } // If the button text is "Complete", move the task to "Complete" and update the database
        else if (buttonText.equals("Complete")) {
            moveTask(taskPanel, tasksListInProgress, tasksListComplete, "Complete", Color.GREEN);
            updateTaskStatusInDatabase(task.getId(), "Complete");
        }
    }

// Method to move a task from one list (panel) to another and change the button color
    private void moveTask(VBox taskPanel, VBox fromPanel, VBox toPanel, String newStatus, Color buttonColor) {
        // Remove the task from the current panel (fromPanel)
        fromPanel.getChildren().remove(taskPanel);

        // Add the task to the new panel (toPanel)
        toPanel.getChildren().add(taskPanel);

        // Get the button from the task panel (assuming it's the second child of the task panel)
        Button button = (Button) taskPanel.getChildren().get(1);

        // Set the button's text color based on the task's new status
        button.setTextFill(buttonColor);
    }

    // Method to update the status of a task in the database based on taskId
    private void updateTaskStatusInDatabase(int taskId, String newStatus) {
        try {
            // SQL query to update the status of the task in the database where the task ID matches
            String sql = "UPDATE tasks SET status = ? WHERE id = ?";

            // Prepare the SQL statement to execute the update
            PreparedStatement pstmt = taskConn.prepareStatement(sql);

            // Set the new status parameter in the query
            pstmt.setString(1, newStatus);

            // Set the task ID parameter in the query
            pstmt.setInt(2, taskId);

            // Execute the update statement
            pstmt.executeUpdate();

            // Print a log message confirming the task's status has been updated
            System.out.println("Task ID " + taskId + " status updated to " + newStatus);
        } catch (SQLException e) {
            // If an error occurs, print the stack trace
            e.printStackTrace();
        }
    }

    // Method to retrieve a list of tasks from the database
    private List<Task> getTasksFromDatabase() {
        // Create an empty list to store tasks
        List<Task> tasks = new ArrayList<>();

        try {
            // SQL query to select all tasks from the database
            String sql = "SELECT * FROM tasks";

            // Create a Statement object to execute the SQL query
            Statement stmt = taskConn.createStatement();

            // Execute the query and get the result set
            ResultSet rs = stmt.executeQuery(sql);

            // Loop through the result set and create Task objects
            while (rs.next()) {
                // Retrieve each task's attributes from the result set
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                String status = rs.getString("status");
                String assignedTo = rs.getString("assignedTo");
                String role = rs.getString("role");
                // Convert deadline and addDate from epoch seconds to LocalDate
                LocalDate deadline = LocalDateTime.ofEpochSecond(rs.getLong("deadline") / 1000, 0, ZoneOffset.UTC).toLocalDate();
                LocalDate addDate = LocalDateTime.ofEpochSecond(rs.getLong("addDate") / 1000, 0, ZoneOffset.UTC).toLocalDate();

                // Create a Task object with the retrieved data
                Task task = new Task(id, name, description, status, assignedTo, role, deadline, addDate) {
                    @Override
                    public String getType() {
                        // Throw an exception for the unsupported method (not relevant for this example)
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                };

                // Add the task to the list if it doesn't already exist in the list
                if (!tasks.contains(task)) {
                    tasks.add(task);
                    // Log the task retrieval
                    System.out.println("Task retrieved: " + name + ", Status: " + status);
                }
            }
        } catch (SQLException e) {
            // If an error occurs, print the stack trace
            e.printStackTrace();
        }

        // Return the list of tasks retrieved from the database
        return tasks;
    }

    // Method to search for tasks in the original task list based on the search text
    private void searchTasks(String searchText) {
        // Clear the current task list
        taskList.clear();

        // Iterate through the original task list
        for (Task task : originalTaskList) {
            // If the task name contains the search text (case insensitive), add it to the task list
            if (task.getName().toLowerCase().contains(searchText.toLowerCase())) {
                taskList.add(task);
            }
        }

        // Display the filtered tasks
        displayTasks();
    }

    // Method to show an information alert with a title and message
    private void showAlert(String title, String message) {
        // Create an alert of type INFORMATION
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // Set the title of the alert
        alert.setTitle(title);

        // Set the header text (null, as no header is needed)
        alert.setHeaderText(null);

        // Set the content text to display the message
        alert.setContentText(message);

        // Show the alert and wait for the user to close it
        alert.showAndWait();
    }

    // Method to save a new task to the database
    private void saveTaskToDatabase(Task task) {
        try {
            // SQL query to insert a new task into the database
            String sql = "INSERT INTO tasks (taskName, description, status, addDate, deadline) VALUES (?, ?, ?, ?, ?)";

            // Prepare the SQL statement to execute the insertion
            PreparedStatement pstmt = taskConn.prepareStatement(sql);

            // Set the task's attributes in the SQL query
            pstmt.setString(1, task.getName());
            pstmt.setString(2, task.getDescription());
            pstmt.setString(3, task.getStatus());
            pstmt.setDate(4, Date.valueOf(task.getAddDate())); // Convert LocalDate to java.sql.Date
            pstmt.setDate(5, Date.valueOf(task.getDeadline())); // Convert LocalDate to java.sql.Date

            // Execute the insert query to save the task to the database
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // If an error occurs, print the stack trace
            e.printStackTrace();
        }
    }

    // Override method to close the database connection when the application stops
    @Override
    public void stop() {
        try {
            // If the taskConn (database connection) is not null and is open, close it
            if (taskConn != null && !taskConn.isClosed()) {
                taskConn.close();
            }
        } catch (SQLException e) {
            // If an error occurs while closing the connection, print the stack trace
            e.printStackTrace();
        }
    }
}