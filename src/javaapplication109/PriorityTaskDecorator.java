/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication109;

public class PriorityTaskDecorator extends TaskDecorator {
    private String priority;

    public PriorityTaskDecorator(Task task, String priority) {
        super(task);
        this.priority = priority;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " (Priority: " + priority + ")";
    }

    @Override
    public String getType() {
        return "";
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
