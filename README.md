# Compass

## Overview
Compass is a business intelligence application that provides a collection of various dashboards. The purpose of Compass is to showcase data and enable users to gain valuable insights. It operates based on the principle of data-in, data-out, meaning that it does not collect real-time data. Instead, it presents operational and developmental metrics to offer a comprehensive understanding of where to invest resources to improve processes and minimize future risks.

By leveraging the dashboards in Compass, users can analyze and visualize data to make informed decisions and drive improvements in their business operations

This readme provides an overview of the Compass application, including its features, setup instructions, and usage guidelines.

## Table of Contents
- [Requirements](#requirements)
- [Installation](#installation)
- [Usage](#usage)
- [Cloud Deployment](#cloud-deployment)
- [Microservices](#microservices)
- [Microfrontend](#microfrontend)
- [Collectors](#collectors)
- [Post Processors](#post-processors)
- [Contributing](#contributing)
- [License](#license)

## Requirements
To run the Compass application, ensure that the following prerequisites are met:

- Java Development Kit (JDK) 17 or later is installed.
- Maven build tool is installed.
- Node.js 16+ and npm are installed.
- Python 3.6+ is installed.
- Docker is installed (optional, for containerized deployment).

## Installation
To install the Compass application, follow these steps:

1. Clone the monorepo from the repository:

   ```
   git clone <repository_url>
   ```

2. Build the project using Maven:

   ```
   mvn clean install
   ```

3. The build process will generate the necessary artifacts for each microservice.

4. Change to the `microfrontend` directory:

   ```
   cd microfrontend
   ```

5. Install the dependencies for the microfrontend individually in their respective folder:

   ```
   npm install
   ```

## Usage
To start using the Compass application, follow these steps:

1. Run each microservice individually or deploy them in a containerized environment (e.g., Docker). Refer to the respective microservice's readme for detailed instructions on how to run or deploy them.

2. Once all microservices are running, you can access the Compass application using the provided endpoints or user interface (UI). Refer to the documentation or user guide for detailed information on how to interact with the application.

3. To start the microfrontend, change to the `microfrontend` directory and run each microfrontend individually in their respective folder.

4. Once all the microfrontend are running, start the microfrontend server

   ```
   cd server
   node server.js
   ```

   This will start the development server and open the Compass application in your default web browser.

## Cloud Deployment
Compass can also be deployed on the cloud using AWS services and Helm. To deploy the Compass application on the cloud, follow these steps:

1. Set up an AWS account and configure the necessary credentials and permissions.

2. Use the provided cloud scripts for AWS to create the required infrastructure components and set up the networking and security configurations.

3. Once the infrastructure is set up, use Helm to deploy the Compass application on the cloud. Helm charts are provided to simplify the deployment process. Refer to the documentation or user guide for detailed instructions on how to deploy the application using Helm in individual document.

4. After successful deployment, you can access the Compass application through the provided cloud endpoint. Use the AWS console or CLI to obtain the application's endpoint URL.

## Microservices
The Compass application is structured as a monorepo and consists of the following microservices:

1. **PSR Service:** Responsible for serving data related to JIRA and other Project management tools in PMO microfrontend.
2. **Strategy Service:** Responsible for serving data related to SCM tools in PMO microfrontend.
3. **Client Service:** Responsible for serving data related to all the agile tools configured in Client microfrontend.
4. **User Service:** Handles user management, authentication, and authorization functionalities.
5. **Onboard Service:** Helps you onboard projects on Compass and configure its respective collectors.

Each microservice is located in its respective directory within the monorepo and has its own set of dependencies, configurations, and endpoints.

## Microfrontend
The Compass application includes a React microfrontend located in the `microfrontend` directory. The microfrontend enhances the user interface and provides an interactive experience for users.

The microfrontend is built using React, and its dependencies are managed through npm. To make changes or customize the microfrontend, navigate to the `microfrontend` directory and follow the instructions provided in the microfrontend readme.

## Collectors
Collectors in the Compass application are Java programs responsible for collecting data from different Agile tools and dumping it into a data lake. These collectors integrate with

various APIs or data sources to extract relevant information. The collected data is then stored in a structured format in the data lake for further processing.

To use collectors, follow the specific instructions provided in the collector's readme. Set up the necessary configurations, such as API keys or credentials, and run the collector program to initiate data collection.

## Post Processors
Post processors in the Compass application are responsible for processing the data stored in the data lake. These components refine the collected data into a Unified Data Model (UDM) and store it in a database. The UDM represents a standardized format that can be easily accessed and served by microservices.

To use post processors, follow the specific instructions provided in the post processor's readme. Configure the necessary parameters, such as the data lake location and database connection details, and execute the post processor program to process the data and store it in the database.

## Contributing
Contributions to the Compass application are welcome. If you would like to contribute, please follow these guidelines:

1. Fork the repository and create a new branch for your contribution.
2. Make your changes and ensure that the code adheres to the project's coding standards.
3. Write unit tests for the new features or modifications.

. Submit a pull request, clearly describing the changes you have made and their purpose.

## License
The Compass application is licensed under the **MIT License**.