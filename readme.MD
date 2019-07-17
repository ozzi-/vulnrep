# VulnRep - Vulnerability Reporting
This tool will collect all vulnerabilities (from vulners.com and/or wpvulndb.com) for defined keywords generate a HTML report.
Additionally, the report will be sent as an email to the recipients defined in email.json.

![screenshot of report](https://i.imgur.com/S2t21Zw.png)

# Usage

## API Key
### Vulners
Create a vulners account then navigate to https://vulners.com/userinfo.
Unter the tab "API KEYS", select scope "api", license "free" and enter your public IP where vulnrep will be running.
Copy the API key and paste it into "subscriptions.json" under "apikeyVulners".

### WPVulnDB
Create a WPVulnDB account then navigate to https://wpvulndb.com/users/edit.
Copy / Generate the API key and paste it into "subscriptions.json" under "apikeyWPVulnDB".

## Configuration
In subscriptions.json, you can define keywords which will be used to find vulnerabilities.
Additionally, a minimum CVSS score can be defined.
In email.json you can define your smtp settings for the report delivery.

## Running VulnRep
```sh
java -jar VulnerabilityReporting.jar 5d
```
Where 5d means get me all the vulnerabilities published in the last 5 days of the keywords defined in subscriptions.json, which i have not seen yet. You can use h for hours and m for minutes too.

## Creating a job
In order to automate this, i use a cron job:
```sh
$ env EDITOR=nano crontab -e

# run every sunday morning at 0700
0 7 * * 0  cd /etc/vulnrep; java -jar VulnerabilityReporting.jar
```

# Building a runnable JAR from Source
1. Open project in Eclipse
2. Right click project -> Export
3. Runnable JAR
4. Export Destination needs to end with 'VulnerabilityReporting.jar'
5. Library Handling -> Package required libraries into generated JAR
6. Remove the configuration JSON files from the JAR and place them next to the JAR using the following commands (or use cleanJar.sh)
```sh
unzip -o VulnerabilityReporting.jar subscriptions.json
unzip -o VulnerabilityReporting.jar history.json
unzip -o VulnerabilityReporting.jar email.json
zip -d VulnerabilityReporting.jar subscriptions.json
zip -d VulnerabilityReporting.jar history.json
zip -d VulnerabilityReporting.jar email.json
```
