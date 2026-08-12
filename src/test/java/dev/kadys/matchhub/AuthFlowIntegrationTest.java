package dev.kadys.matchhub;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthFlowIntegrationTest {
    @Autowired MockMvc mvc;
    @Test void registersAndRejectsDuplicatedEmail() throws Exception {
        String json="{\"name\":\"Ana Jogadora\",\"email\":\"ana@example.com\",\"password\":\"senha-forte-123\"}";
        mvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isCreated()).andExpect(jsonPath("$.tokenType").value("Bearer"));
        mvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isConflict()).andExpect(jsonPath("$.message").value("E-mail já cadastrado."));
    }
    @Test void validatesRegistration() throws Exception {
        mvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"\",\"email\":\"x\",\"password\":\"1\"}"))
            .andExpect(status().isBadRequest()).andExpect(jsonPath("$.fields.email").exists());
    }
}
