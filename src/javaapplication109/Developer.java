/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication109;

public class Developer extends User {
    public Developer(String name) {
        super(name);
    }

    @Override
    public String getRole() {
        return "Developer";
    }
}