package kenn767r.lafiresirenbackend.service;

import kenn767r.lafiresirenbackend.model.Siren;
import kenn767r.lafiresirenbackend.repository.SirenRepository;
import org.springframework.stereotype.Service;

@Service
public class SirenService {

    private final SirenRepository sirenRepository;

    public SirenService(SirenRepository sirenRepository) {
        this.sirenRepository = sirenRepository;
    }

    public Siren createSiren(Siren siren) {
        return sirenRepository.save(siren);
    }
}
