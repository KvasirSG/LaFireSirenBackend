package kenn767r.lafiresirenbackend.controller;

import kenn767r.lafiresirenbackend.model.Siren;
import kenn767r.lafiresirenbackend.service.SirenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sirens")
public class SirenController {

    private final SirenService sirenService;

    public SirenController(SirenService sirenService) {
        this.sirenService = sirenService;
    }

    @PostMapping
    public ResponseEntity<Siren> createSiren(@RequestBody Siren siren) {
        Siren created = sirenService.createSiren(siren);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}
