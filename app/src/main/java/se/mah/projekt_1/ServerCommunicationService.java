package se.mah.projekt_1;

import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Gustaf on 03/05/2016.
 */
public class ServerCommunicationService {

    public Account login(String url, String username, String password) throws RuntimeException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        String plainCreds = username + ":" + password;
        username = password = null;
        byte[] plainCredsByte = plainCreds.getBytes();
        byte[] base64 = Base64Utils.encode(plainCredsByte);
        String encryptedString = new String(base64);
        headers.add("Authorization", "Basic " + encryptedString);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        Log.v("SERVERCOMMUNICATIONSERV", "INB4 THE LOGIN");
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Map.class);
        Log.v("SERVERCOMMUNICATIONSERV", "AFTER THE LOGIN");

        LinkedHashMap<String, Object> responseMap = (LinkedHashMap<String, Object>) response.getBody();
        LinkedHashMap<String, Object> accountMap = (LinkedHashMap<String, Object>) responseMap.get("principal");
        LinkedHashMap<String, Object> rfidMap = (LinkedHashMap<String, Object>) accountMap.get("rfidKey");
        Account acc = new Account();

        acc.setFirstName((String) accountMap.get("firstName"));
        acc.setLastName((String) accountMap.get("lastName"));
        acc.setId((String) accountMap.get("id"));
        acc.setAccountNonExpired((Boolean) accountMap.get("accountNonExpired"));
        acc.setIsEnabled((Boolean) accountMap.get("enabled"));
        acc.setRfidKey(new RfidKey((String) rfidMap.get("id")));
        return acc;
    }

    public AndroidStamp[] getStampsForUser(String url, String username, String password, String rfid, long from, long to) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        String plainCreds = username + ":" + password;
        username = password = null;
        byte[] plainCredsByte = plainCreds.getBytes();
        byte[] base64 = Base64Utils.encode(plainCredsByte);
        String encryptedString = new String(base64);
        headers.add("Authorization", "Basic " + encryptedString);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("from", String.valueOf(from));
        body.add("to", String.valueOf(to));
        body.add("id", String.valueOf(rfid));
        //headers.add("from", String.valueOf(from));
        //headers.add("to", String.valueOf(to));
        //headers.add("rfid", String.valueOf(rfid));
        url += "from=" + String.valueOf(from);
        url += "&to=" + String.valueOf(to);
        url += "&rfid=" + String.valueOf(rfid);
        Log.v("HEADER STRING", headers.toString());
        HttpEntity<?> requestEntity = new HttpEntity<Object>(headers);

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("id", rfid);
        params.put("from", from);
        params.put("to", to);

        ResponseEntity<AndroidStamp[]> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, AndroidStamp[].class);

        AndroidStamp[] stamps = response.getBody();

        return stamps;
    }
}
