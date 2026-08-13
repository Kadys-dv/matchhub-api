package dev.kadys.matchhub;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.regex.Pattern;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityAndValidationIntegrationTest {
    @Autowired MockMvc mvc;

    @Test
    void rejectsProtectedEndpointWithoutTokenAndInvalidToken() throws Exception {
        mvc.perform(get("/api/v1/matches")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/v1/matches").header("Authorization", "Bearer token-invalido"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void rejectsWrongPasswordAndInvalidMatchPayload() throws Exception {
        String token = register("Validadora", "validation@example.com");
        mvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"validation@example.com\",\"password\":\"senha-incorreta\"}"))
            .andExpect(status().isUnauthorized());

        mvc.perform(post("/api/v1/matches").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"\",\"sport\":\"FUTEBOL\",\"address\":\"Arena\",\"startsAt\":\"2020-01-01T10:00:00Z\",\"capacity\":1}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.fields.title").exists())
            .andExpect(jsonPath("$.fields.startsAt").exists())
            .andExpect(jsonPath("$.fields.capacity").exists());
    }

    @Test
    void rejectsDuplicateParticipationAndJoiningFullMatch() throws Exception {
        String organizer = register("Organizador seguro", "secure-organizer@example.com");
        String player = register("Jogador seguro", "secure-player@example.com");
        String reserve = register("Reserva seguro", "secure-reserve@example.com");
        MvcResult created = mvc.perform(post("/api/v1/matches")
                .header("Authorization", "Bearer " + organizer)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Partida segura\",\"sport\":\"FUTEBOL\",\"address\":\"Arena Central\",\"startsAt\":\"2035-09-10T19:00:00Z\",\"capacity\":2}"))
            .andExpect(status().isCreated()).andReturn();
        String matchId = field(created, "id");

        mvc.perform(post("/api/v1/matches/{id}/participants/me", matchId)
                .header("Authorization", "Bearer " + organizer))
            .andExpect(status().isConflict());
        mvc.perform(post("/api/v1/matches/{id}/participants/me", matchId)
                .header("Authorization", "Bearer " + player))
            .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("FULL"));
        mvc.perform(post("/api/v1/matches/{id}/participants/me", matchId)
                .header("Authorization", "Bearer " + reserve))
            .andExpect(status().isConflict());
    }

    private String register(String name, String email) throws Exception {
        MvcResult result = mvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"" + name + "\",\"email\":\"" + email + "\",\"password\":\"senha-forte-123\"}"))
            .andExpect(status().isCreated()).andReturn();
        return field(result, "accessToken");
    }

    private String field(MvcResult result, String name) throws Exception {
        var matcher = Pattern.compile("\\\"" + name + "\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"")
            .matcher(result.getResponse().getContentAsString());
        if (!matcher.find()) throw new AssertionError("Campo ausente: " + name);
        return matcher.group(1);
    }
}
