plugins {
    application
    id("java")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "se"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    // Apply a specific Java toolchain to ease working on different environments.
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    // Define the main class for the application.
    mainClass = "Main"
}
javafx {
    version = "22.0.1"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.media")
}
dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    // Jackson for JSON and YAML support
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.15.2") // Optional
}


tasks.test {
    useJUnitPlatform()
}