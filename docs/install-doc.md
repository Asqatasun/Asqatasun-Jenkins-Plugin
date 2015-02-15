# Installation of Tanaguru Jenkins plugin

## Network architecture

You may use Tanaguru Jenkins plugin in two different ways :

* one single host for the whole stack
* stack split over two or more hosts

### One single host

One single host carries the whole stack (Jenkins, Tanaguru, MySQL).

![](Images/Tanaguru-Jenkins-Network-architecture-one-host.svg)

### Two of more hosts

If you have multiple different Jenkins and want to use only one uniq Tanaguru to
view the details of an audit, you can :

* have a Tanaguru CLI (Command Line Interface) installed on the Jenkins host,
* have a full Tanaguru (CLI + Webapp) on another host,
* and have both Tanaguru share the same MySQL database.

![](Images/Tanaguru-Jenkins-Network-architecture-multiple-hosts.svg)

## Prerequesites

* Jenkins 1.500+ on an Ubuntu Linux box
* Java 7
* XVFB package (see below)
* Firefox ESR (see below)
* Tanaguru 3.0.4+ Command Line Interface (see below)

### XVFB

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

RUN_AS_USER=tomcat6
OPTS=":99 -screen 1 1024x768x24 -nolisten tcp"
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

Configure xvfb to run at startup:

```bash
sudo update-rc.d xvfb defaults
```

## Installation

@@@

### Configuration

