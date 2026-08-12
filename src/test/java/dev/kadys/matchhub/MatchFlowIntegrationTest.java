package dev.kadys.matchhub;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.*;
import java.util.regex.Pattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MatchFlowIntegrationTest {
    @Autowired MockMvc mvc;

    @Test
    void managesCapacityWithdrawalAndOrganizerAuthorization() throws Exception {
        String organizer=register("Organizador","organizer@example.com");
        String player=register("Jogadora","player@example.com");

        String body="{\"title\":\"Futebol de sábado\",\"sport\":\"FUTEBOL\",\"address\":\"Arena Central\",\"startsAt\":\"2035-08-16T19:00:00Z\",\"capacity\":2}";
        MvcResult created=mvc.perform(post("/api/v1/matches").header("Authorization","Bearer "+organizer)
                .contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isCreated()).andExpect(jsonPath("$.confirmed").value(1)).andReturn();
        String matchId=field(created,"id");

        mvc.perform(post("/api/v1/matches/{id}/participants/me",matchId).header("Authorization","Bearer "+player))
            .andExpect(status().isOk()).andExpect(jsonPath("$.confirmed").value(2)).andExpect(jsonPath("$.status").value("FULL"));
        mvc.perform(delete("/api/v1/matches/{id}/participants/me",matchId).header("Authorization","Bearer "+player))
            .andExpect(status().isOk()).andExpect(jsonPath("$.confirmed").value(1)).andExpect(jsonPath("$.status").value("OPEN"));
        mvc.perform(post("/api/v1/matches/{id}/complete",matchId).header("Authorization","Bearer "+player))
            .andExpect(status().isForbidden());
        mvc.perform(post("/api/v1/matches/{id}/complete",matchId).header("Authorization","Bearer "+organizer))
            .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    private String register(String name,String email) throws Exception {
        String request="{\"name\":\""+name+"\",\"email\":\""+email+"\",\"password\":\"senha-forte-123\"}";
        MvcResult result=mvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(request))
            .andExpect(status().isCreated()).andReturn();
        return field(result,"accessToken");
    }

    private String field(MvcResult result,String name) throws Exception {
        var matcher=Pattern.compile("\\\""+name+"\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"").matcher(result.getResponse().getContentAsString());
        if (!matcher.find()) throw new AssertionError("Campo ausente na resposta: "+name);
        return matcher.group(1);
    }
}
