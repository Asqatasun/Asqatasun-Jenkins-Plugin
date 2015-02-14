# Tanaguru Jekins Plugin

This project is the **Jenkins plugin** of Tanaguru. 

(If you are only interested in testing web accessibility in a graphical environment,
please go to [Tanaguru Documentation](http://tanaguru.readthedocs.org/))

The Jenkins plugin of Tanaguru is dedicated to integrate the web accessibility 
tests Tanaguru can do into a Quality Assurance (QA) plateform, here Jenkins.
It allows for example to add accessibility tests of a web application on a nightly 
build process (for instance just after the unit tests and functional tests).

The Jenkins plugin of Tanaguru is opensource (AGPL license).

@@@ add an image to make this more sexy :)

## Problem & solution

You already have a QA platform (Jenkins) to build and test your app. Different kinds of 
tests are performed thanks to various Jenkins plugins (unit tests, functional 
tests, smoke tests, performance tests...). 

For accessibility testing, your staff uses some automated tools and human tests, but
you would like to have a stack of basic accessibility tests done in a **fully automated and integrated**
fashion. You know this is not sufficient to have a full compliance to accessibility,
but this is a good starting point to seamlessly increase the accessibility level of your app
(and by the way the accessibility skills of your dev team ;) ).

Tanaguru Jenkins plugin is meant to "mecanize" the tests provided by Tanaguru.

## Features

* leverage strong level of automation provided by Tanaguru
* benefit from highly reliable tests of Tanaguru
* have stable or broken build depending on a given level of acceptance 
(for instance, stable = at least 80% of tests are OK)
* have a centralized control panel in Jenkins

You may also generate graphs of various metrics including:

* Tanaguru Meter (the performance score of accessibility)
* number of Passed / Failed / Not Applicatble tests
* number of occurrences of failed tests

All audits ran can also be viewed in detail within Tanaguru web application, easing the
identification of given errors.

You will find a detailed [list of accessibility tests in Tanaguru Documentation](http://tanaguru.readthedocs.org/).

## Download

[Tanaguru Jenkins plugin latest release (.hpi, ~500kb)](http://download.tanaguru.org/Tanaguru-jenkins-plugin/tanaguru-jenkins-plugin-latest.hpi)

## Demo

@@@ see Tanaguru on Tanaguru

## Installation and documentation

* [Installation doc](install-doc.md)
* [Graphs creation and customisation](graphs.md)
* [Developer doc (how to build, to contribute)](developer-doc.md)

## Support and discussions

* [Tanaguru discussion space](http://discuss.tanaguru.org) 

## Contribute

- [Source Code](https://github.com/Tanaguru/jenkins-tanaguru-plugin/)
- [Issue Tracker](https://github.com/Tanaguru/jenkins-tanaguru-plugin/issues)

## Contact 

* email to `tanaguru AT tanaguru dot org` (only english, french and klingon is spoken :) ) 
* [Twitter @TanaguruApp](https://twitter.com/tanaguruapp)

## Other opensource tools

* [KBAccess](http://www.kbaccess.org/) : database of good and bad examples of web accessibility
* [Tanaguru Contrast-Finder](http://contrast-finder.tanaguru.com/) : for a given wrong contrast, *offers* good color combination
* [Accessibility observatory](http://observatoire-accessibilite.org/) : have an overview of the accessibility of a large set of websites
 
All these projects are opensource and also under the umbrella of [Tanaguru Github account](https://github.com/Tanaguru)

Have fun !

[Tanaguru Team](http://tanaguru.readthedocs.org/en/develop/tanaguru-team/)