Validate WS
================

# Introduction

This is the web-service part of the validator application developed at DIFI.
It contains the following services:

* SOAP
* REST
* Web

# Build

On the top level, build all components with the following command:

    mvn clean install

Please note that this will not work if you have not specified where to find the configuration-files in the properties file
located in the test-resources:

    validator.properties

Please update the BUILD_PATH to the path where the home of the configuration is placed. Example:

    DATA_DIR=/Users/vegardaasen/develop/github/vefa-validator-conf

# Run

In order to run the application, head to the class located here:

    no.difi.vefa.ws.run.StartServer

Add a new "Application runnable" in your IDE.

You will also need to add the java System parameter which should point to the validator.properties-file:

    no.difi.vefa.validation.configuration.datadir
    # Example:
    -Dno.difi.vefa.validation.configuration.datadir=/Users/vegardaasen/develop/github/vefa-validator-app/validate-ws/src/test/resources/validator.properties

# Run the generated jar-file

In order to run the jar-file which has been built using Maven, you can use the following command in any command prompt:

    java -jar -Dno.difi.vefa.validation.configuration.datadir=/path/to/your/validator.properties validate-ws-1.0.0.jar start 7003

Here, you have the following bits and pieces:

    # VALIDATOR-PROPERTIES
    -Dno.difi.vefa.validation.configuration.datadir=/path/to/your/validator.properties
    # FILE
    validate-ws-1.0.0.jar
    # OPERATION (mandatory, either start or stop)
    start
    # PORT (optional, the default port is 7007)
    7003

# Accessing the services

## Web interface

Navigate to [http://localhost:port/](http://localhost:port/)

## REST services

For a full list, please see the package related to the rest-services.

Navigate to [http://localhost:port/rest/](http://localhost:port/rest/)

## SOAP services

For a full list, please see the package related to the SOAP-services.

Navigate to [http://localhost:port/services/](http://localhost:port/services/)