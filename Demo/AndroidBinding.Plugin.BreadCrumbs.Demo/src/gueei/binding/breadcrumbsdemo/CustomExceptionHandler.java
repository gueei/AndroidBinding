package gueei.binding.breadcrumbsdemo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;

import android.os.Environment;

// http://stackoverflow.com/questions/601503/how-do-i-obtain-crash-data-from-my-android-application

public class CustomExceptionHandler implements UncaughtExceptionHandler { 
	 
    private UncaughtExceptionHandler defaultUEH;  
    private String localPath;  
    private String url; 
    public static String logFile = "exception.log"; 
 
    /*  
     * if any of the parameters is null, the respective functionality  
     * will not be used  
     */ 
    public CustomExceptionHandler() {
    	File f = Environment.getExternalStorageDirectory();
    	if( f != null )    	
    		this.localPath = f.getPath(); 
    } 
 
    public void uncaughtException(Thread t, Throwable e) { 
        final Writer result = new StringWriter(); 
        final PrintWriter printWriter = new PrintWriter(result); 
        e.printStackTrace(printWriter); 
        String stacktrace = result.toString(); 
        printWriter.close(); 
 
        if (localPath != null) { 
            writeToFile(stacktrace, logFile); 
        } 
        if (url != null) { 
            sendToServer(stacktrace, logFile); 
        } 
 
        defaultUEH.uncaughtException(t, e); 
    } 
 
    private void writeToFile(String stacktrace, String filename) { 
        try { 
            BufferedWriter bos = new BufferedWriter(new FileWriter( 
                    localPath + "/" + filename)); 
            bos.write(stacktrace); 
            bos.flush(); 
            bos.close(); 
        } catch (Exception e) { 
        	// this is the end...
            e.printStackTrace(); 
        } 
    } 
 
    private void sendToServer(String stacktrace, String filename) { 
    	/*
        DefaultHttpClient httpClient = new DefaultHttpClient(); 
        HttpPost httpPost = new HttpPost(url); 
        List<NameValuePair> nvps = new ArrayList<NameValuePair>(); 
        nvps.add(new BasicNameValuePair("filename", filename)); 
        nvps.add(new BasicNameValuePair("stacktrace", stacktrace)); 
        try { 
            httpPost.setEntity( 
                    new UrlEncodedFormEntity(nvps, HTTP.UTF_8)); 
            httpClient.execute(httpPost); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
        */
    } 
    
    public static Date getCrashFileDate() {
    	try
    	{
    		File f = new File(Environment.getExternalStorageDirectory(), logFile); 
    		if( f != null && f.exists() && f.lastModified() > 0) {
    			long modified = f.lastModified();
    			return new Date(modified);
    		}
    	} catch (Exception e) {
    	}
		return null;
    }
} 

