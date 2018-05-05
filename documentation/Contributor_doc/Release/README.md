# Release a version of Asqatasun-Jenkins-Plugin

This is the documentation for releasing a new version for Asqatasun-Jenkins-Plugin.
As an end user, you won't need it, it is just for developers.

## The process

### check dependencies, compile and test it
```bash
    # check dependencies (security)
    mvn dependency-check:aggregate

    # compile and test it
    mvn clean install
    ...

```


### Prepare a Release
```bash
# 1. change version ("x.y.z-SNAPSHOT" to "x.y.z")
    # */pom.xml
    # CHANGELOG
git checkout develop

# for pom.xml files
mvn versions:set -DnewVersion=x.y.z
mvn versions:commit  # remove pom.xml.versionsBackup files

# for these following files
    # CHANGELOG
(...)

# 2. commit
git add .
git commit -m  "set version to x.y.z"

# 3. Merge and commit
git checkout master
git merge --no-ff --log develop
```

versions-maven-plugin (`mvn versions:set -DnewVersion=x.y.z`) :

- http://www.mojohaus.org/versions-maven-plugin/examples/set.html
- http://www.mojohaus.org/versions-maven-plugin/examples/setaggregator.html

Another plugin to do some of the work (not tested):

- http://maven.apache.org/maven-release/maven-release-plugin/index.html

### Test it
```bash
# compile and test it
mvn clean install
...
```

### Release
```bash
# add tag
# push tag
# push master
```

### Prepare develop branch for the next release
```bash
# 1. change version ("x.y.z" to "x.y.z+1-SNAPSHOT")
    # */pom.xml
    # CHANGELOG
git checkout develop

# for pom.xml files
mvn versions:set -DnewVersion=x.y.z+1
mvn versions:commit  # remove pom.xml.versionsBackup files

# for these following files
    # CHANGELOG
(...)

# 2. commit
git add .
git commit -m  "set version to x.y.z+1-SNAPSHOT"
```

## Versioning

We tend to follow the [semantic versioning](http://semver.org/) recommendations.

