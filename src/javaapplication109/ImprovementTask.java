package javaapplication109;
// The ImprovementTask class extends the Task class, representing a specific type of task (Improvement)
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ImprovementTask extends Task {

    // Constructor for ImprovementTask class that initializes the task properties using the parent class constructor
    public ImprovementTask(String name, String description, LocalDate addDate, LocalDate deadline, String responsibleUsers) {
        super(name, description, addDate, deadline, responsibleUsers);  // Calls the parent class constructor to initialize common task attributes
    }

    // Override the getType method to return the type of task as "Improvement"
    @Override
    public String getType() {
        return "Improvement";  // Returns the type of task as "Improvement"
  }
}
  