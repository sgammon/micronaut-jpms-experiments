# Micronaut JPMS Experiments

Quick experiments for using Micronaut under JPMS. Runnable with:

```
./gradlew :appmodule:run
```

Expected output:
```
Type-safe project accessors is an incubating feature.

> Task :appmodule:runModular
Hello, modular Java! Hello, Modular Lib!
App resource: This is an app module resource
Lib resource: ERR: java.lang.NullPointerException object was null: /libmodule.txt
Lib resource (M): ERR: java.lang.NullPointerException object was null: /META-INF/micronaut/libmodule.txt
Lib resource (T): ERR: java.lang.NullPointerException object was null: /META-INF/micronaut/libmodule.txt
Lib resource (R): This is a modular resource

BUILD SUCCESSFUL in 1s
11 actionable tasks: 3 executed, 8 up-to-date
```

There are two JPMS modules: `demo.libmodule` and `demo.appmodule`. The "app module" depends on the "lib module."

`App resource` / `Lib resource` show:
```java
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
```

### Modular access

`(M)` shows:
```java
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
```

### "This module" access

`(T)` shows:
```java
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
```

### Trampoline resource access

`(R)` shows:
```java
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
```

Where `org.sample.lib.ResourceProvider` is an exported class within `demo.libmodule`:

```java
public class ResourceProvider {
    public static InputStream getResource(String name) throws IOException {
        return ResourceProvider.class.getModule().getResourceAsStream(name);
    }
}
```

## What does this project show?

**1) Cross-module use of resources**

Experiments for loading resources across Java module boundaries, which will be needed in order to get Micronaut's DI
container and injection tools working properly.

**2) Cross-module use of services**

Experiments for Micronaut's use of Service Loader under JPMS. (Coming soon)

**3) Options for addressing the split-package issue at build time**

(Coming soon)

----

## Micronaut 4.3.5 Documentation

- [User Guide](https://docs.micronaut.io/4.3.5/guide/index.html)
- [API Reference](https://docs.micronaut.io/4.3.5/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/4.3.5/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
---

- [Micronaut Gradle Plugin documentation](https://micronaut-projects.github.io/micronaut-gradle-plugin/latest/)
- [GraalVM Gradle Plugin documentation](https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html)
- [Shadow Gradle Plugin](https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow)
## Feature buildless documentation

- [https://docs.less.build/](https://docs.less.build/)


## Feature ksp documentation

- [Micronaut Kotlin Symbol Processing (KSP) documentation](https://docs.micronaut.io/latest/guide/#kotlin)

- [https://kotlinlang.org/docs/ksp-overview.html](https://kotlinlang.org/docs/ksp-overview.html)


## Feature serialization-jackson documentation

- [Micronaut Serialization Jackson Core documentation](https://micronaut-projects.github.io/micronaut-serialization/latest/guide/)


## Feature micronaut-aot documentation

- [Micronaut AOT documentation](https://micronaut-projects.github.io/micronaut-aot/latest/guide/)


