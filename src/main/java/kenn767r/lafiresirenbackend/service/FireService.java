package kenn767r.lafiresirenbackend.service;

import kenn767r.lafiresirenbackend.model.Fire;
import kenn767r.lafiresirenbackend.model.Siren;
import kenn767r.lafiresirenbackend.model.SirenStatus;
import kenn767r.lafiresirenbackend.repository.FireRepository;
import kenn767r.lafiresirenbackend.repository.SirenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FireService {

    private final FireRepository fireRepository;
    private final SirenRepository sirenRepository;
    /**
     * Average radius of the Earth in kilometers.
     * Used in Haversine formula for calculating distance between two coordinates.
     */
    private static final int EARTH_RADIUS_KM = 6371;

    public FireService(FireRepository fireRepository, SirenRepository sirenRepository) {
        this.fireRepository = fireRepository;
        this.sirenRepository = sirenRepository;
    }

    public Fire registerFire(double latitude, double longitude) {
        List<Siren> allSirens = sirenRepository.findAll();

        List<Siren> nearby = allSirens.stream()
                .filter(siren -> calculateDistanceKM(latitude, longitude, siren.getLatitude(), siren.getLongitude()) <= 10)
                .peek(siren -> siren.setStatus(SirenStatus.DANGER))
                .collect(Collectors.toList());

        // Save updated siren statuses
        sirenRepository.saveAll(nearby);

        Fire fire = new Fire();
        fire.setLatitude(latitude);
        fire.setLongitude(longitude);
        fire.setTimestamp(LocalDateTime.now());
        fire.setActive(true);
        fire.setTriggeredSirens(nearby);

        return fireRepository.save(fire);
    }

    double calculateDistanceKM(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double rLat1 = Math.toRadians(lat1);
        double rLat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2), 2)
                + Math.cos(rLat1) * Math.cos(rLat2)
                * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    public List<Fire> getActiveFires() {
        return fireRepository.findByActiveTrue();
    }

    public Fire closeFire(Long fireId) {
        Fire fire = fireRepository.findById(fireId)
                .orElseThrow(() -> new RuntimeException("Fire not found with ID: " + fireId));

        fire.setActive(false);

        List<Siren> sirens = fire.getTriggeredSirens();

        for (Siren siren : sirens) {
            boolean isUsedInOtherFires = fireRepository.findByActiveTrue().stream()
                    .filter(f -> !f.getId().equals(fire.getId())) // skip the one we're closing
                    .flatMap(f -> f.getTriggeredSirens().stream()) // pull all sirens from other active fires
                    .anyMatch(s -> s.getId().equals(siren.getId())); // check if this siren is used elsewhere

            if (!isUsedInOtherFires) {
                siren.setStatus(SirenStatus.SAFE);
            }
        }

        sirenRepository.saveAll(sirens);
        return fireRepository.save(fire);
    }

}
