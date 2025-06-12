package kenn767r.lafiresirenbackend.controller;

import kenn767r.lafiresirenbackend.model.Siren;
import kenn767r.lafiresirenbackend.service.SirenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<Siren>> getAllSirens() {
        List<Siren> sirens = sirenService.getAllSirens();
        return ResponseEntity.ok(sirens);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Siren> updateSiren(@PathVariable Long id, @RequestBody Siren updatedSiren) {
        Siren siren = sirenService.updateSiren(id, updatedSiren);
        return ResponseEntity.ok(siren);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSiren(@PathVariable Long id) {
        sirenService.deleteSiren(id);
        return ResponseEntity.noContent().build();
    }

}
