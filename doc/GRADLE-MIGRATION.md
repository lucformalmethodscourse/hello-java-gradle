# Gradle Migration Documentation

## Overview

This project has been migrated from Apache Maven to Gradle, utilizing the current stable release of Gradle (9.3.0). The migration maintains all functionality while adding enhanced capabilities for standalone application packaging.

## Version Information

- **Gradle Version**: 9.3.0 (current stable release as of January 2026)
- **Java Version**: 21 (using Java Toolchain)
- **Build Tool**: Gradle with Wrapper (platform-independent)

## Migration Details

### Build Files Created

1. **build.gradle** - Main build configuration file (replaces pom.xml)
2. **settings.gradle** - Project settings and name configuration
3. **gradlew / gradlew.bat** - Gradle wrapper scripts for Unix/Windows
4. **gradle/wrapper/** - Gradle wrapper JAR and properties

### Dependencies Migrated

All Maven dependencies have been successfully migrated to Gradle format:

| Dependency | Version | Scope | Notes |
|------------|---------|-------|-------|
| junit-jupiter-api | 5.11.4 | test | JUnit 5 API for writing tests |
| junit-jupiter-engine | 5.11.4 | testRuntime | JUnit 5 test execution engine |
| junit-platform-launcher | 1.11.4 | testRuntime | Required for Gradle 9.x JUnit integration |

### Plugins Configured

1. **java** - Core Java compilation and build support
2. **application** - Provides `run` task and application packaging
3. **jacoco** - Code coverage reporting (version 0.8.12)
4. **shadow** - Creates standalone executable fat JARs (version 9.0.0-beta4)

### Build Configuration

#### Java Toolchain
The project uses Gradle's Java Toolchain feature to ensure Java 21 is used consistently:
```gradle
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}
```

#### Application Configuration
Main class is configured for execution:
```gradle
application {
    mainClass = 'hw.Main'
}
```

#### JaCoCo Coverage
Code coverage is configured to:
- Run automatically after tests
- Exclude `Main.java` from coverage metrics (pedagogical reasons)
- Generate both XML and HTML reports

## Standalone Execution

### Shadow Plugin Integration

The project now uses the Shadow plugin (formerly known as shadow-jar) to create standalone executable JARs. This plugin:
- Bundles all dependencies into a single JAR file
- Configures the manifest with the correct main class
- Creates a "fat JAR" that can run anywhere with Java installed

### Generated Artifact

Running `./gradlew shadowJar` produces:
```
build/libs/hello-java-1.0-SNAPSHOT-all.jar
```

This JAR can be executed standalone:
```bash
java -jar build/libs/hello-java-1.0-SNAPSHOT-all.jar [args...]
```

**Example:**
```bash
$ java -jar build/libs/hello-java-1.0-SNAPSHOT-all.jar test arg1 arg2
args = [test, arg1, arg2]
hello world
2024
bye for now
```

## Command Reference

### Maven vs Gradle Command Mapping

| Maven Command | Gradle Equivalent | Description |
|---------------|-------------------|-------------|
| `mvn clean` | `./gradlew clean` | Clean build artifacts |
| `mvn compile` | `./gradlew compileJava` | Compile source code |
| `mvn test` | `./gradlew test` | Run tests |
| `mvn package` | `./gradlew build` or `./gradlew package` | Build project |
| `mvn verify` | `./gradlew check` | Run verification tasks |
| `mvn exec:java` | `./gradlew run` or `./gradlew exec` | Execute main class |
| N/A | `./gradlew shadowJar` | Create standalone fat JAR |

### Common Gradle Commands

#### Build Commands
```bash
# Clean and build the project
./gradlew clean build

# Build without tests
./gradlew build -x test

# Create standalone JAR
./gradlew shadowJar

# Alias for shadowJar (Maven-style)
./gradlew package
```

#### Execution Commands
```bash
# Run the application
./gradlew run

# Run with arguments
./gradlew run --args="arg1 arg2 arg3"

# Alias for run (Maven-style)
./gradlew exec
```

#### Testing Commands
```bash
# Run all tests
./gradlew test

# Run tests with coverage report
./gradlew test jacocoTestReport

# Run specific test class
./gradlew test --tests TestHelloWorld

# Run tests continuously (on file changes)
./gradlew test --continuous
```

#### Dependency Commands
```bash
# View dependency tree
./gradlew dependencies

# View test dependencies only
./gradlew dependencies --configuration testRuntimeClasspath

# Check for dependency updates
./gradlew dependencyUpdates
```

#### Information Commands
```bash
# List all tasks
./gradlew tasks

# List all tasks including detailed descriptions
./gradlew tasks --all

# View project properties
./gradlew properties
```

## Project Structure

The source directory structure remains unchanged from Maven:

```
hello-java-maven/
├── build.gradle              # Gradle build configuration
├── settings.gradle            # Gradle project settings
├── gradlew                    # Unix Gradle wrapper script
├── gradlew.bat               # Windows Gradle wrapper script
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── src/
│   ├── main/
│   │   └── java/
│   │       └── hw/
│   │           ├── HelloWorld.java
│   │           └── Main.java
│   └── test/
│       └── java/
│           └── hw/
│               └── TestHelloWorld.java
└── build/                    # Gradle build output (gitignored)
    ├── classes/
    ├── libs/                 # Contains generated JARs
    ├── reports/              # Test and coverage reports
    └── test-results/
```

## Build Output Locations

| Artifact Type | Maven Location | Gradle Location |
|---------------|----------------|-----------------|
| Compiled classes | `target/classes/` | `build/classes/java/main/` |
| Test classes | `target/test-classes/` | `build/classes/java/test/` |
| JAR files | `target/*.jar` | `build/libs/*.jar` |
| Test reports | `target/surefire-reports/` | `build/reports/tests/test/` |
| Coverage reports | `target/site/jacoco/` | `build/reports/jacoco/test/` |

## Advanced Features

### Configuration Cache

Gradle 9.x supports configuration cache for faster builds. Enable it by adding to `gradle.properties`:
```properties
org.gradle.configuration-cache=true
```

### Build Scans

Generate detailed build analysis:
```bash
./gradlew build --scan
```

### Parallel Execution

Enable parallel test execution in `build.gradle`:
```gradle
test {
    maxParallelForks = Runtime.runtime.availableProcessors().intdiv(2) ?: 1
}
```

### Custom Tasks

Two alias tasks have been added for Maven users:
- **package** - Calls `shadowJar` (Maven-style packaging)
- **exec** - Calls `run` (Maven-style execution)

## Migration Benefits

1. **Faster Builds**: Gradle's incremental compilation and build cache
2. **Better Dependency Management**: Transitive dependency resolution
3. **Flexible Scripting**: Groovy/Kotlin DSL for complex build logic
4. **Rich Plugin Ecosystem**: Easy integration of new build tools
5. **Standalone Packaging**: Built-in support for fat JAR creation
6. **Modern Tooling**: Active development and Java 21+ support

## Troubleshooting

### Gradle Daemon Issues
```bash
# Stop all Gradle daemons
./gradlew --stop

# Check daemon status
./gradlew --status
```

### Clean Build
```bash
# Force clean build
./gradlew clean build --no-build-cache
```

### Wrapper Update
```bash
# Update to latest Gradle version
./gradlew wrapper --gradle-version=9.3.0
```

### Dependency Conflicts
```bash
# View dependency insight
./gradlew dependencyInsight --dependency junit-jupiter
```

## Compatibility Notes

- **Java 21**: Required by project configuration
- **Gradle 9.3.0**: Uses Shadow plugin beta (9.0.0-beta4) for compatibility
- **JUnit 5**: Platform launcher explicitly included for Gradle 9.x
- **IDE Support**: Works with IntelliJ IDEA, Eclipse, VS Code with Gradle extensions

## References

- [Gradle Documentation](https://docs.gradle.org/9.3.0/userguide/userguide.html)
- [Shadow Plugin](https://github.com/GradleUp/shadow)
- [Gradle Java Plugin](https://docs.gradle.org/current/userguide/java_plugin.html)
- [Gradle Application Plugin](https://docs.gradle.org/current/userguide/application_plugin.html)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
