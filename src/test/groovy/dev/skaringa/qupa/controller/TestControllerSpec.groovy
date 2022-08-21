package dev.skaringa.qupa.controller

import dev.skaringa.qupa.SpecBaseIT

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class TestControllerSpec extends SpecBaseIT {
    def "GET /api/test returns Hello World"() {
        given: "expected result"
        def expected_result = "Hello World"

        when: "GET /api/test is called"
        def response = mockMvc.perform(get("/api/test"))

        then: "correct result is returned"
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath('$').value(expected_result))
    }
}
