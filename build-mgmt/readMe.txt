Building TACOS Server and Client Application
---------------------------------------------
The following file describes the needed steps to setup and build the TACOS Server and Client Application.

1. Checkout the sources from the repository.
   http://rc-tacos.svn.sourceforge.net/viewvc/rc-tacos/source/trunk
   
2. Checkout the complete build-environment
   http://rc-tacos.svn.sourceforge.net/viewvc/rc-tacos/build-mgmt

3. Download the 'Platform Runtime Binary' and the 'RCP Delta Pack' ant the  from http://update.eclipse.org/downloads
   
   Unzip them into the 'base-dir' folder of the build environment.
   Some of the files overlap and it's ok to overwrite them.
   
4. Now you must modify the build.properties files for the server AND the client.
   Server: server/_build.properties 
   Client: client/_build.properties
   
   Make a copy of each of the files and rename it to build.properties
   
   Then modify the following properties to reflect your current environment
    -> pdeBuildPluginVersion
    -> equinoxLauncherPluginVersion
    -> eclipseLocation
    -> build.dir
    
5. Finally you must modify the _build.properties template in this directory and set the path where you checked out the source files.

6. Run the build :)
   The build files can be found in the 'installation' directory

