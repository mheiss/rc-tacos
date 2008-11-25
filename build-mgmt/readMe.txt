Building TACOS Server and Client Application
---------------------------------------------
The following file describes the needed steps to setup and build the TACOS Server and Client Application. 

1. Make sure you checked out all the needed source files from the repository.
   https://rc-tacos.svn.sourceforge.net/svnroot/rc-tacos/source/trunk
   https://rc-tacos.svn.sourceforge.net/svnroot/rc-tacos/build-mgmt

2. Download the 'Eclipse-SDK' and the 'Delta Pack' from http://update.eclipse.org
   
   Unzip them into the 'base-dir' folder of the build environment (build-mgmt)
   Some of the files overlap and it's ok to overwrite them.
   
3. Now you must set some properties so that the build can run.
   The '_build.properties' is a template that you can use. Just copy the file and rename it to 'build.properties'
   
   Set the 'build.workspace' property to the current directory of the build management environment.
   NOTE: Check also 'build.pdeBuildPlugin.version' and 'build.equinoxLauncherPlugin.version'

4. Now the applications can be build using the 'buildAll.xml' file.
   After the build was successfully the server and client application can be found in the 'installation' directory.
   
5. Unzip the application and adjust the configuration

6. Run the applications :D


Requirements
------------
-> Eclipse 3.4.x with an SVN Plugin like http://subclipse.tigris.org
OR
-> External SVN Client and ANT 1.7
   http://tortoisesvn.tigris.org
   http://ant.apache.org/


