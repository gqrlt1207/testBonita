package xyg.app;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Base64;


public class muleRequest {
  private static String requestUrl;
  private static String credential;

  public muleRequest(String requesturl, String Credential) {
    requestUrl=requesturl;
    credential=Credential;
    System.out.println("\n request url:"+requestUrl+"\n");
  }


  public String sendRequest() {
  /*TrustManager[] trustAllCerts = new TrustManager[] {
    new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
        public void checkClientTrusted(
            java.security.cert.X509Certificate[] certs, String authType) {
            }
        public void checkServerTrusted(
            java.security.cert.X509Certificate[] certs, String authType) {
        }
    }
    };*/


   try {
    //SSLContext sc = SSLContext.getInstance("SSL");
    //sc.init(null, trustAllCerts, new java.security.SecureRandom());
    //HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    URL url = new URL(requestUrl);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    String usernameColonPassword = credential;
    String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString(usernameColonPassword.getBytes());
    //connection.setDoInput(true);
    //connection.setDoOutput(true);
    connection.setRequestMethod("GET");
    //connection.setRequestProperty("Accept", "application/json");
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestProperty("Authorization", basicAuthPayload);
    //connection.setRequestProperty("X-Requested-With", "Curl");
    //OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
    //writer.write(payload);
    //writer.close();
    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    StringBuffer jsonString = new StringBuffer();
    String line;
    while ((line = br.readLine()) != null) {
                jsonString.append(line);
    }
    br.close();
    connection.disconnect();
    return jsonString.toString();
    } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
    }

    }

} 
