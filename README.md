# jenkins-tanaguru-plugin

The plugin allows you to trigger Tanaguru analysis from Jenkins using Build step.

## Requirements

Jenkins : v1.597+

### System requirements 
On the same machine, the following components need to be installed : 
* Tanaguru Cli : built from develop (incompatible with the latest release). Please refer to  http://tanaguru.org/en/content/ubuntu-installation-tanaguru-3x for installation
* Latest firefox Esr : Downloaded from http://download.cdn.mozilla.net/pub/mozilla.org/firefox/releases/latest-esr/
* Xvfb : Refer to http://tanaguru.org/en/content/ubuntu-prerequisites-tanaguru-3x for details about Xvfb installation

### Required Jenkins Plugins:
* Plot plugin (https://wiki.jenkins-ci.org/display/JENKINS/Plot+Plugin)

##How to install

Run
``mvn clean install``
to create the plugin .hpi file.

To install the plugin:

* copy the resulting ./target/tanaguru.hpi file to the $JENKINS_HOME/plugins directory. Don't forget to restart Jenkins afterwards.

* or use the plugin management console (http://example.com:8080/pluginManager/advanced) to upload the hpi file. You have to restart Jenkins in order to find the pluing in the installed plugins list.

##Configure plugin

* Log into Jenkins as an administrator and go to Manage Jenkins > Configure System: 
* Scroll down to the Tanaguru Runner configuration section:
* Configure your Tanaguru Cli installation
* Configure your Firefox ESR installation
* Configure the display exported by XVFB

##Use plugin
### Configure the Build Step
### Create your scenario
### Create your scenario

