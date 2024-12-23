package javaapplication109;
// The UserFactory class is responsible for creating users based on their role
public class UserFactory {
    // Static method to create a user of the specified role and name
    public static User createUser(String role, String name) {
        // Switch statement to determine which type of user to create based on the provided role
        switch (role.toLowerCase()) {
            case "accountant":
                // Creates and returns an Accountant if the role is "accountant"
                return new Accountant(name);
            
            case "developer":
                // Creates and returns a Developer if the role is "developer"
                return new Developer(name);
            case "manager":
                // Creates and returns a Manager if the role is "manager"
                return new Manager(name);
            case "designer":
                // Creates and returns a Designer if the role is "designer"
                return new Designer(name);    
            default:
                // Throws an exception if the role is unknown
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }
}