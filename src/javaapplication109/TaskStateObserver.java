package javaapplication109;
public class TaskStateObserver implements Observer {
    // This method will be called whenever the subject (Observable) notifies observers
    @Override
    public void update(String message) {
        // Check if the message contains information about task state change
        if (message.contains("Task status changed")) {
            System.out.println("Task state has changed. Taking necessary action.");
            // Implement any necessary logic to handle task state changes, e.g., updating UI or internal state
        }
    }
}
