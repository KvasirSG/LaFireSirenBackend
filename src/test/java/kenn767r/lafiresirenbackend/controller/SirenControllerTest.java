package kenn767r.lafiresirenbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kenn767r.lafiresirenbackend.model.Siren;
import kenn767r.lafiresirenbackend.model.SirenStatus;
import kenn767r.lafiresirenbackend.service.SirenService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SirenController.class)
class SirenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SirenService sirenService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateSirenSuccessfully() throws Exception {
        Siren input = new Siren(null, "Test Siren", 34.01, -118.49, SirenStatus.SAFE);
        Siren saved = new Siren(1L, "Test Siren", 34.01, -118.49, SirenStatus.SAFE);

        when(sirenService.createSiren(any(Siren.class))).thenReturn(saved);

        mockMvc.perform(post("/sirens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Siren"))
                .andExpect(jsonPath("$.status").value("SAFE"));
    }
}
