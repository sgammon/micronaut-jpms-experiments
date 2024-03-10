package org.sample.app;

import org.sample.lib.ModularLibClass;
import org.sample.lib.ResourceProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.concurrent.Callable;

public class ModularApp {
    private static void check(String name, Object any) {
        Objects.requireNonNull(any, "object was null: " + name);
    }

    private static String protect(Callable<String> callable) {
        try {
            return callable.call();
        } catch (Exception err) {
            return "ERR: " + err.getClass().getName() + " " + err.getMessage();
        }
    }

    private static String readResource(String name) {
        try (var stream = ModularApp.class.getResourceAsStream(name)) {
            check(name, stream);

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
            check(name, stream);

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
            check(name, stream);

            try (var buf = new BufferedReader(new InputStreamReader(stream))) {
                return buf.readLine();
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    private static String readResourceWithTrampoline(String name) {
        try (var stream = org.sample.lib.ResourceProvider.getResource(name)) {
            check(name, stream);

            try (var buf = new BufferedReader(new InputStreamReader(stream))) {
                return buf.readLine();
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    public static void main(String[] args) {
        System.out.println("Hello, modular Java! " + ModularLibClass.getMessage());

        // normal access
        System.out.println("App resource: " + protect(() -> readResource("/appmodule.txt")));
        System.out.println("Lib resource: " + protect(() -> readResource("/libmodule.txt")));

        // modular access
        System.out.println("Lib resource (M): " + protect(() -> readResourceFromModule(
            ModularLibClass.class.getModule(),
            "/META-INF/micronaut/libmodule.txt"
        )));

        // this module access
        System.out.println("Lib resource (T): " + protect(() -> readResourceThisModule(
            "/META-INF/micronaut/libmodule.txt"
        )));

        // trampoline module access
        System.out.println("Lib resource (R): " + protect(() -> readResourceWithTrampoline(
                "/META-INF/micronaut/libmodule.txt"
        )));
    }
}
