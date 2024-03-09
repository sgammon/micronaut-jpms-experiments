package org.sample.app;

import org.sample.lib.ModularLibClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ModularApp {
    private static String readResource(String name) {
        try (var stream = ModularApp.class.getResourceAsStream(name)) {
            assert stream != null;

            try (var buf = new BufferedReader(new InputStreamReader(stream))) {
                return buf.readLine();
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    public static void main(String[] args) {
        System.out.println("Hello, modular Java! " + ModularLibClass.getMessage());
        System.out.println("App resource: " + readResource("/appmodule.txt"));
        System.out.println("App resource: " + readResource("/libmodule.txt"));
    }
}
