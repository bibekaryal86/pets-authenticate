# pets-authenticate-layer

This REST API is the authentication service/server for Personal Expenses Tracking System application.
This service is created to use as the authentication server to validate users. It provides two services:
* Validate username / password and return user details with token to use in subsequent service calls.
* Validate existing token and provide an updated one with added expiration time.

To run the app, we need to supply the following environment variables:
* Active Profile
    * spring.profiles.active (development, docker, production)
* PETS Service Security Details:
    * BASIC_AUTH_USR_PETSSERVICE (auth username of pets-service)
    * BASIC_AUTH_PWD_PETSSERVICE (auth password of pets-service)
* The final run command looks like this:
    * java -jar -D"spring.profiles.active=development" -DBASIC_AUTH_USR_PETSSERVICE=some_username -DBASIC_AUTH_PWD_PETSSERVICE=some_password JARFILE.jar

This app is one of the five apps that form the PETS (Personal Expenses Tracking System) application:
* https://github.com/bibekaryal86/pets-database-layer
* https://github.com/bibekaryal86/pets-service-layer
* https://github.com/bibekaryal86/pets-authenticate-layer (this)
* https://github.com/bibekaryal86/pets-gateway-layer
* https://github.com/bibekaryal86/pets-ui-layer

This app is deployed in Google Cloud Project. The GCP configurations are found in the `gcp` folder in the project root.
To deploy to GCP, we need to copy the jar file to that folder and use gcloud app deploy terminal command.
* App Test Link: https://pets-authenticate.appspot.com/pets-authenticate/tests/ping
