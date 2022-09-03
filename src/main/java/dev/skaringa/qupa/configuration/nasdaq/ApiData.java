package dev.skaringa.qupa.configuration.nasdaq;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("com.nasdaq.api")
public class ApiData {
    private String rootUri;
    private String key;
}
