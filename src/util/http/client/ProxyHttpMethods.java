package util.http.client;


import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;


/**
 * Clase proxy para la ejecucion de peticiones HTTP.
 * 
 * Ejemplo de uso:
 * {@code 
 		IHttpMethods httpMethods = new ProxyHttpMethods("ISO-8859-1");
		String respGet = httpMethods.doGet(urlGet);
		String respPost = httpMethods.doPost(urlPost);
		httpMethods = null;
	}
 *
 */
public class ProxyHttpMethods implements IHttpMethods {

	private IHttpMethods realObject = null;
	
	/**
	 * 
	 * @param charset Valor que determina que charset se utilizara en los encodes.
	 * 				Por ejemplo: "ISO-8859-1" , "UTF-8"
	 */
	public ProxyHttpMethods(String charset) {
		realObject = new ApacheHttpMethods(charset);
	}
	public ProxyHttpMethods(String charset, boolean isTraza) {
		realObject = new ApacheHttpMethods(charset,isTraza);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String doPost(String url, List<NameValuePair> params, Map<String, String> headers, String payload) throws Exception {
		return realObject.doPost(url, params, headers, payload);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String doPost(IHttpClient httpClientBuilder, String url, List<NameValuePair> params, Map<String, String> headers, String payload) throws Exception {
		return realObject.doPost(httpClientBuilder, url, params, headers, payload);
	}

	/**
	 * {@inheritDoc}
	 */
	public String doPut(String url, Map<String, String> params, Map<String, String> headers, String payload) throws Exception {
		return realObject.doPut(url, params, headers, payload);
	}

	/**
	 * {@inheritDoc}
	 */
	public String doGet(String url, Map<String, String> params, Map<String, String> headers) throws Exception {
		return realObject.doGet(url, params, headers);
	}

}
