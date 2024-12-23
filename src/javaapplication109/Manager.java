package javaapplication109;

// The Manager class extends the User class
public class Manager extends User {

    // Constructor for Manager class that takes name as parameter and passes it to the parent class (User) constructor
    public Manager(String name) {
        super(name);  // Calls the parent class constructor with the name argument
    }

    // Override the getRole method to return the role of the user as "Manager"
    @Override
    public String getRole() {
        return "Manager";  // Returns the role as "Manager"
    }
}
