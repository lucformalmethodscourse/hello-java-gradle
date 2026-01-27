[![Java CI with Gradle](https://github.com/lucformalmethodscourse/hello-java-maven/actions/workflows/gradle.yml/badge.svg)](https://github.com/lucformalmethodscourse/hello-java-maven/actions/workflows/gradle.yml)

This build will always fail because it has 2 examples of failing tests:
one because of a defect in the SUT (system under test), and one because of an error in the test itself.

## Learning objectives

* Simple hello world example
* Experience with Git source code management
* Building with Gradle
* Automated unit testing with JUnit 5
* Continuous integration with GitHub Actions

## System requirements

* Java 21 SDK or later (Java 21 LTS release recommended)
* Gradle (included via wrapper - no separate installation required)

Your Codespace for this repo will already include these tools.

When working on a local workstation, you can conveniently install Java using [SDKMAN!](https://sdkman.io/).

## Migration Note

This project has been migrated from Maven to Gradle 9.3.0. For detailed migration information, see [doc/GRADLE-MIGRATION.md](doc/GRADLE-MIGRATION.md).

## Running the application

Without command-line arguments:

    $ ./gradlew run

You can also run the application with specific command-line arguments:

    $ ./gradlew run --args="arg1 arg2 arg3"
	
## Running the tests

To compile and run the tests:

    $ ./gradlew test
	
## Generating the test coverage report

The coverage report is automatically generated when you run tests. To view it:

    $ ./gradlew test jacocoTestReport

You can view the formatted HTML version of the report.
In your Codespace or other VS Code instance, locate

    build/reports/jacoco/test/html/index.html
    
then right-click and choose "show preview" from the context menu.
(This requires the Live Preview extension for VS Code to be installed.)

You can also open the report locally through a web browser or on the command line.

On macOS:

    $ open build/reports/jacoco/test/html/index.html

On Linux:

    $ xdg-open build/reports/jacoco/test/html/index.html

On Windows: please let me know if you know how to do this from the WSL
command line.
Otherwise you can open the index file in your web browser or VS Code.

## Running the application outside Gradle

First, create the standalone executable JAR file containing all dependencies:

    $ ./gradlew shadowJar

This creates a "fat JAR" that can be executed anywhere Java is installed.

The JAR can be run on any platform (Linux, macOS, Windows) with:

    $ java -jar build/libs/hello-java-1.0-SNAPSHOT-all.jar arg1 arg2 arg3

No classpath configuration is needed - all dependencies are bundled inside the JAR.
