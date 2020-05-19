# VulnRep - Vulnerability Reporting
This tool will collect all vulnerabilities (from vulners.com and/or wpvulndb.com) for defined keywords generate a HTML report.
Additionally, the report will be sent as an email to the recipients defined in email.json.

![screenshot of functionality](https://i.imgur.com/7f9JGhz.png)

Here you can see how the email report will look like:
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
Where 5d means get me all the vulnerabilities published in the last 5 days of the keywords defined in subscriptions.json, which I have not seen yet. You can use h for hours and m for minutes too.

## Creating a job
In order to automate this, I use a cron job:
```sh
$ env EDITOR=nano crontab -e

# run every sunday morning at 0700
0 7 * * 0  cd /opt/vulnrep; java -jar VulnerabilityReporting.jar
```
