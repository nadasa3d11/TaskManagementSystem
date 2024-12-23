package javaapplication109;

// The TaskFactory class is responsible for creating different types of tasks based on the provided task type
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TaskFactory {
    // Static method to create a task of the specified type
    public static Task createTask(String taskType, String name, String description, LocalDate addDate, LocalDate deadline, String responsibleUsers) {
        // Switch statement to determine which type of task to create based on the taskType string
        switch (taskType.toLowerCase()) {
            case "bug":
                // Creates and returns a BugTask if the task type is "bug"
                return new BugTask(name, description, addDate, deadline, responsibleUsers);
            case "feature":
                // Creates and returns a FeatureTask if the task type is "feature"
                return new FeatureTask(name, description, addDate, deadline, responsibleUsers);
            case "improvement":
                // Creates and returns an ImprovementTask if the task type is "improvement"
                return new ImprovementTask(name, description, addDate, deadline, responsibleUsers);
            default:
                // Throws an exception if the task type is unknown
                throw new IllegalArgumentException("Unknown task type: " + taskType);
        }
    }
}

