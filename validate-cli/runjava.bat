@REM ----------------------------------------------------------------------------
@REM It is possible to run the application in 2 modes
@REM Mode 1: automatic detect what version and id from configuration to use when
@REM 		 validating XML
@REM 
@REM Mode 2: spesify both version and id
@REM 
@REM Usage mode 1: runjava.bat pathToXmlFile
@REM Example mode 1: runjava.bat c:\temp\invoice.xml
@REM 
@REM Usage mode 2: runjava.bat pathToXmlFile version id
@REM Example mode 2: runjava.bat c:\temp\invoice.xml 1.6 urn:www.cenbii.eu:profile:bii04:ver1.0#urn:www.cenbii.eu:transaction:biicoretrdm010:ver1.0#urn:www.peppol.eu:bis:peppol4a:ver1.0#urn:www.difi.no:ehf:faktura:ver1
@REM 
@REM ----------------------------------------------------------------------------

@echo off
java -jar target/validate-cli.jar %*