# Parallel Hash Coding Challenge

## About the Application
The application accepts a text file containing a list of URLs. 
The application then downloads the content from the URLs in parallel and calculates
the MD5 hash of each URL content. The list of URLs processed is configurable as well
as the number of threads used to hash the url content (see below);

## Documentation
Aside from this readme, the `adr` folder contains the required notes expressing what would 
have been done differently had the time spent on the task been a week.

## Tech
The application is built using Spring Boot and Java.
Dependency management and build uses Gradle. The Gradle Wrapper is used so that
Gradle need not be installed as a prerequisite.

## Setup prior to running
User definable items include the input text file and a max thread limit.
* The input file containing the URLs is located in the `src/main/resources` folder.
* The name of the input file is specified in the`application.properties` file as 
the `url.input.file` property whose value must match the filename in the `resources` folder. 
* The number of threads can be limited by changing the `max.thread.count` value in
the `application.properties` file. If no limit is provided, the application will use the
lesser of the count of URLs in the input file vs. the number of processor cores on the machine.
If a limit is set to a number that is greater than the count of URLs to process, then the limit is set
to the count of the URLs to process to avoid the overhead of unneeded threads.

## Testing
From the project root, run:
`/.gradlew test`

## Building
From the project root, run:
`/.gradlew clean build`

## Running
From the project root, run:
`/.gradlew bootRun`
