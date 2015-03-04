This repository contains the application files for the [VEFA validator](http://vefa.difi.no) which is being developed by [Difi](http://www.difi.no).

The validator consists of two Github projects:
*  [vefa-validator-app](https://github.com/difi/vefa-validator-app) - The java application (this project)
*  [vefa-validator-conf](https://github.com/difi/vefa-validator-conf) - The configuration

To install:
* See [documentation](https://github.com/difi/vefa-validator-app/blob/master/validate-web/src/main/webapp/documentation/Documentation.rtf) for details

Short install version:
* Clone [vefa-validator-conf](https://github.com/difi/vefa-validator-conf) to /etc/opt/VEFAvalidator
* Clone [vefa-validator-app](https://github.com/difi/vefa-validator-app) to a directory of your choosing

To build:
* At VEFAvalidatorApp: mvn clean install

The build will create a web application and a web service application that can be install in Tomcat or GlassFish. Please refer to the [documentation](https://github.com/difi/vefa-validator-app/blob/master/validate-web/src/main/webapp/documentation/Documentation.rtf) for more information.
