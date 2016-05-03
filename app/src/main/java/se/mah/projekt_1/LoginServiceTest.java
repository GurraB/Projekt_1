package se.mah.projekt_1;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.LinearLayout;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Gustaf on 02/05/2016.
 */
public class LoginServiceTest extends AsyncTask<String, String, String> {

    LogInActivity ac;

    public LoginServiceTest(LogInActivity ac) {
        this.ac = ac;
    }

    @Override
    protected String doInBackground(String... strings) {
        String url = "http://195.178.224.74:44344/api/account";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        Map<String, Object> loginParams = new LinkedHashMap<>();
        loginParams.put("username", strings[0]);
        loginParams.put("password", strings[1]);

        ResponseEntity<Map> response;

        HttpHeaders headers = new HttpHeaders();
        String plainCreds = "user:pass";
        byte[] plainCredsByte = plainCreds.getBytes();
        byte[] base64 = Base64Utils.encode(plainCredsByte);
        String cancer = new String(base64);
        headers.add("Authorization", "Basic " + cancer);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        //String a = restTemplate.execute(url, HttpMethod.GET, null, extractor, loginParams);
        response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Map.class);

        LinkedHashMap<String, Object> responseMap = (LinkedHashMap<String, Object>) response.getBody();
        LinkedHashMap<String, Object> accountMap = (LinkedHashMap<String, Object>) responseMap.get("principal");
        Account acc = new Account();
        acc.setFirstName((String) accountMap.get("firstName"));
        acc.setLastName((String) accountMap.get("lastName"));
        acc.setId((String) accountMap.get("id"));
        acc.setAccountNonExpired((Boolean) accountMap.get("accountNonExpired"));
        acc.setIsEnabled((Boolean) accountMap.get("enabled"));

        Log.v("ACCOUNT", acc.toString());

        //String fancy = restTemplate.getForObject(url, String.class, loginParams);
        return response.getStatusCode().toString() + response.getBody();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        ac.finishedLoading(s);
    }
}
