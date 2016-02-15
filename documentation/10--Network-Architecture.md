# Network architecture 

You may use Asqatasun Jenkins plugin in two different ways :

* one single host for the whole stack
* stack split over two or more hosts

## One single host

One single host carries the whole stack (Jenkins, Asqatasun, MySQL).


@@@TODO add image 
![](Images/Asqatasun-Jenkins-Network-architecture-one-host.svg)

## Two of more hosts

If you have multiple Jenkins and want to use only one uniq Asqatasun to
view the details of an audit, you can :

* have a Asqatasun runner (Command Line Interface) installed on the Jenkins host,
* have a full Asqatasun on another host,
* and have both Asqatasun share the same MySQL database.

@@@TODO add image 
![](Images/Asqatasun-Jenkins-Network-architecture-multiple-hosts.svg)

## Next step

Proceed to [Installation](20-install-doc.md)