#
# ReadMe file for the adapted version of IzPack
#
# Written by                : David Loureiro
# Date of last modification : 05/03/2007
#

IzPack has been adapted to provide customized panels for the
DIET_DashBoard and GRUDU.

To compile the adapted version of the standalone compiler of IzPack
you must run :

	ant build.standalone 
	
in the src directory.

Then in the lib directory you should find (if the compilation 
succeeded) a jar fie called 

standalone-compiler.jar

that should be placed in the lib directory of the DIET_DashBoard
or GRUDU project tree.

When calling the GRUDU_installer or DIET_DashBoard_installer target
of the main ant file of the project, this archive will be used
to create the custom panels.
