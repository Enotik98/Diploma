package com.example.bookstore_app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.bookstore_app.dto.AuthDTO;
import com.example.bookstore_app.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

//    @Test
//    public void testRegistration() throws Exception {
//        AuthDTO authDTO = new AuthDTO();
//        authDTO.setEmail("test1@gmail.com");
//        authDTO.setPassword("password");
//
//        ResultActions resultActions = mockMvc.perform(post("/registration")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(authDTO)));
//        resultActions.andExpect(status().isOk());
//
//    }

    @Test
    public void testLogin() throws Exception {
        AuthDTO authDTO = new AuthDTO();
        authDTO.setEmail("test1@gmail.com");
        authDTO.setPassword("password");

        ResultActions resultActions = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authDTO)));
        resultActions.andExpect(status().isOk());
    }
    @Test
    public void testLoginIncorrect() throws Exception {
        AuthDTO authDTO = new AuthDTO();
        authDTO.setEmail("tes@gmail.com");
        authDTO.setPassword("password");

        ResultActions resultActions = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authDTO)));
        resultActions.andExpect(status().isUnauthorized());
    }
}
