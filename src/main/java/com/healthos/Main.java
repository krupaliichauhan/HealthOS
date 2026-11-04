package com.healthos;

/**
 * Standard Main Class Entry Point.
 * Dispatches to HealthOSApp to bypass JavaFX module runtime restrictions
 * when executing in diverse IDE configurations and packaged JARs.
 */
public class Main {
    public static void main(String[] args) {
        HealthOSApp.main(args);
    }
}
