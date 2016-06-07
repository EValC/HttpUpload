package HttpUpload;
import java.io.File;
import java.io.IOException;
import java.util.List;
public class HttpPost 
{
 public static void main(String[] args) 
	 {
	   String charset = "UTF-8";
	   String requestURL = "https://knott-demo.coupacloud.com/api/invoices/81/attachments";	
	   File imageo = new File("C://Z/coupa.jpg");       
       try 
	        {
	            HttpUpload upload = new HttpUpload(requestURL, charset);
	            upload.addFormField("description", "uploading images");
	            upload.addFilePart("fileUpload", imageo);
	            upload.addHeaderField("User-Agent",  "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36");
	            List<String> response = upload.finish();	             
	            System.out.println("Served Replied:");	             
	            for (String res : response) 
	            {
	                System.out.println(res);
	            }
	         } 
	        catch (IOException ex) 
	        {
	            System.err.println(ex);
	        }
	    }
}