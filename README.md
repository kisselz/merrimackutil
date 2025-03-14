# Merrimack Util

This package is a library for students and faculty developing code at Merrimack College. The 
code is protectected by the GPL license. 

## Building
To build the library use ant. The targets are:

- ``compile``: This compiles the project.
- ``dist``: This generates the jar file for distribution (the jar will be in the ``dist`` subdirectory)
- ``api``: This generates the JavaDoc API for hosting on the web (places the website in the ``api`` subdirectory)
- ``clean``: Cleans the whole project removing all of the autogenerated directories.
- ``test``: Runs the JUnit tests.

## Documentation
The documentation for the API and the precompiled jar file is hosted on the CS server at http://cs.merrimack.edu/merrimackutil

## Main Authors
- Zach Kissel (2017 -- )
- Chris Stuetzle (2023 -- )

### Student Contributors
- Seth Holtzman -- `getLong` support for JSON package.
