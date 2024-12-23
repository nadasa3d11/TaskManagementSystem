package javaapplication109;
// The Accountant class extends the User class
public class Accountant extends User {

    // Constructor for Accountant class that takes name as parameter and passes it to the parent class (User) constructor
    public Accountant(String name) {
        super(name);  // Calls the parent class constructor with the name argument
    }

    // Override the getRole method to return the role of the user as "Accountant"
    @Override
    public String getRole() {
        return "Accountant";  // Returns the role as "Accountant"
    }
}
