// Fail fast with a clear message if the JDK running Gradle is too old.
// Prevents silent misbehavior from incompatible toolchain versions.
require(JavaVersion.current() >= JavaVersion.VERSION_21) {
    "This project requires Java 21 or newer (found ${JavaVersion.current()})"
}

// Plugins extend the build with additional tasks and conventions.
// Built-in plugins (java, application, jacoco, pmd) need no version.
// Third-party plugins require an explicit version from the Gradle Plugin Portal.
// Note: Spotless and testlogger are also available globally via ~/.gradle/init.d/
// but defer to project-level declarations when present (requires afterEvaluate guard in init.d).
plugins {
    java                                             // compile, test, jar
    application                                      // adds 'run' task and mainClass support
    jacoco                                           // code coverage via JaCoCo
    pmd                                              // static analysis via PMD
    id("com.diffplug.spotless") version "8.4.0"      // code formatting via Spotless
    id("com.adarshr.test-logger") version "4.0.0"    // pretty test output via testlogger
    id("com.gradleup.shadow") version "9.4.1"        // fat/uber JAR with all dependencies
}

group = "edu.luc.cs"
version = "1.0-SNAPSHOT"

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

// Configure the test task to use the JUnit 5 platform (JUnit Jupiter).
// finalizedBy ensures the coverage report is always generated after tests run,
// even if some tests fail.
tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

jacoco {
    toolVersion = "0.8.13"
}

// Generate XML (for CI tools) and HTML (for humans) coverage reports.
// Main is excluded from coverage metrics because it is just an entry point
// with no logic worth measuring.
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

// Spotless enforces consistent code formatting.
// spotlessCheck (runs with 'check') verifies formatting; spotlessApply fixes it in place.
// Google Java Format enforces Google style (2-space indent, import ordering, etc.).
spotless {
    java {
        googleJavaFormat()  // enforces Google Java style (2-space indent, etc.)
        removeUnusedImports()
        trimTrailingWhitespace()
        endWithNewline()
    }
}

// Static analysis with PMD's built-in rule categories.
// bestpractices: common coding pitfalls; errorprone: patterns likely to cause bugs.
// rulesMinimumPriority = 5 reports all severity levels (1 = critical, 5 = low).
// isConsoleOutput prints violations directly to the build output.
pmd {
    toolVersion = "7.24.0"
    isConsoleOutput = true
    rulesMinimumPriority.set(5)
    ruleSets = listOf("rulesets/java/quickstart.xml")
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
