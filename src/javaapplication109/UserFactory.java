/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication109;
public class UserFactory {
    public static User createUser(String role, String name) {
        switch (role.toLowerCase()) {
            case "accountant":
                return new Accountant(name);
            case "developer":
                return new Developer(name);
            case "manager":
                return new Manager(name);
            case "designer":
                return new Designer(name);
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }
}
