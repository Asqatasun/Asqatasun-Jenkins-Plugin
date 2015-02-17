# Installation of Tanaguru Jenkins plugin

## Prerequesites

* Java 7
* Jenkins 1.500+ on an Ubuntu Linux box
* Jenkins Plot Plugin  
* XVFB package (see below)
* Tanaguru 3.0.4+ (see below)

### Plot PLugin

Available:

* either from the *Available plugins*
* or from [plot plugin page](https://wiki.jenkins-ci.org/display/JENKINS/Plot+Plugin)


### XVFB

This is the Xvfb used for Jenkins (not the one used for Tanaguru)

```bash
sudo aptitude install xvfb
```

Create the startup script in `/etc/init.d/xvfb`

```bash
sudo touch /etc/init.d/xvfb
```

Add the following content to the xvfb startup script.

```bash
#!/bin/sh

set -e

RUN_AS_USER=jenkins                             # the user is jenkins
OPTS=":98 -screen 1 1024x768x24 -nolisten tcp"  # the port is 98 
XVFB_DIR=/usr/bin
PIDFILE=/var/run/xvfb

case $1 in

start)
    start-stop-daemon --chuid $RUN_AS_USER -b --start --exec $XVFB_DIR/Xvfb --make-pidfile --pidfile $PIDFILE -- $OPTS &
;;

stop)
    start-stop-daemon --stop --user $RUN_AS_USER --pidfile $PIDFILE
    rm -f $PIDFILE
;;

restart)
    if start-stop-daemon --test --stop --user $RUN_AS_USER --pidfile $PIDFILE >/dev/null; then
        $0 stop
    fi;
    $0 start
;;

*)
    echo "Usage: $0 (start|restart|stop)"
    exit 1
;;

esac

exit 0
```

Start Xvfb:

```bash
sudo chmod +x /etc/init.d/xvfb
sudo /etc/init.d/xvfb start
```

Configure Xvfb to run at startup:

```bash
sudo update-rc.d xvfb defaults
```

**Note ** : if you are on a single host installation, you will have two Xvfb : one for Jenkins, another one for Tanaguru.

### Tanaguru

#### Single host install

[Install Tanaguru ](http://tanaguru.readthedocs.org/).

#### Multiple hosts install

* we assume you already have a full Tanaguru installed with agiven Mysql database
* [Install Tanaguru Command Line](http://tanaguru.readthedocs.org/en/develop/prerequisites-cli-doc/) on the 
Jenkins host, and configure it to use the same MySql database

Let `TANAGURU_CLI_DIR` be the directory where Tanaguru Cli is installed (for 
instance `/opt/tanaguru-cli/`, containing the `bin/` `conf/` `lib/` `logs/` directories)

Run:

```bash
sudo chown -R jenkins TANAGURU_CLI_DIR
```

## Installation

Grab the [Tanaguru Jenkins plugin (.hpi, ~500kb)](http://download.tanaguru.org/Tanaguru-jenkins-plugin/tanaguru-jenkins-plugin-latest.hpi)

Upload the plugin. Go to Manage Jenkins > Manage plugins

![](Images/screenshot_20150216_TANAGURU_jenkins_manage_plugins.png)

Go to Advanced tab and upload the .hpi file

![](Images/screenshot_20150216_TANAGURU_jenkins_manage_plugins_advanced_tab_highlight.png)

If you ever had played with Tanaguru CLI before, please run:

```bash
sudo rm -rf  /tmp/org.hibernate.cache.*
```

## Next step

proceed to [Configuration](configuration.md)
