package javaapplication109;

// The Task class is an abstract class that represents a task with its attributes and methods
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public abstract class Task {
    private String name;  // Name of the task
    private String description;  // Description of the task
    private LocalDate addDate;  // The date the task was added
    private LocalDate deadline;  // The deadline for the task
    private String status;  // The status of the task (e.g., Pending, Completed)
    private String responsibleUsers;  // Users responsible for the task
    private String role;  // Role associated with the task
    private int id;  // Unique identifier for the task
    
    public Task(){};
    // Getter for task ID
    public int getId() {
        return id;  // Returns the ID of the task
    }

    // Setter for task ID
    public void setId(int id) {
        this.id = id;  // Sets the ID of the task
    }

    // Constructor that initializes a task with a name, description, add date, deadline, and responsible users
    public Task(String name, String description, LocalDate addDate, LocalDate deadline, String responsibleUsers) {
        this.name = name;
        this.description = description;
        this.addDate = addDate;
        this.deadline = deadline;
        this.responsibleUsers = responsibleUsers;
        this.status = "Pending";  // Default status is "Pending"
    }

    // Constructor with additional parameters for task ID, role, and status
    Task(int id, String taskName, String description, String status, String taskFor, String role, LocalDate deadline, LocalDate addDate) {
        this.name = taskName;
        this.description = description;
        this.addDate = addDate;
        this.deadline = deadline;
        this.responsibleUsers = taskFor;
        this.status = status;  // Can be set to a custom status
        this.id = id;
        this.role = role;
    }
    
   public Task(String name) {
    this.responsibleUsers = name;
}

    // Getter for the role of the task
    public String getRole() {
        return role;  // Returns the role associated with the task
    }

    // Setter for the role of the task
    public void setRole(String role) {
        this.role = role;  // Sets the role of the task
    }

    // Getter for task name
    public String getName() {
        return name;  // Returns the name of the task
    }

    // Getter for task description
    public String getDescription() {
        return description;  // Returns the description of the task
    }

    // Getter for the add date of the task
    public LocalDate getAddDate() {
        return addDate;  // Returns the add date of the task
    }

    // Setter for task name
    public void setName(String name) {
        this.name = name;  // Sets the name of the task
    }

    // Setter for task description
    public void setDescription(String description) {
        this.description = description;  // Sets the description of the task
    }

    // Setter for the add date of the task
    public void setAddDate(LocalDate addDate) {
        this.addDate = addDate;  // Sets the add date of the task
    }

    // Setter for the deadline of the task
    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;  // Sets the deadline of the task
    }

    // Setter for responsible users of the task
    public void setResponsibleUsers(String responsibleUsers) {
        this.responsibleUsers = responsibleUsers;  // Sets the responsible users of the task
    }

    // Getter for task deadline
    public LocalDate getDeadline() {
        return deadline;  // Returns the deadline of the task
    }

    // Getter for task status
    public String getStatus() {
        return status;  // Returns the status of the task
    }

    // Getter for responsible users of the task
    public String getResponsibleUsers() {
        return responsibleUsers;  // Returns the responsible users of the task
    }

    // Setter for task status
    public void setStatus(String status) {
        this.status = status;  // Sets the status of the task
    }

    // Abstract method to get the type of the task (to be implemented by subclasses)
    public abstract String getType();
}
