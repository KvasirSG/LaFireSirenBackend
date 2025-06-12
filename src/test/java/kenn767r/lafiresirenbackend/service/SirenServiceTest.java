package kenn767r.lafiresirenbackend.service;

import kenn767r.lafiresirenbackend.model.Siren;
import kenn767r.lafiresirenbackend.model.SirenStatus;
import kenn767r.lafiresirenbackend.repository.SirenRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SirenServiceTest {

    @Mock
    private SirenRepository sirenRepository;

    @InjectMocks
    private SirenService sirenService;

    @Test
    void shouldCreateNewSiren() {
        Siren input = new Siren(null, "Santa Monica 1", 34.01, -118.49, SirenStatus.SAFE);
        Siren saved = new Siren(1L, "Santa Monica 1", 34.01, -118.49, SirenStatus.SAFE);


        when(sirenRepository.save(input)).thenReturn(saved);

        Siren result = sirenService.createSiren(input);

        assertNotNull(result.getId());
        assertEquals("Santa Monica 1", result.getName());
        verify(sirenRepository, times(1)).save(input);
    }
}

