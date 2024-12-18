/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package library.management.system.operationState;

/**
 *
 * @author AG3
 */
public class OperationContext {
    private OperationState currentState;

    // Set the current state
    public void setState(OperationState state) {
        this.currentState = state;
    }

    // Execute the current state's action
    public void execute() {
        if (currentState != null) {
            currentState.execute();
        } else {
            System.out.println("No state set.");
        }
    }
}
