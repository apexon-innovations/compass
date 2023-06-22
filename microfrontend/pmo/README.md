
# PMO APP

### Basic information 

-    Here we used the create react app as a starter kit to make a PMO app and it is a micro frontend app.
-   The main purpose of this app is to manage routing, state, authentication, Unit test cases, CSS and some API level logic to load widgets. 
-    In this app, We include reusable-components as npm package (Please check RC repo readme for more details)
-    There is a separate CI/CD pipeline for PMO APP upload on the server.
-    In the local machine, the PMO app will work on 3001 PORT. (you can change it from package.json)
-    This app will access after login has been completed and in URL param we get all details of users and store in local storage.


## Available Scripts

In the project directory, you can run:

### `npm run start`
Runs the app in the development mode. Open [http://localhost:3001](http://localhost:3001) to view it in the browser.

The page will reload if you make edits.<br  /> You will also see any lint errors in the console.


### `npm install build`

Builds the app for production to the `build` folder.<br  />

It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.<br  />Your app is ready to be deployed!

See the section about [deployment](https://facebook.github.io/create-react-app/docs/deployment) for more information.

### `npm run test`

Launches the test runner in the interactive watch mode.<br  />

See the section about [running tests](https://facebook.github.io/create-react-app/docs/running-tests) for more information.

### `npm run analyse`
 Its will lunch analyzer and its all information about build files and its size
 Open [http://127.0.0.1:8888/](http://127.0.0.1:8888/) to view it in the browser.