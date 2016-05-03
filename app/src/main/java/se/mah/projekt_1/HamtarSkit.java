package se.mah.projekt_1;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Gustaf on 03/05/2016.
 */
public class HamtarSkit {

    public Account login(String url, String username, String password) throws RuntimeException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        ResponseEntity<Map> response;

        HttpHeaders headers = new HttpHeaders();
        String plainCreds = username + ":" + password;
        username = password = null;
        byte[] plainCredsByte = plainCreds.getBytes();
        byte[] base64 = Base64Utils.encode(plainCredsByte);
        String encryptedString = new String(base64);
        headers.add("Authorization", "Basic " + encryptedString);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Map.class);

        LinkedHashMap<String, Object> responseMap = (LinkedHashMap<String, Object>) response.getBody();
        LinkedHashMap<String, Object> accountMap = (LinkedHashMap<String, Object>) responseMap.get("principal");
        Account acc = new Account();
        acc.setFirstName((String) accountMap.get("firstName"));
        acc.setLastName((String) accountMap.get("lastName"));
        acc.setId((String) accountMap.get("id"));
        acc.setAccountNonExpired((Boolean) accountMap.get("accountNonExpired"));
        acc.setIsEnabled((Boolean) accountMap.get("enabled"));
        return acc;
    }

    public AndroidStamp[] getStampsForUser() {
        return null;
    }
}
