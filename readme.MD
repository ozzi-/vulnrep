# VulnRep - Vulnerability Reporting
This tool will collect ) all vulnerabilities (from vulners.com and/or wpvulndb.com) for a defined keywords and generate a HTML report.
Additionally, the report will be sent as an email to the recipients defined in email.json.

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
# Running the program
```sh
java -jar VulnerabilityReporting.jar 5d
```
Where 5d means get me all the vulnerabilities published in the last 5 days of the keywords defined in subscriptions.json, which i have not seen yet. You can use h for hours and m for minutes too.

# Screenshot
![screenshot of report](https://i.imgur.com/S2t21Zw.png)


# Setup
In order to automate this, i use a cron job:
```sh
$ env EDITOR=nano crontab -e

# run every sunday morning at 0700
0 7 * * 0  cd /Library/Server/Web/Data/Sites/Security/; /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/bin/java -jar /Library/Server/Web/Data/Sites/Security/VulnerabilityReporting.jar
```
