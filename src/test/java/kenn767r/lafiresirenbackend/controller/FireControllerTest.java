package kenn767r.lafiresirenbackend.controller;

import kenn767r.lafiresirenbackend.model.Fire;
import kenn767r.lafiresirenbackend.service.FireService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FireController.class)
class FireControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireService fireService;

    @Test
    void shouldRegisterFireSuccessfully() throws Exception {
        Fire mockFire = new Fire(1L, 34.01, -118.49, LocalDateTime.now(), true, Collections.emptyList());

        when(fireService.registerFire(34.01, -118.49)).thenReturn(mockFire);

        mockMvc.perform(post("/fires")
                        .param("latitude", "34.01")
                        .param("longitude", "-118.49")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.latitude").value(34.01))
                .andExpect(jsonPath("$.longitude").value(-118.49));
    }

    @Test
    void shouldReturnListOfActiveFires() throws Exception {
        Fire activeFire = new Fire(
                1L,
                34.05,
                -118.49,
                LocalDateTime.now(),
                true,
                Collections.emptyList()
        );

        when(fireService.getActiveFires()).thenReturn(List.of(activeFire));

        mockMvc.perform(get("/fires").param("status", "active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].active").value(true))
                .andExpect(jsonPath("$[0].latitude").value(34.05));
    }
}
