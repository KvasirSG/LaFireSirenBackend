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
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class SirenServiceTest {

    @Mock
    private SirenRepository sirenRepository;

    @InjectMocks
    private SirenService sirenService;

    @Test
    void shouldCreateNewSiren() {
        Siren input = new Siren(null, "Santa Monica 1", 34.01, -118.49, SirenStatus.SAFE, false);
        Siren saved = new Siren(1L, "Santa Monica 1", 34.01, -118.49, SirenStatus.SAFE, false);


        when(sirenRepository.save(input)).thenReturn(saved);

        Siren result = sirenService.createSiren(input);

        assertNotNull(result.getId());
        assertEquals("Santa Monica 1", result.getName());
        verify(sirenRepository, times(1)).save(input);
    }

    @Test
    void shouldUpdateSirenSuccessfully() {
        Long id = 1L;
        Siren existing = new Siren(id, "Old Name", 34.01, -118.49, SirenStatus.SAFE, false);

        Siren updated = new Siren(id, "Updated Name", 34.05, -118.50, SirenStatus.DANGER, true);

        when(sirenRepository.findById(id)).thenReturn(Optional.of(existing));
        when(sirenRepository.save(any(Siren.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Siren result = sirenService.updateSiren(id, updated);

        assertEquals("Updated Name", result.getName());
        assertEquals(34.05, result.getLatitude());
        assertEquals(SirenStatus.DANGER, result.getStatus());
        assertTrue(result.isDisabled());
    }

}

