package kenn767r.lafiresirenbackend.service;

import kenn767r.lafiresirenbackend.model.Fire;
import kenn767r.lafiresirenbackend.model.Siren;
import kenn767r.lafiresirenbackend.model.SirenStatus;
import kenn767r.lafiresirenbackend.repository.FireRepository;
import kenn767r.lafiresirenbackend.repository.SirenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FireServiceTest {

    @Mock
    private FireRepository fireRepository;

    @Mock
    private SirenRepository sirenRepository;

    @InjectMocks
    private FireService fireService;

    @Test
    void shouldRegisterFireAndActivateNearbySirens() {
        Siren nearSiren = new Siren(1L, "Siren A", 34.01, -118.49, SirenStatus.SAFE);
        Siren farSiren = new Siren(2L, "Siren B", 34.90, -119.50, SirenStatus.SAFE);

        when(sirenRepository.findAll()).thenReturn(List.of(nearSiren, farSiren));
        when(fireRepository.save(any(Fire.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Fire fire = fireService.registerFire(34.00, -118.50);

        assertTrue(fire.isActive());
        assertEquals(1, fire.getTriggeredSirens().size());
        assertEquals(SirenStatus.DANGER, fire.getTriggeredSirens().get(0).getStatus());

        verify(sirenRepository).saveAll(anyList());
        verify(fireRepository).save(any(Fire.class));
    }
}
