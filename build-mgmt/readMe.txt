Building TACOS Server and Client Application
---------------------------------------------
The following file describes the needed steps to setup and build the TACOS Server and Client Application. 

1. Make sure you checked out all the needed source files from the repository.
   https://rc-tacos.svn.sourceforge.net/svnroot/rc-tacos/source/trunk
   https://rc-tacos.svn.sourceforge.net/svnroot/rc-tacos/build-mgmt

2. Download the 'Platform Runtime Binary' and the 'RCP Delta Pack' from http://update.eclipse.org/downloads
   
   Unzip them into the 'base-dir' folder of the build environment (build-mgmt)
   Some of the files overlap and it's ok to overwrite them.
   
3. Now you must modify the build.properties files for the server AND the client.
   Server: server/_build.properties 
   Client: client/_build.properties
   
   Make a copy of each files and rename it to build.properties
   
   Then modify the following properties to reflect your current environment
    -> pdeBuildPluginVersion
    -> equinoxLauncherPluginVersion
    -> eclipseLocation
    -> dir.buildBase

4. Run the 'build-all' target to build the application
   The server and client application can be found in the 'installation' directory.
   
5. Unzip the application and adjust the configuration

6. Run the applications :D

