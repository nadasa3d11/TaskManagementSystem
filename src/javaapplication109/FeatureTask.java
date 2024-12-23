package javaapplication109;
// The FeatureTask class extends the Task class, representing a specific type of task (Feature)
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class FeatureTask extends Task {

    // Constructor for FeatureTask class that initializes the task properties using the parent class constructor
    public FeatureTask(String name, String description, LocalDate addDate, LocalDate deadline, String responsibleUsers) {
        super(name, description, addDate, deadline, responsibleUsers);  // Calls the parent class constructor to initialize common task attributes
    }

    // Override the getType method to return the type of task as "Feature"
    @Override
    public String getType() {
        return "Feature";  // Returns the type of task as "Feature"
    }
}
