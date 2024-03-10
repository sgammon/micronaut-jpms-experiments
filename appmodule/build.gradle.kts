plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.micronaut.application") version "4.3.4"
    id("io.micronaut.aot") version "4.3.4"
}

version = "0.1"
group = "com.example"

val entrypoint = "org.sample.app.ModularApp"
val entrymodule = "demo.appmodule"
val kotlinVersion = project.properties.get("kotlinVersion")

repositories {
    mavenCentral()
}

application {
    mainClass = entrypoint
    mainModule = entrymodule
}

dependencies {
    annotationProcessor(mn.micronaut.inject.java)
    implementation(projects.libmodule)
}

tasks.jar {
    doLast {
        project.exec {
            workingDir = destinationDirectory.get().asFile
            executable = "jar"
            args(
                "--update",
                "--file",
                archiveFileName.get(),
                "--main-class",
                entrypoint,
                "."
            )
        }
    }
}

tasks.register("runModular", Exec::class) {
    executable("java")
    dependsOn(tasks.jar)
    mustRunAfter(tasks.jar)
    inputs.files(tasks.jar.get().outputs)

    args(
        "--module-path",
        (configurations.runtimeClasspath.get().asPath + ":" + tasks.jar.get().outputs.files.asPath),
        "--module",
        "$entrymodule/$entrypoint",
    )
}

tasks.named("run", JavaExec::class.java) {

}

java {
    sourceCompatibility = JavaVersion.toVersion("21")
}

graalvmNative.toolchainDetection.set(false)

micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("com.example.*")
    }
    aot {
    // Please review carefully the optimizations enabled below
    // Check https://micronaut-projects.github.io/micronaut-aot/latest/guide/ for more details
        optimizeServiceLoading.set(false)
        convertYamlToJava.set(false)
        precomputeOperations.set(true)
        cacheEnvironment.set(true)
        optimizeClassLoading.set(true)
        deduceEnvironment.set(true)
        optimizeNetty.set(true)
    }
}

tasks.named<io.micronaut.gradle.docker.MicronautDockerfile>("dockerfile") {
    baseImage("eclipse-temurin:21-jre-jammy")
}

tasks.named<io.micronaut.gradle.docker.NativeImageDockerfile>("dockerfileNative") {
    jdkVersion.set("21")
}
