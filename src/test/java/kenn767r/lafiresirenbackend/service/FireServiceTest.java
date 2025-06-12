package kenn767r.lafiresirenbackend.service;

import kenn767r.lafiresirenbackend.model.Fire;
import kenn767r.lafiresirenbackend.model.Siren;
import kenn767r.lafiresirenbackend.model.SirenStatus;
import kenn767r.lafiresirenbackend.repository.FireRepository;
import kenn767r.lafiresirenbackend.repository.SirenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")

class FireServiceTest {

    @Mock
    private FireRepository fireRepository;

    @Mock
    private SirenRepository sirenRepository;

    @InjectMocks
    private FireService fireService;

    @Test
    void shouldRegisterFireAndActivateNearbySirens() {
        Siren nearSiren = new Siren(1L, "Siren A", 34.01, -118.49, SirenStatus.SAFE, false);
        Siren farSiren = new Siren(2L, "Siren B", 34.90, -119.50, SirenStatus.SAFE, false);


        when(sirenRepository.findAll()).thenReturn(List.of(nearSiren, farSiren));
        when(fireRepository.save(any(Fire.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Fire fire = fireService.registerFire(34.00, -118.50);

        assertTrue(fire.isActive());
        assertEquals(1, fire.getTriggeredSirens().size());
        assertEquals(SirenStatus.DANGER, fire.getTriggeredSirens().get(0).getStatus());

        verify(sirenRepository).saveAll(anyList());
        verify(fireRepository).save(any(Fire.class));
    }

    @Test
    void shouldCalculateKnownDistanceBetweenSantaMonicaAndVenice() {
        double lat1 = 34.0100, lon1 = -118.4969; // Santa Monica Pier
        double lat2 = 33.9850, lon2 = -118.4695; // Venice Beach

        double distance = fireService.calculateDistanceKM(lat1, lon1, lat2, lon2);

        assertTrue(distance > 3.0 && distance < 4.0,
                "Expected ~3.5 km, got: " + distance);
    }
    @Test
    void shouldCloseFireAndOnlyResetSirensNotUsedElsewhere() {
        Siren siren1 = new Siren(1L, "Siren A", 34.01, -118.49, SirenStatus.DANGER, false);
        Siren siren2 = new Siren(2L, "Siren B", 34.02, -118.48, SirenStatus.DANGER, false);

        Siren siren2Copy = new Siren(2L, "Siren B", 34.02, -118.48, SirenStatus.DANGER, false);


        Fire fireToClose = new Fire(1L, 34.00, -118.50, LocalDateTime.now(), true, List.of(siren1, siren2));
        Fire otherActiveFire = new Fire(2L, 34.03, -118.47, LocalDateTime.now(), true, List.of(siren2Copy));

        when(fireRepository.findById(1L)).thenReturn(java.util.Optional.of(fireToClose));
        when(fireRepository.findByActiveTrue()).thenReturn(List.of(otherActiveFire, fireToClose));
        when(fireRepository.save(any(Fire.class))).thenAnswer(inv -> inv.getArgument(0));

        fireService.closeFire(1L);

        assertEquals(SirenStatus.SAFE, siren1.getStatus());   // Not used elsewhere
        assertEquals(SirenStatus.DANGER, siren2.getStatus()); // Still used by another active fire
        assertFalse(fireToClose.isActive());
    }


}
