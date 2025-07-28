# c8-develop-workers-java-lab

## Project Overview
This is a Camunda 8 job worker implementation lab project demonstrating advanced job handling patterns using Java.

## Key Changes Made

### 1. Dependency Updates
- **Updated `pom.xml`**: Migrated from legacy Zeebe Java Client to the modern **Camunda Java Client**
- This ensures compatibility with the latest Camunda 8 features and APIs

### 2. Code Refactoring
- **Refactored job handlers**: Updated all job handler implementations to use the new Camunda Java Client APIs
- **Updated imports**: Changed from `io.zeebe.*` to `io.camunda.client.*` packages
- **Modernized worker patterns**: Implemented current best practices for job worker development

### 3. Local Development Setup
- **Configured for local environment**: Project is set up to run against a local Camunda 8 instance
- **Removed cloud dependencies**: No cloud-specific configurations or authentication required
- **Local connection**: Uses local broker connection instead of Camunda Cloud/SaaS

## Setup Instructions

### Prerequisites
- Java 11 or higher
- Maven 3.6+
- Local Camunda 8 instance running (Docker recommended)

### Running Locally
1. Clone the repository
2. Update Maven dependencies:
   ```bash
   mvn clean install
   ```
3. Ensure local Camunda 8 is running on default port (26500)
4. Run the application:
   ```bash
   mvn exec:java -Dexec.mainClass="com.camunda.academy.Main"
   ```

## Features
- Advanced job worker patterns
- Payment processing simulation
- Comprehensive logging
- Error handling and retry mechanisms
- Local development optimized

## Technologies Used
- **Camunda 8** (Local setup)
- **Java 11+**
- **Maven**
- **SLF4J** for logging
- **JavaFaker** for test data generation

---
*Configured for local development environment*