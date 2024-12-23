package javaapplication109;
// The Developer class extends the User class
public class Developer extends User {

    // Constructor for Developer class that takes name as parameter and passes it to the parent class (User) constructor
    public Developer(String name) {
        super(name);  // Calls the parent class constructor with the name argument
    }

    // Override the getRole method to return the role of the user as "Developer"
    @Override
    public String getRole() {
        return "Developer";  // Returns the role as "Developer"
    }
}
