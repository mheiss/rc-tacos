TACOS-PROJECT |
===============

To get involved into the TACOS project you have to prepare the development environment using the 
following features:

	Eclipse SDK 3.5						http://download.eclipse.org/eclipse/downloads/drops/R-3.5.2-201002111343/index.php
	Tomcat 6.0.26						http://tomcat.apache.org/download-60.cgi
	Eclipse SQL Explorer				https://sourceforge.net/projects/eclipsesql/files/

The project also requires external features that are not bundled with the Eclipse SDK. 
You can get them from the specified update-sites using the build-in update manager. 

	Subversive SVN 						http://community.polarion.com/projects/subversive/download/eclipse/2.0/update-site/
										http://download.eclipse.org/technology/subversive/0.7/update-site/
	JST Server UI						http://download.eclipse.org/releases/galileo
  	JST Web UI							http://download.eclipse.org/releases/galileo
  	JST Server Adapters  				http://download.eclipse.org/releases/galileo
  	WST Server Adapters					http://download.eclipse.org/releases/galileo
	Eclipse Web Developer Tools			http://download.eclipse.org/releases/galileo
	Eclipse Java EE Developer Tools		http://download.eclipse.org/releases/galileo
	
Checkout-Projects
------------------
We provide a team project set to quickly checkout the code from SVN. To use the TeamProjectSets the corresponding 
SVN provider must be installed using the update manager. Just get the team projects set and import the file using 
the import manager.
  -> File -> Import -> Team Project Set.

Configure Eclipse
---------------
Please use the provided formatter so that the code from individual developers is automatically 
formatted so that reading the code will be easier.

  -> Window -> Preferences -> Java -> Code Style -> Formatter -> Import