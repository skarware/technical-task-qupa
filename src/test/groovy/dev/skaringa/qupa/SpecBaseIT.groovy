package dev.skaringa.qupa


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
abstract class SpecBaseIT extends Specification {
    protected static final def TICKER = "ticker"

    @Autowired
    protected MockMvc mockMvc
}
