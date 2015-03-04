Validate CLI
====================

# Information

This is the CLI-version of the validation-api provided by DIFI. The official repository is located here: 
https://github.com/difi/vefa-validator-app

# Build

    ant

# Run
    
    # Usage:
    java -jar -Dno.difi.vefa.validation.configuration.datadir="<path-to-configuration>" validate-cli.jar "<path-to-filename>"
    
    # Example:
    $ java -jar -Dno.difi.vefa.validation.configuration.datadir="C:/_dev/github/vefa-validator-conf-master/" validate-cli.jar "C:\_dev\github\vefa-validator-app-master\validate-lib\src\test\resources\Invoice.xml"