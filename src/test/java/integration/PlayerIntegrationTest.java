package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utils.TestUtils.assertThrowsExceptionMessage;
import app.foot.FootApi;
import app.foot.controller.rest.Player;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = FootApi.class)
@AutoConfigureMockMvc
class PlayerIntegrationTest {
  private final ObjectMapper objectMapper = new ObjectMapper();
  @Autowired
  private MockMvc mockMvc;

  Player player1() {
    return Player.builder()
        .id(1)
        .name("J1")
        .isGuardian(false)
        .build();
  }

  Player player2() {
    return Player.builder()
        .id(2)
        .name("J2")
        .isGuardian(false)
        .build();
  }

  Player player3() {
    return Player.builder()
        .id(3)
        .name("J3")
        .isGuardian(false)
        .build();
  }

  @Test
  void read_players_ok() throws Exception {
    MockHttpServletResponse response = mockMvc
        .perform(get("/players"))
        .andReturn()
        .getResponse();
    List<Player> actual = convertFromHttpResponse(response);

    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertEquals(9, actual.size());
    assertTrue(actual.containsAll(List.of(
        player1(),
        player2(),
        player3())));
  }

  @Test
  void create_players_ok() throws Exception {
    Player toCreate = Player.builder()
        .name("Joe Doe")
        .isGuardian(false)
        .teamName("E1")
        .build();
    MockHttpServletResponse response = mockMvc
        .perform(post("/players")
            .content(objectMapper.writeValueAsString(List.of(toCreate)))
            .contentType("application/json")
            .accept("application/json"))
        .andReturn()
        .getResponse();
    List<Player> actual = convertFromHttpResponse(response);

    assertEquals(1, actual.size());
    assertEquals(toCreate, actual.get(0).toBuilder().id(null).build());
  }

  @Test
  void update_player_ok() throws Exception {
    Player updated = Player.builder()
        .id(1)
        .name("J1")
        .isGuardian(false)
        .teamName("E1")
        .build();
    MockHttpServletResponse response = mockMvc
        .perform(put("/players/1")
            .content(objectMapper.writeValueAsString(updated))
            .contentType("application/json"))
        .andReturn()
        .getResponse();
    MockHttpServletResponse getResponse =
        mockMvc.perform(get("/players"))
            .andReturn().getResponse();

    Player actual = objectMapper.readValue(response.getContentAsString(), Player.class);
    List<Player> actualList = convertFromHttpResponse(getResponse);

    assertEquals(updated, actual.toBuilder().build());
    assertTrue(actualList.contains(updated));
  }

  @Test
  void update_player_ko() throws Exception {
    MockHttpServletResponse response = mockMvc.perform(
            put("/players/100")
                .content(objectMapper.writeValueAsString(new Player()))
                .contentType("application/json")
        )
        .andExpect(status().isBadRequest())
        .andReturn().getResponse();

    assertThrowsExceptionMessage(response,
        "400 BAD_REQUEST : Name is mandatory. IsGuardian is mandatory. ");
  }


  private List<Player> convertFromHttpResponse(MockHttpServletResponse response)
      throws JsonProcessingException, UnsupportedEncodingException {
    CollectionType playerListType = objectMapper.getTypeFactory()
        .constructCollectionType(List.class, Player.class);
    return objectMapper.readValue(
        response.getContentAsString(),
        playerListType);
  }
}
