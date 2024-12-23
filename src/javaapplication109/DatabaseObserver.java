package javaapplication109;
public class DatabaseObserver implements Observer {
    // This method executes whenever there's a change in task status (task added or removed)
    @Override
    public void update(String message) {
        // Check if the message indicates that a task has been added or removed
        if (message.contains("Task Added") || message.contains("Task Removed")) {
            updateDatabase(message);  // Call the method to update the database based on the event
        }
    }

    // Method to handle database updates based on task changes
    private void updateDatabase(String message) {
        // Implement logic to interact with the database, e.g., updating records or logging the action
        System.out.println("Database updated with: " + message);
    }
}