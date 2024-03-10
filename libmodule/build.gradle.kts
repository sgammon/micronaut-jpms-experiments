plugins {
    java
    `java-library`
    id("io.micronaut.library") version "4.3.4"
}

version = "0.1"
group = "com.example"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.toVersion("21")
}
