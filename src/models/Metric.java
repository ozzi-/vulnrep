package models;

public class Metric {
	private String metric;
	private String value;

	public String getMetric() {
		return metric;
	}

	public void setMetric(String metric) {
		this.metric = metric;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String toFormattedString() {
		String metricL = metric.toLowerCase();
		String valueF = value.substring(0, 1) + value.substring(1).toLowerCase();
		String metricF = "";
		switch (metricL) {
		case "av":
			metricF = "Attack Vector";
			break;
		case "ac":
			metricF = "Attack Complexity";
			break;
		case "pr":
			metricF = "Privileges Required";
			break;
		case "ui":
			metricF = "User Interaction";
			break;
		case "s":
			metricF = "Scope";
			break;
		case "c":
			metricF = "Confidentiality";
			break;
		case "i":
			metricF = "Integrity";
			break;
		case "a":
			metricF = "Availability";
			break;
		case "e":
			metricF = "Exploit Code Maturity";
			break;
		case "rl":
			metricF = "Remediation Level";
		case "rc":
			metricF = "Report Confidence";
		}
		return metricF + ": " + valueF;
	}

}
