package javaapplication109;
// The Designer class extends the User class
public class Designer extends User {

    // Constructor for Designer class that takes name as parameter and passes it to the parent class (User) constructor
    public Designer(String name) {
        super(name);  // Calls the parent class constructor with the name argument
    }

    // Override the getRole method to return the role of the user as "Designer"
    @Override
    public String getRole() {
        return "Designer";  // Returns the role as "Designer"
    }
}
