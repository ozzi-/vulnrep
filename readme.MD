# VulnRep - Vulnerability Reporting
This tool will collect all vulnerabilities (from vulners.com and/or wpvulndb.com) for defined keywords generate a HTML report.
Additionally, the report will be sent as an email to defined recipients.

![screenshot of functionality](https://i.imgur.com/7f9JGhz.png)

Here you can see how the email report will look like:
![screenshot of report](https://i.imgur.com/S2t21Zw.png)

# Setup

## API Key
First of all, you will need to get yourself some API Keys.

### Vulners
Create a vulners account then navigate to https://vulners.com/userinfo.
Unter the tab "API KEYS", select scope "api", license "free" and enter your public IP where vulnrep will be running.
Copy the API key and paste it into "subscriptions.json" under "apikeyVulners".

### WPVulnDB
Create a WPVulnDB account then navigate to https://wpvulndb.com/users/edit.
Copy / Generate the API key and paste it into "subscriptions.json" under "apikeyWPVulnDB".

## Configuration
All configuration is done in three JSON files, "subscriptions.json", "history.json" and "email.json".
Those JSON files need to be in the same directory as the JAR.
### subscriptions
Subscriptions contains your API keys as well all of the  wordpress plugins and keywords you are interested in.
```json
{
        "apikeyVulners": "****************",
        "apikeyWPVulnDB": "****************",
        "entries":[
                {
                        "name":"custom_wpvulndb_plugin" ,
                        "plugins":[
							"duplicate-post","polylang","post-types-definitely","regenerate-thumbnails"
						]
                },{
                        "name":"chrome" ,
                        "cvss": 5.0
                },{
                        "name":"firefox" ,
                        "cvss": 5.0
                }
        ]
}
```
### email
This JSON contains all settings related to the e-mail report.
```json
{
        "recipients":[ "recipient@domain.ch" ],
        "sender": {
                "address": "sender@domain.ch",
                "host": "smtp.domain.ch",
                "port":25,
                "user":"sender@domain.ch",
                "password":"*****************",
                "subject":"Vulnerability Report <d>"
        }
}
```
### history
This JSON file will store all reported vulnerabilities 
```json
{
  "history": [
  ],
  "deleteAfter": {
    "days": 60
  }
}
```
## Running VulnRep
```sh
java -jar VulnerabilityReporting.jar 5d
```
Where 5d means get me all the vulnerabilities published in the last 5 days of the keywords defined in subscriptions.json, which I have not seen yet. You can use h for hours and m for minutes too. The report HTML file will be saved in the same directory the JAR resides in.

### Creating a job
In order to automate this, I use a cron job:
```sh
$ env EDITOR=nano crontab -e

# run every sunday morning at 0700
0 7 * * 0  cd /opt/vulnrep; java -jar VulnerabilityReporting.jar
```
