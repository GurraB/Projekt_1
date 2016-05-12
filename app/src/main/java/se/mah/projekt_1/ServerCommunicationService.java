package se.mah.projekt_1;

import android.net.SSLCertificateSocketFactory;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;
import javax.security.cert.CertificateExpiredException;

/**
 * Created by Gustaf on 03/05/2016.
 */
public class ServerCommunicationService {

    public Account login(String url, String username, String password) throws RuntimeException {
        return login(url, encryptAuthentication(username, password));
    }

    public static void trustSelfSignedSSL() {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {

                public void checkClientTrusted(X509Certificate[] xcs, String string) {
                }

                public void checkServerTrusted(X509Certificate[] xcs, String string) {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLContext.setDefault(ctx);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Account login(String url, String encryptedUserCredentials) throws RuntimeException {
        BasicAuthRestTemplate restTemplate = createRestTemplate(encryptedUserCredentials);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + encryptedUserCredentials);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Map.class);
        LinkedHashMap<String, Object> responseMap = (LinkedHashMap<String, Object>) response.getBody();
        LinkedHashMap<String, Object> accountMap = (LinkedHashMap<String, Object>) responseMap.get("principal");

        Account acc = new Account();
        acc.createAccountFromMap(accountMap, encryptedUserCredentials);
        Log.v("ACCOUNT", acc.toString());
        return acc;
    }

    public AndroidStamp[] getStampsForUser(String url, String encryptedUserCredentials, String rfid, String from, String to) {

        BasicAuthRestTemplate restTemplate = createRestTemplate(encryptedUserCredentials);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + encryptedUserCredentials);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        //Temporary url TODO change to URIComponentBuilder
        url += "&";
        url += "from=" + from;
        url += "&to=" + to;
        url += "&rfid=" + rfid;
        Log.v("HEADER STRING", headers.toString());

        ResponseEntity<AndroidStamp[]> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, AndroidStamp[].class);
        AndroidStamp[] stamps = response.getBody();

        return stamps;
    }

    public ScheduleStamp[] getScheduleForUser(String url, String encryptedUserCredentials, String rfid, String from, String to) {
        BasicAuthRestTemplate restTemplate = createRestTemplate(encryptedUserCredentials);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + encryptedUserCredentials);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        //Temporary url TODO change to URIComponentBuilder
        url += "&";
        url += "from=" + from;
        url += "&to=" + to;
        url += "&id=" + rfid;
        Log.v("HEADER STRING", headers.toString());

        ResponseEntity<ScheduleStamp[]> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, ScheduleStamp[].class);
        ScheduleStamp[] stamps = response.getBody();
        return stamps;
    }

    private BasicAuthRestTemplate createRestTemplate(String encryptedUserCredentials) {
        BasicAuthRestTemplate restTemplate = new BasicAuthRestTemplate(encryptedUserCredentials);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        trustSelfSignedSSL();
        return restTemplate;
    }

    private String encryptAuthentication(String username, String password) {
        String plainCreds = username + ":" + password;
        byte[] plainCredsByte = plainCreds.getBytes();
        byte[] base64 = Base64Utils.encode(plainCredsByte);
        return new String(base64);
    }

    private String buildURI(String uri, String from, String to, String rfid) {
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        ArrayList<String> param = new ArrayList<>();
        param.add(from);
        map.put("from", param);
        param.set(0, to);
        map.put("to", param);
        param.set(0, rfid);
        map.put("rfid", param);
        UriComponents uriComp = UriComponentsBuilder.fromUriString(uri).queryParams(map).build();
        return uriComp.toString();
    }
}
