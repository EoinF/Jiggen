# Jiggen

A jigsaw puzzle mobile/desktop browser game.

## Frontend technologies

* LibGDX
* React
* Both served by a tomcat service

## Development setup steps

#### In-game logic

The in-game logic refers to what happens inside the html canvas, including interacting with the puzzle.

To test this out run with the standard libgdx desktop configuration.

#### Web UI

The rest of the app, including choosing a puzzle to play with, creating new puzzles, etc.

To test this out, go to the html/react folder and run `npm start`

#### LibGDX/React integration

The Gwt adapter exists in both javascript and java code

### gwtadapter.js

This contains functions that interact with the libgdx code
It exposes a promise `onGwtLoadedPromise` which is resolved by the gdx code when initialization is complete

It also wraps the methods exposed by the gdx code (gdx attaches functions to the global window) and ensures
that the libgdx code has initialized, through the use of the promise.

### GwtAdapter.java

This contains functions, which are attached to the global window, that are called by the js code.
It contains the business logic for these functions.

### Service worker setup

To generate the service worker for local development:
* Start up gwt superdev as usual
* cd to `html/react` folder
* Open a bash terminal
* Run `BUILD_PATH='../build/gwt/draftOut' npm run build-sw`

Note: Recompiling the gdx app will not work with service worker caching. Superdev should be restarted

## Deployment on AWS

The code is built to be deployed on AWS, but can be used with any container orchestration service, if modified

### Docker Build Image

The definition is inside docker-builder

Running this image in a container will fetch the latest version of jiggen, build it, and create a new docker image
for deploying it.

### Docker Deploy Image

The definition is inside docker-deploy

Running this image in a container will deploy the version of jiggen installed inside it. It uses tomcat to serve
the app

### Dockerrun.aws.json

A configuration used for AWS Elastic beanstalk

Uploading this configuration will fetch the latest image created by docker-deploy and launch a container instance
using it.