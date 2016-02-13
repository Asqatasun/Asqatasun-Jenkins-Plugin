# Asqatasun Jenkins plugin 


![](documentation/Images/logo/kraken.io--lossy/asqatasun-jenkins-plugin--500x268.png)

This project is the **Jenkins plugin** of Asqatasun. 


> If you are only interested in testing web accessibility 
> in a graphical environment, please go to [Asqatasun Documentation](http://doc.asqatasun.org/en/).

----



The Jenkins plugin of Asqatasun is dedicated to integrate the web accessibility 
tests Asqatasun can do into a Quality Assurance (QA) plateform, here Jenkins.
It allows for example to add accessibility tests of a web application on a nightly 
build process (for instance just after the unit tests and functional tests).

The Jenkins plugin of Asqatasun is opensource ([AGPL v3](LICENSE) license).

@@@TODO add screenshot




## Problem & solution

You already have a QA platform (Jenkins) to build and test your app. Different kinds of 
tests are performed thanks to various Jenkins plugins (unit tests, functional 
tests, smoke tests, performance tests...). 

For **accessibility testing**, your staff uses some automated tools and human tests, but
you would like to have a stack of basic accessibility tests done in a **fully automated and integrated**
fashion.

You know this is not sufficient to have a full compliance to accessibility,
but this is a **good starting point to seamlessly increase the accessibility level** of your app
(and by the way the accessibility skills of your dev team ;) ).

Asqatasun Jenkins plugin is meant to *mecanize* the tests provided by Asqatasun.

## Features

* Leverage strong level of automation provided by Asqatasun
* Benefit from highly reliable tests of Asqatasun
* Have stable or broken build depending on a given level of acceptance 
(for instance, stable = at least 80% of tests are OK)
* Have a centralized control panel in Jenkins

You may also generate graphs of various metrics including:

* Asqatasun Meter (the performance score of accessibility)
* Number of Passed / Failed / Not Applicatble tests
* Number of occurrences of failed tests

All audits ran can also be viewed in detail within Asqatasun web application, easing the
identification of given errors.

You will find a detailed [list of accessibility tests in Asqatasun Documentation](http://doc.asqatasun.org/en/).

## Download

[Asqatasun Jenkins plugin latest release (.hpi, ~700kb)](https://github.com/Asqatasun/Asqatasun-Jenkins-Plugin/releases)



## Installation and documentation

* @@@TODO [Network architecture](#documentation/network-architecture.md)
* @@@TODO [Installation doc](#documentation/install-doc.md)
* @@@TODO [Configuration](#documentation/configuration.md)
* @@@TODO [Graphs creation and customisation](#documentation/graphs.md)
* @@@TODO [Developer doc (how to build, to contribute)](#documentation/developer-doc.md)

## Support and discussions

* [Asqatasun discussion space](http://forum.asqatasun.org/) 
* [Twitter @Asqatasun](https://twitter.com/Asqatasun)
* email to `asqatasun AT asqatasun dot org` (only English, French and klingon is spoken :) ) 

## Contribute

We would be really glad to have you on board ! You can help in many ways:

* [Fill in bug report](https://github.com/Asqatasun/Asqatasun-Jenkins-Plugin/issues)
* [Pull Requests](https://github.com/Asqatasun/Asqatasun-Jenkins-Plugin/pulls) are off course welcome


Everything is summarized in the [CONTRIBUTING](CONTRIBUTING.md) file.


## License

 [AGPL v3](LICENSE) 

 
@@@TODO Build Status - travis-ci.org