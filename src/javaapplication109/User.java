package javaapplication109;

// The User class is an abstract class representing a user in the system
public abstract class User {
    protected String name;  // The name of the user

    // Constructor to initialize the user with a name
    public User(String name) {
        this.name = name;  // Sets the name of the user
    }

    // Getter method to retrieve the name of the user
    public String getName() {
        return name;  // Returns the name of the user
    }

    // Abstract method that must be implemented by subclasses to return the user's role
    public abstract String getRole();
}
