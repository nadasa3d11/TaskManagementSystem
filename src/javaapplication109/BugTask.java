package javaapplication109;
// The BugTask class extends the Task class, representing a specific type of task (Bug)
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class BugTask extends Task {

    // Constructor for BugTask class that initializes the task properties using the parent class constructor
    public BugTask(String name, String description, LocalDate addDate, LocalDate deadline, String responsibleUsers) {
        super(name, description, addDate, deadline, responsibleUsers);  // Calls the parent class constructor to initialize common task attributes
    }

    // Override the getType method to return the type of task as "Bug"
    @Override
    public String getType() {
        return "Bug";  // Returns the type of task as "Bug"
    }
}
