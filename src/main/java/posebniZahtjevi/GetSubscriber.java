package posebniZahtjevi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class GetSubscriber {
	private static final String USER_AGENT = "Mozilla/5.0";

	public boolean provjera(String dn, String areaCode) throws IOException {

		// POCETAK <====Trust code za SSL sertifikat ====>

		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}
		} };

		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
		}

		// KRAJ trust code

		boolean potvrda = false;
		GetSubscriberReq getSub = new GetSubscriberReq();
		String url = "https://mnsvgp.telrad.loc/openmn/nb/NBProvisioningWebService";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		String encoded = Base64.getEncoder()
				.encodeToString(("sysadmin" + ":" + "sysadmin").getBytes(StandardCharsets.UTF_8));

		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Content-Type", "text/xml");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Authorization", "Basic " + encoded);

		String urlParameters = getSub.getData("1000", areaCode, dn);
		//System.out.println("POSLAO " + urlParameters);

		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		//System.out.println("ODGOVOR   " + response.toString());
		
		
			if (response.toString().toLowerCase().contains("error")) {
				potvrda = false;
			} else {
				potvrda = true;
			}
		

		return potvrda;
	}
}
