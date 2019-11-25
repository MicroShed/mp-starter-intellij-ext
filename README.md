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

# Prepare a new version for release

**Update version**

Update the version of the plugin in `build.gradle` (use semantic versioning scheme):
```groovy
version 'X.Y.Z'
```

**Update the change log**

The change log in the `patchPluginXml` block inside `build.gradle` should be updated with necessary details about the changes in the new version. During the build process, the contents of the `changeNotes` block would be copied into the 
`plugin.xml` that eventually appears as part of the description of the plugin in its IntelliJ marketplace page:

Example:

```groovy
patchPluginXml {
    changeNotes """
        The issues #X resolved.<br/>
        Feature Y added
    """
}
```

**Build a new distribution**

`./gradlew buildPlugin`

This task prepares the final output in `build/distribution` folder as a zip file.

**Upload to the marketplace**

This step is manual. You should login to [Upload New Plugin](https://plugins.jetbrains.com/plugin/add/) page in
Jetbrains website using the project account and upload the new version there.
