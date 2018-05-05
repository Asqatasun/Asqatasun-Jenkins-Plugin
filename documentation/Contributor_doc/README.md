#  Developer documentation

* [Release a version of Asqatasun-Jenkins-Plugin](Release/README.md)

-----
* Checking for new dependency / plugin updates
* OWASP Dependency-Check
* JavaDoc

## Checking for new dependency / plugin updates
```bash
# Checking for new dependency updates
mvn versions:display-dependency-updates

# Checking for new plugin updates
mvn versions:display-plugin-updates

# Checking for new property-linked updates
mvn versions:display-property-updates
```

### Documentation
* http://www.mojohaus.org/versions-maven-plugin/examples/display-dependency-updates.html
* http://www.mojohaus.org/versions-maven-plugin/examples/display-plugin-updates.html
* http://www.mojohaus.org/versions-maven-plugin/examples/display-property-updates.html


##  OWASP Dependency-Check
Scan Asqatasun-Jenkins-Plugin to identify the use of known vulnerable components.

```bash
git clone https://github.com/Asqatasun/Asqatasun-Jenkins-Plugin
cd  Asqatasun-Jenkins-Plugin
```

One of the following command lines:
```bash
mvn dependency-check:check
mvn dependency-check:aggregate
mvn site
```

The dependencies report is available in this file:
```bash
target/dependency-check-report.html
```

Others command lines:
```bash
mvn dependency-check:help
mvn dependency-check:help -Ddetail=true -Dgoal=aggregate
```

### Documentation
* https://www.owasp.org/index.php/OWASP_Dependency_Check
* http://jeremylong.github.io/DependencyCheck/
* http://jeremylong.github.io/DependencyCheck/dependency-check-maven/


## JavaDoc

```bash
git clone https://github.com/Asqatasun/Asqatasun-Jenkins-Plugin
cd Asqatasun-Jenkins-Plugin
```

One of the following command lines:
```bash
mvn javadoc:javadoc
mvn javadoc:aggregate
mvn javadoc:test-javadoc
mvn javadoc:test-aggregate
mvn site
```

JavaDoc is available in the following directories:
```bash
target/site/apidocs
target/site/testapidocs
```

### Documentation
* [Apache Maven Javadoc Plugin](https://maven.apache.org/plugins/maven-javadoc-plugin/)
* [Generate Javadocs](https://maven.apache.org/plugins/maven-javadoc-plugin/usage.html)
* [Javadoc Plugin Documentation](https://maven.apache.org/plugins/maven-javadoc-plugin/javadoc-mojo.html)


