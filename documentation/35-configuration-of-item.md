# Configuration of item

## Create a new item

* Give it a name, let say "Asqatasun on myApp"
* Let the item be **freestyle project**

![](Images/screenshot_20150218_ASQATASUN_jenkins_new_item.png)

## Add build step

* Add a build step
* Select **Asqatasun Runner**

![](Images/screenshot_20150218_ASQATASUN_jenkins_new_item_add_build_step.png)
![](Images/screenshot_20150218_ASQATASUN_jenkins_new_item_add_build_step-2.png)

## Configure build step

* Name the scenario
* Copy/paste the scenario you will have created (see [Asqatasun scenario user documentation](http://doc.asqatasun.org/en/20_User_doc/userdoc-04-scenario-audit.html))
* Choose the Referentiel and Level (if you don't what to choose, select *Rgaa3: AA*)
* Save

![](Images/screenshot_20150218_ASQATASUN_jenkins_new_item_configure_build_step_highlight01.png)

## Links between Jenkins and Asqatasun

Here are a few important things to know.

* Each item will **automatically create a contract** for [Asqatasun user configured for Jenkins](30-configuration.md#asqatasun-account-login-mandatory)
just after the first build. The contract name will be the item name. The contract
will have the scenario feature activated. The scenario name will be the one just given.
* Renaming an item **does not rename** the associated Asqatasun project, a new project is created instead.
* Renaming the scenario or changing its content, will **add a new scenario** to project.
The old scenario will be kept in Asqatasun, but will be no more executed by Jenkins.
* In Asqatasun, the project page contains the history of last audits. Each audit is named
as the concatenation of the scenario name and the job build number given by Jenkins

## Define when a build is stable / unstable

You have two metrics to define the stability of a build:

* Minimal mark (Asqatasun Meter)
* Maximal number of failed occurrences

![](Images/screenshot_20150218_ASQATASUN_jenkins_new_item_configure_build_step_stability.png)

Default values are the following ones:

* Minimal mark: 100 (percentage)
* Maximal number of failed occurrences: 0 (absolute value) 

## Next step

proceed to [Usage](40-usage.md)

