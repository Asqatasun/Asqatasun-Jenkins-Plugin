# Configuration of Asqatasun-Jenkins-plugin

- Go to **Jenkins** > **Manage Jenkins** > **Configure system**
- Go down to the **Asqatasun Runner** section

![](Images/screenshot_20150217_ASQATASUN_jenkins_configuration.png)

## Path to the Asqatasun-Runner installation (Mandatory)

Path to the local Asqatasun-Runner

Example:

```bash
/opt/asqatasun-cli/
```

## Display port (Mandatory)

Value of the display port configured for the [Xvfb startup script](20-install-doc.md#Xvfb).

Example:

```bash
98
```

**Note:** you must not have colon ":" in this value?

## Path to Firefox binary (Mandatory)

Full path to access the binary of the Firefox installed.

Example:

```bash
/opt/firefox-31esr/firefox
```

**Note:** This is NOT the path (`/opt/firefox/`) that but actually the full path with binary name included (`/opt/firefox/firefox`)

## Asqatasun webapp URL (Mandatory)

The URL where the detailed results of an audit could be viewed.

Examples :

```bash
http://my-asqatasun-host.internal.company.com/
```

```bash
http://my-asqatasun-host.preprod.company.com:8080/asqatasun/
```

**Note:** This value should never be `localhost` (except for a single host install on a laptop, that may travel).
For a multiple host, this must be the DNS name of the Asqatasun Host (not the Jenkins host) (This is used only to create a link to access the detailed results of an audit.)

## Database information (Mandatory)

All the information to access the MySql.

In a Multiple host installation, this is the "shared" MySql between the Jenkins host and the Asqatasun host

### Database hostname (Mandatory)

Hostname of the server hosting the MySql database

```bash
my-sql-host.internal.company.com
```

### Database port (Mandatory)

Port to connect to on the MySql host

```bash
3306
```

### Database name (Mandatory)

Name of the database used for Asqatasun

```bash
my-asqatasun-dbname
```

### Database login (Mandatory)

Username to log in Mysql

```bash
my-asqatasun-db-username
```

### Database password (Mandatory)

Password to log in MySql

```bash
my-asqatasun-db-passwd
```

## Asqatasun account login (Mandatory)

The account to whom the contracts will be attached to. This user must **have been created before** on the Asqatasun host.

```bash
my-jenkins-user@internal.company.com
```

**Note:** Each (Jenkins) Item will automatically create a contract for this user,
just after the first build. The contract name will be the item name. The contract
will have the scenario feature activated. The scenario name will be the one given
in the configuration of the item (Build section).

## Next step

proceed to [Configuration of Item](35-configuration-of-item.md)
