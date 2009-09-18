package fr.grid5000.api;
import fr.grid5000.api.errors.HTTPClientError;
import fr.grid5000.api.errors.HTTPServerError;

import java.util.Iterator;
import java.util.Map;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import javax.ws.rs.core.MediaType;


public class Connection {
	private URI uri;
	private Client client;
	
	public Connection(String url, Map<String, String> options) throws URISyntaxException, GeneralSecurityException {
		uri = new URI(url);
		// http://www.nakov.com/blog/2009/07/16/disable-certificate-validation-in-java-ssl-connections/
	    // Create a trust manager that does not validate certificate chains  
	    TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {  
	            public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
	                return null;  
	            }  
	            public void checkClientTrusted(X509Certificate[] certs, String authType) {  
	            }  
	            public void checkServerTrusted(X509Certificate[] certs, String authType) {  
	            }  
	        }  
	    };  

	    // Install the all-trusting trust manager  
	    SSLContext sc = SSLContext.getInstance("SSL");  
	    sc.init(null, trustAllCerts, new java.security.SecureRandom());  
	    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());  
	      
	    // Create all-trusting host name verifier  
	    HostnameVerifier allHostsValid = new HostnameVerifier() {  
	        public boolean verify(String hostname, SSLSession session) {  
	            return true;
	        }  
	    };  
	      
	    // Install the all-trusting host verifier  
	    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);  

		client = Client.create();
		if (options.containsKey("username") && options.containsKey("password")) {
			HTTPBasicAuthFilter authFilter = new HTTPBasicAuthFilter(options.get("username"), options.get("password"));
			client.addFilter(authFilter);
		}
		
	}
	
	/**
	 * GET a resource and return the raw response .
	 * @param path the path to the resource, to be joined to the connection's uri (if no scheme is present) or used as itself.
	 * @param params a hash of query parameters.
	 * @param headers a hash of custom HTTP headers to send with the query.
	 * @return ClientResponse
	 */
	public ClientResponse get(String path, Map<String, String> params, Map<String, String> headers) throws HTTPClientError, HTTPServerError {
		URI location = this.uri.resolve(path);
		WebResource webResource = client.resource(location.toString());
		MultivaluedMapImpl queryParams = new MultivaluedMapImpl();
		Iterator<String> iquery = params.keySet().iterator();
		while(iquery.hasNext()) {
			String key = iquery.next();
			queryParams.add(key, params.get(key));
		}
		Iterator<String> iheader = headers.keySet().iterator();
		while(iheader.hasNext()) {
			String key = iheader.next();
			webResource.header(key, headers.get(key));
		}
		ClientResponse response = webResource.queryParams(queryParams).accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		return response;
	}

}
