# Intellij Extension for MicroProfile Starter
This plugin allows you to easily create a microservice module based on MicroProfile 
APIs. You can choose your preferred MicroProfile runtime such as Liberty, Helidon, TomEE, Payara Micro, Thorntail, etc.
By doing so you'll get a project with proper Maven profiles already setup so that you run it right after the
setup completes. 

It also allows you to choose among different MicroProfile APIs (e.g. Configuration, Metrics,
Open Tracing, etc.) and then it automatically generate examples in the initial project configuration.   

The extension incorporates the MicroProfile Starter REST API available at https://start.microprofile.io/api/ to 
generate the project.
 
## Installation
Search for _MicroProfile Starter_ in IntelliJ **Settings > Plugins > Marketplace** and click the Install button.


## Build
The project is built using [gradle-intellij-plugin](https://github.com/JetBrains/gradle-intellij-plugin/).

To build the project you must have the gradle installed on your computer. You can use [sdkman](https://sdkman.io) 
to do that. 
First initialized the gradle wrapper using the following command so that proper version of the Gradle gets downloaded 
for the build process.
```groovy
gradle wrapper
```
then you can build and executes an IntelliJ IDEA instance with the plugin installed by running the following grade
task:
```
./gradlew runIde
```

## Prepare a new version for release

**Update version**

Update the version of the plugin in `build.gradle` (use semantic versioning scheme):
```groovy
version 'X.Y.Z'
```

**Update the change log**

The change log in the `patchPluginXml` block inside `build.gradle` should be updated with necessary details about the changes in the new version.

Example:

```groovy
patchPluginXml {
    changeNotes """
        <strong><em>2019-11-28</em></strong>
        <p>
        [Bug Fix] : NullPointerException in the IDE log when the description of a spec is going to be updated in the project wizard (#334)
        [New Feature] : Now developers can choose options X or Y (#223)
        [Improvements] : The process of doing Z is now faster (#112)
        </p>
    """
}
```

then by running the following gradle task to copy the contents of the `changeNotes` block into the `plugin.xml` that eventually appears as part of the
 description of the plugin in its IntelliJ marketplace page:

```groovy
./gradlew patchPluginXml
```

**Build a new distribution**

`./gradlew buildPlugin`

This task prepares the final output in `build/distribution` folder as a zip file.

**Upload to the marketplace**

You can do this step either manually or via gradle.

- Uploading manually

    You should login to [Upload New Plugin](https://plugins.jetbrains.com/plugin/add/) page in JetBrains website using the
     project account and upload the new version there.

- Upload via gradle

    - Create a Permanent Token with _Marketplace_ scope at [JetBrains Hub](https://hub.jetbrains.com/users/moghaddam?tab=authentification)
    - Copy the value of the token in an environment variable named _ORG_GRADLE_PROJECT_intellijPublishToken_
    - Run`./gradlew publishPlugin` in the root of the project to publish the plugin.
    

Normally it takes two business days until the plugin gets approved and published to the Marketplace.

## Contributing
Our [CONTRIBUTING](CONTRIBUTING.md) document contains details for submitting pull requests.
