package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import app.foot.FootApi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = FootApi.class)
@AutoConfigureMockMvc
public class HealthIntegrationTest {
  @Autowired
  private MockMvc mockMvc;

  @Test
  void ping_pong_ok() throws Exception {
    MockHttpServletResponse response = mockMvc.perform(get("/ping"))
        .andReturn()
        .getResponse();

    assertEquals("pong", response.getContentAsString());
  }
}
