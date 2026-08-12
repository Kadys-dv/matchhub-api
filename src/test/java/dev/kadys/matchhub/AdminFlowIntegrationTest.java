package dev.kadys.matchhub;

import dev.kadys.matchhub.user.UserRepository;
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
class AdminFlowIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired UserRepository users;

    @Test
    void protectsModerationAndAllowsAccountDeactivation() throws Exception {
        register("Administrador","admin-flow@example.com");
        String player=register("Atleta reportante","reporter-flow@example.com");
        var adminUser=users.findByEmailIgnoreCase("admin-flow@example.com").orElseThrow();
        adminUser.promoteToAdmin(); users.saveAndFlush(adminUser);
        String admin=login("admin-flow@example.com");

        mvc.perform(post("/api/v1/reports").header("Authorization","Bearer "+player).contentType(MediaType.APPLICATION_JSON)
                .content("{\"reason\":\"Conduta inadequada\",\"details\":\"Mensagem ofensiva enviada na sala da partida.\"}"))
            .andExpect(status().isCreated()).andExpect(jsonPath("$.status").value("PENDING"));
        mvc.perform(get("/api/v1/reports").header("Authorization","Bearer "+player)).andExpect(status().isForbidden());
        mvc.perform(get("/api/v1/reports").header("Authorization","Bearer "+admin)).andExpect(status().isOk()).andExpect(jsonPath("$.totalElements").value(1));

        String playerId=users.findByEmailIgnoreCase("reporter-flow@example.com").orElseThrow().getId().toString();
        mvc.perform(patch("/api/v1/admin/users/{id}/status",playerId).header("Authorization","Bearer "+admin).contentType(MediaType.APPLICATION_JSON).content("{\"enabled\":false}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.enabled").value(false));
        mvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"reporter-flow@example.com\",\"password\":\"senha-forte-123\"}"))
            .andExpect(status().isUnauthorized());
    }

    private String register(String name,String email) throws Exception {MvcResult result=mvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\""+name+"\",\"email\":\""+email+"\",\"password\":\"senha-forte-123\"}")).andExpect(status().isCreated()).andReturn();return field(result,"accessToken");}
    private String login(String email) throws Exception {MvcResult result=mvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content("{\"email\":\""+email+"\",\"password\":\"senha-forte-123\"}")).andExpect(status().isOk()).andReturn();return field(result,"accessToken");}
    private String field(MvcResult result,String name) throws Exception {var matcher=Pattern.compile("\\\""+name+"\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"").matcher(result.getResponse().getContentAsString());if(!matcher.find())throw new AssertionError("Campo ausente: "+name);return matcher.group(1);}
}
