# Jiggen Desktop
A sample app for testing out the puzzle solver screen specifically.

It's possible to configure it to run the template generation program, but that requires 
some code changes to use the TemplateCreatorScreen.

## Running locally
Run this script at the root of the project to create links to the core assets within
the desktop assets folder.

Set the working directory to `desktop/assets` so that both
core assets and desktop assets will be available at run time; thanks to the 
junction links

##### Open command prompt as admin and run:

    mklink /J desktop\assets\shaders core\assets\shaders
    mklink /J desktop\assets\skin core\assets\skin
    mklink /J desktop\assets\ui core\assets\ui
    
#### Running the code

Run Gradle from the root of the project

    gradlew desktop:run
    
To use IDE integration consult the libgdx documentation

    