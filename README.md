# rate-limiter
Rate limiter - API requests rate limiter (demo)

### Must have pre-installed
- Oracle JDK 17.0.3
- Gradle 7.6
- Redis server 7.0.0

### Description
Solution consists of 2 parts:
* Back-end: api module (By default starts on port 9090)
* Front-end: app module (By default starts on port 9091)

## Build & Deployment
1. Ensure redis is installed and running. System will expect it at address localhost:6379
2. Execute following commands (from project's root) to start both modules:


````
// Build whole project: 
./gradlew clean build

// Deploy API module: 
./gradlew :api:bootRun

// Deploy APP module: 
cd app
npm i
npm start
````

## Usage:
Navigate to http://localhost:9091 in your browser.

By default, system has only one in-memory user with 1000 points (username: admin).
Any other username will be treated as guest user with 500 points.
Enter number in the second field to specify request weight and press submit.
Execute multiple times using submit button to test rate limit.

#### IMPORTANT!!!
Unrecognized user's rate limit is calculated using user's IP.
If you call endpoint/* using different guest users from you machine,
ip address will be same, so next random username will immediately reach rate limit.



