package helpers;

public class ErrorReporter {
	public static boolean failed=false;
	public static String errorMessage="";
	
	public static void handleError(String error, Exception e) {
		System.err.println(error);
		errorMessage+=error+" - "+e.getClass().toString()+"\r\n";
		ErrorReporter.failed=true;
		e.printStackTrace();
	}
}
