# Digital Receipt Reader API
The backend for the Digital Receipt Reader. This exposes endpoints to the mobile app in order to access and modify data in the database. This is a Senior Design project for a class at the University of Toledo Ridge Campus. The following memebers of the group are `Seth Hancock`, `Luke Lengel`, `Sam Butler`, and `Miah Hale`.


<!-- ABOUT THE PROJECT -->
## About The Project

### Built With

* Java
* Gradle
* Spring Boot framework




<!-- GETTING STARTED -->
## Getting Started

To get a local copy up and running follow these simple steps.

### Prerequisites

1. You will first have to make sure that you have gradle installed on your computer. If not then you will want to go to the following link.
    ```sh
    https://gradle.org/install/
    ```
2. If you are not sure if you have gradle installed, run the following command in the command prompt.
    ```sh
    gradle -v
    ```
    * If a version of gradle is displayed then you have gradle on your system.
3. Next you will need an IDE, you can either use Vscode or Eclipse for this (It does not matter, both work the same)
4. Then you will need a jdk installed in your computer of 11 or higher. You can go to the following link to do this.
    ```sh
    https://jdk.java.net/java-se-ri/14
    ```

### Installation

1. Clone the repo
   ```sh
   git clone https://github.com/sambutler1017/digital-receipt-reader-backend.git
   ```

2. Build and install packages provided in `build.gradle` file
   ```sh
   gradle build
   ```



<!-- USAGE EXAMPLES -->
## Usage

Use this space to show useful examples of how a project can be used. Additional screenshots, code examples and demos work well in this space. You may also link to more resources.

* Using and running the endpoints you will want an API client such as postman or some extension of chrome that allows you to consume endpoints.

1. The following link is to download postman on your local machine. You will be asked to create an account (it's free)
    ```sh
    https://www.postman.com/downloads/
    ```

2. Next, in the project you will want to copy the `application.local.template.propteries` file and paste it and rename it to `application.local.properties`.

3. Then inside you will want to update the following fields:
    ```sh
    spring.datasource.username=<MYSQL_USERNAME>
    spring.datasource.password=<MYSQL_PASSWORD>
    ```
4. Finally, everything is set up and you can run the following command to start a local instance of the project.
    ```sh
    gradle bootrun
    ```
5. If it stood up successfully you should be able to hit the endpoints in the project at the route of `localhost:8080`




<!-- CONTACT -->
## Contact

Samuel Butler - sambutler1017@icloud.com

Project Link: [https://github.com/sambutler1017/digital-receipt-reader-backend](https://github.com/sambutler1017/digital-receipt-reader-backend)



<!-- ACKNOWLEDGEMENTS -->
## Acknowledgements

* Seth Hancock
* Luke Lengel
* Miah Hale
