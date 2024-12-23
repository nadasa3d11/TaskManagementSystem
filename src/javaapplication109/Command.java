package javaapplication109;
// The Command interface defines a contract for classes that implement the command pattern
public interface Command {

    // The execute method is defined in the interface, to be implemented by concrete command classes
    void execute();  // Executes the specific command defined in the implementing class
}
