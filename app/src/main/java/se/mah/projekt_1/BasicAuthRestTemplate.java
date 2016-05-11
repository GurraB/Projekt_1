package se.mah.projekt_1;

import android.util.Base64;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * Created by Gustaf on 11/05/2016.
 */
public class BasicAuthRestTemplate extends RestTemplate {
    public BasicAuthRestTemplate(String username, String password) {
        addAuthentication(username, password);
    }

    public BasicAuthRestTemplate(String encryptedUserCredentials) {
        addAuthentication(encryptedUserCredentials);
    }

    private void addAuthentication(String encryptedUserCredentials) {
        List<ClientHttpRequestInterceptor> interceptors = Collections
                .<ClientHttpRequestInterceptor> singletonList(
                        new BasicAuthorizationInterceptor(encryptedUserCredentials));
        setRequestFactory(new InterceptingClientHttpRequestFactory(getRequestFactory(),
                interceptors));
    }

    private void addAuthentication(String username, String password) {
        if (username == null) {
            return;
        }
        List<ClientHttpRequestInterceptor> interceptors = Collections
                .<ClientHttpRequestInterceptor> singletonList(
                        new BasicAuthorizationInterceptor(username, password));
        setRequestFactory(new InterceptingClientHttpRequestFactory(getRequestFactory(),
                interceptors));
    }

    private static class BasicAuthorizationInterceptor implements
            ClientHttpRequestInterceptor {

        private String username = null;

        private String password = null;

        private String encryptedUserCredentials = null;

        public BasicAuthorizationInterceptor(String username, String password) {
            this.username = username;
            this.password = (password == null ? "" : password);
        }

        public BasicAuthorizationInterceptor(String encryptedUserCredentials) {
            this.encryptedUserCredentials = encryptedUserCredentials;
        }

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                            ClientHttpRequestExecution execution) throws IOException {
            String encryptedCreds;

            if(encryptedUserCredentials == null) {
                byte[] token = Base64Utils.encode(
                        (this.username + ":" + this.password).getBytes());
                encryptedCreds = new String(token);
            }
            else
                encryptedCreds = encryptedUserCredentials;
            HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
            request.getHeaders().setAuthorization(authHeader);
            return execution.execute(request, body);
        }

    }
}
