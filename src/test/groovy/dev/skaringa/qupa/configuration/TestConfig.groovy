package dev.skaringa.qupa.configuration


import dev.skaringa.qupa.service.ChartDataClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import spock.mock.DetachedMockFactory

@Configuration
@Profile("test")
class TestConfig {
    private static final def DETACHED_MOCK_FACTORY = new DetachedMockFactory()

    @Bean
    @Primary
    ChartDataClient chartDataClientSpy() {
        return DETACHED_MOCK_FACTORY.Mock(ChartDataClient)
    }
}
