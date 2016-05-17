package se.mah.projekt_1;

import android.net.SSLCertificateSocketFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import org.springframework.http.HttpAuthentication;
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

    private Controller controller;

    public ServerCommunicationService(Controller controller) {
        this.controller = controller;
    }

    public Account login(String url, String username, String password) throws RuntimeException {
        return login(url, encryptAuthentication(username, password));
    }

    private void showConnectionErrorMessage(final Controller.ErrorType type, final String message) {
        ((AppCompatActivity)controller.getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                controller.showConnectionErrorMessage(type, message);
            }
        });
    }

    public Account login(String url, String encryptedUserCredentials) throws RuntimeException {
        RestTemplate restTemplate = createRestTemplate();
        HttpEntity<String> requestEntity = createHttpEntity(encryptedUserCredentials);

        Account acc = null;
        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Map.class);
            LinkedHashMap<String, Object> responseMap = (LinkedHashMap<String, Object>) response.getBody();
            LinkedHashMap<String, Object> accountMap = (LinkedHashMap<String, Object>) responseMap.get("principal");
            acc = new Account();
            acc.createAccountFromMap(accountMap, encryptedUserCredentials);
            Log.v("ACCOUNT", acc.toString());
        } catch (Exception e) {
            Log.e("LOGIN_EXCEPTION", e.getMessage());
            if(e.getMessage().equals("401 Unauthorized"))
                showConnectionErrorMessage(Controller.ErrorType.UNATHORIZED, "");
            else
                showConnectionErrorMessage(Controller.ErrorType.CONNECTION_ERROR, "");
        }
        return acc;
    }

    public AndroidStamp[] getStampsForUser(String url, String encryptedUserCredentials, String rfid, String from, String to) {

        RestTemplate restTemplate = createRestTemplate();
        HttpEntity<String> requestEntity = createHttpEntity(encryptedUserCredentials);

        //Temporary url TODO change to URIComponentBuilder
        url += "&";
        url += "from=" + from;
        url += "&to=" + to;
        url += "&rfid=" + rfid;


        AndroidStamp[] stamps = new AndroidStamp[0];
        try {
            ResponseEntity<AndroidStamp[]> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, AndroidStamp[].class);
            stamps = response.getBody();
        } catch (Exception e) {
            showConnectionErrorMessage(Controller.ErrorType.CONNECTION_ERROR, "");
        }
        return stamps;
    }

    public ScheduleStamp[] getScheduleForUser(String url, String encryptedUserCredentials, String id, String from, String to) {
        RestTemplate restTemplate = createRestTemplate();
        HttpEntity<String> requestEntity = createHttpEntity(encryptedUserCredentials);

        //Temporary url TODO change to URIComponentBuilder
        /*url += "&";
        url += "from=" + from;
        url += "&to=" + to;
        url += "&id=" + id;*/
        url = buildURI(url, from, to, id);

        ScheduleStamp[] stamps = new ScheduleStamp[0];
        try {
            ResponseEntity<ScheduleStamp[]> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, ScheduleStamp[].class);
            stamps = response.getBody();
        } catch (Exception e) {
            showConnectionErrorMessage(Controller.ErrorType.CONNECTION_ERROR, "");
        }
        return stamps;
    }

    private RestTemplate createRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate;
    }

    private HttpEntity createHttpEntity(String encryptedUserCredentials) {
        HttpAuthentication auth = new HttpBasicAuthentication(encryptedUserCredentials);
        HttpHeaders headers = new HttpHeaders();
        headers.setAuthorization(auth);
        return new HttpEntity(headers);
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
        map.put("id", param);
        UriComponents uriComp = UriComponentsBuilder.fromUriString(uri).queryParams(map).build();
        String completeUri = uriComp.toString().replace('?', '&');
        return completeUri;
    }
}
