plugins {
    java
    application
    jacoco
    id("com.gradleup.shadow") version "9.0.0-beta4"
}

group = "edu.luc.cs"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.4")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.11.4")
}

application {
    mainClass.set("hw.Main")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

jacoco {
    toolVersion = "0.8.12"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it) {
                exclude("**/*Main.*")
            }
        })
    )
}

// Configure shadow JAR for standalone execution
tasks.shadowJar {
    archiveBaseName.set(project.name)
    archiveClassifier.set("all")
    archiveVersion.set(project.version.toString())
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
}

// Add task aliases for Maven users
tasks.register("package") {
    dependsOn(tasks.shadowJar)
    group = "build"
    description = "Creates a standalone executable JAR (alias for shadowJar)"
}

tasks.register<JavaExec>("exec") {
    group = "application"
    description = "Executes the main class (alias for run)"
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set(application.mainClass)
}
