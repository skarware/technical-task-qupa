package dev.skaringa.qupa.configuration.nasdaq;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Log4j2
@Component
@RequiredArgsConstructor
public class NasdaqAuthorizationRequestInterceptor implements ClientHttpRequestInterceptor {
    private final ApiData apiData;

    @Override
    @SneakyThrows
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) {
        URI uriWithApiKey = UriComponentsBuilder.fromUri(request.getURI())
                .queryParam("api_key", apiData.getKey())
                .build().toUri();
        return execution.execute(new ReplaceUriClientHttpRequest(uriWithApiKey, request), body);
    }

    private static class ReplaceUriClientHttpRequest extends HttpRequestWrapper implements HttpRequest {
        private final URI uri;

        ReplaceUriClientHttpRequest(URI uri, HttpRequest request) {
            super(request);
            this.uri = uri;
        }

        @Override
        public ClientHttpRequest getRequest() {
            return (ClientHttpRequest) super.getRequest();
        }

        @Override
        public URI getURI() {
            return this.uri;
        }
    }
}
