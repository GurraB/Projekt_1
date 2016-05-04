package se.mah.projekt_1;

import android.provider.MediaStore;
import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Gustaf on 03/05/2016.
 */
public class ServerCommunicationService {

    public Account login(String url, String username, String password) throws RuntimeException {
        return login(url, encryptAuthentication(username, password));
    }

    public Account login(String url, String encryptedUserCredentials) throws RuntimeException {
        RestTemplate restTemplate = createRestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + encryptedUserCredentials);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Map.class);

        LinkedHashMap<String, Object> responseMap = (LinkedHashMap<String, Object>) response.getBody();
        LinkedHashMap<String, Object> accountMap = (LinkedHashMap<String, Object>) responseMap.get("principal");

        Account acc = new Account();
        acc.createAccountFromMap(accountMap, encryptedUserCredentials);
        return acc;
    }

    public AndroidStamp[] getStampsForUser(String url, String encryptedUserCredentials, String rfid, String from, String to) {
        RestTemplate restTemplate = createRestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + encryptedUserCredentials);

        //Temporary url TODO change to URIComponentBuilder
        url += "&";
        url += "from=" + from;
        url += "&to=" + to;
        url += "&rfid=" + rfid;
        Log.v("HEADER STRING", headers.toString());

        HttpEntity<?> requestEntity = new HttpEntity<Object>(headers);
        ResponseEntity<AndroidStamp[]> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, AndroidStamp[].class);
        AndroidStamp[] stamps = response.getBody();

        return stamps;
    }

    private RestTemplate createRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        SimpleClientHttpRequestFactory requestFactory = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
        requestFactory.setReadTimeout(3000);    //Timeout after 3 seconds
        requestFactory.setConnectTimeout(3000);
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
