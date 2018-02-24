package com.hundun.common.utils;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpStatus;

/**
 * @DESC HTTP Request utils by urlconnection
 * @author saic_xinshiyou
 */
public class HttpsUtil {

	/** @DESC http post method for http request */
	public static String sendPost(String inputURL, String charset, Map<String, String> params) throws Exception {

		StringBuffer sb = new StringBuffer();
		String postDT = null;
		if (null != params && params.size() > 0) {
			for (String key : params.keySet())
				sb.append(key + "=" + params.get(key) + "&");
			postDT = sb.toString().substring(0, sb.length() - 1);
		}

		return sendPost(inputURL, charset, postDT, false);
	}

	/** @DESC http get method for http request */
	public static String sendPost(String inputURL, String charset, String params, boolean isHttps) throws Exception {

		URLConnection httpConn = getConnection(inputURL, false);
		httpConn = setCommonProperties(httpConn, charset);
		if (isHttps)
			httpConn = addHttpsSetting(httpConn);
		httpConn.setDoOutput(true);

		if (null != params)
			httpConn.getOutputStream().write(params.getBytes());

		return validateResponse(httpConn, charset);
	}

	/** @DESC http get method for http request */
	public static String sendGet(String inputURL, String charset, Map<String, String> reqProperty) throws Exception {
		return sendGet(inputURL, charset, reqProperty, false);
	}

	/** @DESC http get method for http request */
	public static String sendGet(String inputURL, String charset, Map<String, String> reqProperty, boolean isHttps)
			throws Exception {

		URLConnection httpConn = getConnection(inputURL, false);
		httpConn = setCommonProperties(httpConn, charset);

		// add req property to http connection
		if (null != reqProperty)
			for (String key : reqProperty.keySet())
				httpConn.setRequestProperty(key, reqProperty.get(key));

		if (isHttps)
			httpConn = addHttpsSetting(httpConn);

		return validateResponse(httpConn, charset);
	}

	/** @DESC get connection by proxy or not */
	public static URLConnection getConnection(String inputURL, boolean useProxy, String... proxys) throws IOException {
		URLConnection httpConn = null;
		URL url = new URL(inputURL);

		if (useProxy) {
			System.setProperty("http.proxyHost", proxys[0]);
			System.setProperty("http.proxyPort", proxys[1]);
			Authenticator.setDefault(new BasicAuthenticator(proxys[2], proxys[3]));
			java.net.Proxy proxy = new java.net.Proxy(java.net.Proxy.Type.HTTP,
					new InetSocketAddress(proxys[0], Integer.parseInt(proxys[1])));
			httpConn = url.openConnection(proxy);
		} else
			httpConn = url.openConnection();

		return httpConn;
	}

	/** @DESC set common properties for http connection */
	private static URLConnection setCommonProperties(URLConnection httpConn, String charset) {

		httpConn.setRequestProperty("Accept-Charset", charset);
		httpConn.setRequestProperty("contentType", charset);
		httpConn.setRequestProperty("accept", "*/*");
		httpConn.setRequestProperty("Accept-Encoding", "gzip");
		httpConn.setRequestProperty("Content-Encoding", "gzip");

		return httpConn;
	}

	/** @DESC check response code status is HttpStatus.OK or not  */
	public static String validateResponse(URLConnection httpConn, String charset) throws IOException, Exception {

		// check response code status
		String value = httpConn.getHeaderField(0);
		int responseCode = 404;
		if (null != value)
			responseCode = Integer.parseInt(value.split("\\s+")[1]);
		System.out.println("CODE:" + responseCode);
		if (HttpStatus.SC_OK == responseCode) {
			byte[] response = null;
			// gzip compress type
			if ("gzip".equals(httpConn.getContentEncoding()))
				response = IOUtil.readStream(new GZIPInputStream(httpConn.getInputStream()));
			else
				response = IOUtil.readStream(httpConn.getInputStream());
			return new String(response, charset);
		}
		String response = new String(IOUtil.readStream(httpConn.getInputStream()));
		System.out.println("Code:" + response);

		return null;
	}

	/** add https properties */
	public static URLConnection addHttpsSetting(URLConnection conn) {

		// may be exists: class cast exception
		HttpsURLConnection httpsConn = (HttpsURLConnection) conn;

		SSLContext ctx = null;
		try {
			ctx = SSLContext.getInstance("TLS");
			ctx.init(new KeyManager[0], new TrustManager[] { new DefaultTrustManager() }, new SecureRandom());
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		SSLSocketFactory ssf = ctx.getSocketFactory();
		httpsConn.setSSLSocketFactory(ssf);
		httpsConn.setHostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String arg0, SSLSession arg1) {
				return true;
			}
		});

		return httpsConn;
	}
}

class BasicAuthenticator extends Authenticator {
	String userName;
	String password;

	public BasicAuthenticator(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	/**
	 * Called when password authorization is needed. Subclasses should override
	 * the default implementation, which returns null.
	 *
	 * @return The PasswordAuthentication collected from the user, or null if
	 *         none is provided.
	 */
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName, password.toCharArray());
	}
}

class DefaultTrustManager implements X509TrustManager {
	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}
}
