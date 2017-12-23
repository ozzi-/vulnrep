package helpers;

public class Convert {
	public static String convertStreamToString(java.io.InputStream is) {
	    if (is == null) {
	        return "";
	    }

	    java.util.Scanner s = new java.util.Scanner(is);
	    s.useDelimiter("\\A");

	    String streamString = s.hasNext() ? s.next() : "";

	    s.close();

	    return streamString;
	}

}
