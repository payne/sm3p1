
package org.mattpayne.learning.sm3p1;

public enum OrderState {
    PLACED("PLACED"),
    PROCESSING("PROCESSING"),PROCESS("PROCESS"),
    SENT("SENT"),SEND("SEND"),
    DELIVERED("DELIVERED"),DELIVER("DELIVER"),
    SIMPLE("simpleStateMachine"),
    PERSIST("persistStateMachineHandler");

    private String label;

    OrderState(String label) {
        this.label = label;
    }

    public String toString() {
      return label;
    }
}