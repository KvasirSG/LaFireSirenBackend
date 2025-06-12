package kenn767r.lafiresirenbackend.service;

import kenn767r.lafiresirenbackend.model.Siren;
import kenn767r.lafiresirenbackend.repository.SirenRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SirenService {

    private final SirenRepository sirenRepository;

    public SirenService(SirenRepository sirenRepository) {
        this.sirenRepository = sirenRepository;
    }

    public Siren createSiren(Siren siren) {
        return sirenRepository.save(siren);
    }

    public List<Siren> getAllSirens() {
        return sirenRepository.findAll();
    }

    public Siren updateSiren(Long id, Siren updated) {
        Siren existing = sirenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Siren not found with id " + id));

        existing.setName(updated.getName());
        existing.setLatitude(updated.getLatitude());
        existing.setLongitude(updated.getLongitude());
        existing.setStatus(updated.getStatus());
        existing.setDisabled(updated.isDisabled());

        return sirenRepository.save(existing);
    }

}
