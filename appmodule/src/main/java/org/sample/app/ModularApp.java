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

    private static String readResourceFromModule(Module base, String name) {
        var thisModule = ModularApp.class.getModule();
        assert base.isNamed();
        assert base.getName().equals("demo.libmodule");
        assert base.isExported("org.sample.lib");
        assert base.canRead(thisModule);

        try (var stream = thisModule.getResourceAsStream(name)) {
            assert stream != null;

            try (var buf = new BufferedReader(new InputStreamReader(stream))) {
                return buf.readLine();
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    private static String readResourceThisModule(String name) {
        var thisModule = ModularApp.class.getModule();

        try (var stream = thisModule.getResourceAsStream(name)) {
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
        System.out.println("Lib resource: " + readResource("/libmodule.txt"));
        System.out.println("App resource (M): " + readResourceFromModule(
            ModularLibClass.class.getModule(),
            "/META-INF/micronaut/appmodule.txt"
        ));
        System.out.println("Lib resource (M): " + readResourceFromModule(
            ModularLibClass.class.getModule(),
            "/META-INF/micronaut/libmodule.txt"
        ));
        System.out.println("App resource (T): " + readResourceThisModule(
            "/META-INF/micronaut/appmodule.txt"
        ));
        System.out.println("Lib resource (T): " + readResourceThisModule(
            "/META-INF/micronaut/libmodule.txt"
        ));
    }
}
