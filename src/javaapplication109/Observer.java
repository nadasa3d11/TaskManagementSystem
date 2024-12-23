package javaapplication109;
// The Observer interface defines a contract for classes that will receive notifications
public interface Observer {

    // The update method is called to notify the observer with a message
    void update(String message);  // Accepts a message and updates the observer with that message
}

