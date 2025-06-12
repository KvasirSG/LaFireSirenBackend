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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@WebMvcTest(SirenController.class)
@ActiveProfiles("test")

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
    @Test
    void shouldReturnListOfSirens() throws Exception {
        List<Siren> sirens = List.of(
                new Siren(1L, "Siren A", 34.01, -118.49, SirenStatus.SAFE),
                new Siren(2L, "Siren B", 34.05, -118.47, SirenStatus.DANGER)
        );

        when(sirenService.getAllSirens()).thenReturn(sirens);

        mockMvc.perform(get("/sirens"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Siren A"))
                .andExpect(jsonPath("$[1].status").value("DANGER"));
    }

}
