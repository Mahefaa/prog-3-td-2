package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utils.TestUtils.assertThrowsExceptionMessage;
import app.foot.FootApi;
import app.foot.controller.rest.Match;
import app.foot.controller.rest.Player;
import app.foot.controller.rest.PlayerScorer;
import app.foot.controller.rest.Team;
import app.foot.controller.rest.TeamMatch;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
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
class MatchIntegrationTest {
  private final ObjectMapper objectMapper = new ObjectMapper()
      .findAndRegisterModules();  //Allow 'java.time.Instant' mapping
  @Autowired
  private MockMvc mockMvc;

  private static Match expectedMatch2() {
    return Match.builder()
        .id(2)
        .teamA(teamMatch3A())
        .teamB(teamMatch3B())
        .stadium("S2")
        .datetime(Instant.parse("2023-01-01T14:00:00Z"))
        .build();
  }

  private static Match expectedMatch1() {
    return Match.builder()
        .id(1)
        .teamA(teamMatch1A())
        .teamB(teamMatch1B())
        .stadium("S1")
        .datetime(Instant.parse("2023-01-01T10:00:00Z"))
        .build();
  }

  private static TeamMatch teamMatch1B() {
    return TeamMatch.builder()
        .team(team2())
        .scorers(
            List.of(
                PlayerScorer.builder()
                    .player(player2())
                    .scoreTime(40)
                    .isOG(true)
                    .build(),
                PlayerScorer.builder()
                    .player(player3())
                    .scoreTime(50)
                    .isOG(false)
                    .build()
            )
        )
        .score(2)
        .build();
  }

  private static TeamMatch teamMatch1A() {
    return TeamMatch.builder()
        .team(team1())
        .score(4)
        .scorers(
            List.of(
                PlayerScorer.builder()
                    .player(player1())
                    .scoreTime(30)
                    .isOG(false)
                    .build(),
                PlayerScorer.builder()
                    .player(player1())
                    .scoreTime(20)
                    .isOG(false)
                    .build(),
                PlayerScorer.builder()
                    .player(player1())
                    .scoreTime(10)
                    .isOG(false)
                    .build(),
                PlayerScorer.builder()
                    .player(player4())
                    .scoreTime(60)
                    .isOG(true)
                    .build()
            )
        )
        .build();
  }

  private static Match expectedMatch3() {
    return Match.builder()
        .id(3)
        .teamA(TeamMatch.builder()
            .team(team1())
            .scorers(List.of())
            .score(0)
            .build())
        .teamB(TeamMatch.builder()
            .team(team3())
            .scorers(List.of())
            .score(0)
            .build())
        .stadium("S3")
        .datetime(Instant.parse("2023-01-01T18:00:00Z"))
        .build();
  }

  private static TeamMatch teamMatch3B() {
    return TeamMatch.builder()
        .team(team3())
        .score(0)
        .scorers(List.of())
        .build();
  }

  private static TeamMatch teamMatch3A() {
    return TeamMatch.builder()
        .team(team2())
        .score(2)
        .scorers(List.of(PlayerScorer.builder()
                .player(player3())
                .scoreTime(70)
                .isOG(false)
                .build(),
            PlayerScorer.builder()
                .player(player6())
                .scoreTime(80)
                .isOG(true)
                .build()))
        .build();
  }

  private static Team team1() {
    return Team.builder()
        .id(1)
        .name("E1")
        .build();
  }

  private static Team team2() {
    return Team.builder()
        .id(2)
        .name("E2")
        .build();
  }

  private static Team team3() {
    return Team.builder()
        .id(3)
        .name("E3")
        .build();
  }

  private static Player player6() {
    return Player.builder()
        .id(6)
        .name("J6")
        .teamName("E3")
        .isGuardian(false)
        .build();
  }

  private static Player player3() {
    return Player.builder()
        .id(3)
        .teamName("E2")
        .name("J3")
        .isGuardian(false)
        .build();
  }

  private static Player player1() {
    return Player.builder()
        .id(1)
        .teamName("E1")
        .name("J1")
        .isGuardian(false)
        .build();
  }

  private static Player player2() {
    return Player.builder()
        .id(2)
        .teamName("E1")
        .name("J2")
        .isGuardian(false)
        .build();
  }

  private static Player player4() {
    return Player.builder()
        .id(4)
        .teamName("E2")
        .name("J4")
        .isGuardian(false)
        .build();
  }


  @Test
  void read_match_by_id_ok() throws Exception {
    MockHttpServletResponse response = mockMvc.perform(get("/matches/2"))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse();
    Match actual = objectMapper.readValue(
        response.getContentAsString(), Match.class);

    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertEquals(expectedMatch2(), actual);
  }

  @Test
  void read_match_by_id_ko() throws Exception {
    MockHttpServletResponse response = mockMvc.perform(get("/matches/100"))
        .andExpect(status().isNotFound())
        .andReturn()
        .getResponse();

    assertThrowsExceptionMessage(response, "404 NOT_FOUND : Match#100 not found.");
  }

  @Test
  void read_matches_ok() throws Exception {
    MockHttpServletResponse response = mockMvc.perform(get("/matches"))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse();

    List<Match> actual = convertFromHttpResponse(response);

    assertEquals(3, actual.size());
    assertTrue(actual.contains(expectedMatch1()));
    assertTrue(actual.contains(expectedMatch2()));
    //updating match#3 changes its data in our db
    //assertTrue(actual.contains(expectedMatch3()));
  }

  @Test
  void add_goals_ok() throws Exception {
    PlayerScorer validScorer = PlayerScorer.builder().player(
            Player.builder()
                .id(5)
                .isGuardian(false)
                .teamName("E3")
                .build())
        .isOG(true)
        .scoreTime(10)
        .build();
    MockHttpServletResponse response = mockMvc.perform(
        post("/matches/3/goals")
            .content(objectMapper.writeValueAsString(List.of(validScorer)))
            .contentType(APPLICATION_JSON)
    ).andReturn().getResponse();

    Match actual = objectMapper.readValue(
        response.getContentAsString(), Match.class);

    assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Test
  void add_goals_ko() throws Exception {
    PlayerScorer invalidScorer = PlayerScorer.builder().player(Player.builder()
            .id(1)
            .isGuardian(true)
            .build())
        .build();
    MockHttpServletResponse response = mockMvc.perform(
        post("/matches/3/goals")
            .content(objectMapper.writeValueAsString(
                List.of(invalidScorer)))
            .contentType(APPLICATION_JSON)
    ).andReturn().getResponse();

    assertThrowsExceptionMessage(response,
        "400 BAD_REQUEST : Player#1 is a guardian so they cannot score.Score minute is mandatory.");
  }

  private List<Match> convertFromHttpResponse(MockHttpServletResponse response)
      throws UnsupportedEncodingException, JsonProcessingException {
    CollectionType matchListType = objectMapper.getTypeFactory()
        .constructCollectionType(List.class, Match.class);
    return objectMapper.readValue(
        response.getContentAsString(),
        matchListType);
  }
}
