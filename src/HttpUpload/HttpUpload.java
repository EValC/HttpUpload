package HttpUpload;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
public class HttpUpload
{
private final String boundary;
private static final String LINE_FEED = "\r\n";
private static final int BUFFER_SIZE = 4096 ;
private HttpURLConnection httpConn;
private String charset;
private OutputStream outputStream;
private PrintWriter writer;
public HttpUpload(String requestURL, String charset) throws IOException 
{
	this.charset = charset;
	boundary ="WebKitFormBoundary" + Long.toHexString(System.currentTimeMillis());
	URL url = new URL(requestURL);
	httpConn = (HttpURLConnection) url.openConnection();
	//httpConn.setRequestMethod("POST");
	httpConn.setUseCaches(false);
	httpConn.setDoOutput(true);
	//httpConn.setDoInput(true);
	httpConn.setRequestProperty("Content-Type","multipart/form-data; boundary=----" + boundary);
    httpConn.setRequestProperty("X-COUPA-API-KEY","096eff7825f0a7c6914f03af66dfb7ce3a5a2dc2");
    httpConn.setRequestProperty("accept","application/xml");
    //httpConn.setRequestProperty("attachment[file]",requestURL);
    outputStream = httpConn.getOutputStream();
	writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),true);
	System.out.println("axios");
}
public void addFormField(String name, String value)
	{
		writer.append("----" + boundary).append(LINE_FEED);
		writer.append("Content-Disposition: form-data; name=\"" + name + "\"").append(LINE_FEED);
		writer.append("Content-Type: text/plain; charset=" + charset).append(LINE_FEED);
		writer.append(LINE_FEED);
		writer.append(value).append(LINE_FEED);
		writer.flush();
	}
public void addFilePart(String fieldName, File imageo) throws IOException 
	{
		String fileName = imageo.getName();
		writer.append("----" + boundary).append(LINE_FEED);
		writer.append("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"").append(LINE_FEED);
		writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED);
		writer.append("fileUpload:"+fileName).append(LINE_FEED);
		writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
		writer.append(LINE_FEED);
		FileInputStream inputStream = new FileInputStream(imageo);
		byte[] buffer = new byte[BUFFER_SIZE];
		int bytesRead = -1;
		while ((bytesRead = inputStream.read(buffer)) != -1) 
		{
			outputStream.write(buffer, 0, bytesRead);
		}
		outputStream.flush();
		inputStream.close();
		writer.append(LINE_FEED);
		writer.flush();		
	}
public void addHeaderField(String name, String value) 
	{
		writer.append(name + ": " + value).append(LINE_FEED);
		writer.flush();
	}
public List<String> finish() throws IOException 
	{
		List<String> response = new ArrayList<String>();
		writer.append(LINE_FEED).flush();
		writer.append("----" + boundary).append(LINE_FEED);
		writer.close();
		int status = httpConn.getResponseCode();
		if (status == HttpURLConnection.HTTP_OK) 
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
			String res = null;
			while ((res = reader.readLine()) != null) 
			{
				response.add(res);
			}
			reader.close();
			httpConn.disconnect();
		} else 
		{
			throw new IOException("Server returned non-OK status: " + status);
		}
		return response;
	}
}
