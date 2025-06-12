package kenn767r.lafiresirenbackend.controller;

import kenn767r.lafiresirenbackend.model.Fire;
import kenn767r.lafiresirenbackend.service.FireService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fires")
public class FireController {

    private final FireService fireService;

    public FireController(FireService fireService) {
        this.fireService = fireService;
    }

    @PostMapping
    public ResponseEntity<Fire> registerFire(@RequestParam double latitude,
                                             @RequestParam double longitude) {
        Fire created = fireService.registerFire(latitude, longitude);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Fire>> getFires(@RequestParam(required = false) String status) {
        if ("active".equalsIgnoreCase(status)) {
            return ResponseEntity.ok(fireService.getActiveFires());
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}/closure")
    public ResponseEntity<Fire> closeFire(@PathVariable Long id) {
        try {
            Fire closed = fireService.closeFire(id);
            return ResponseEntity.ok(closed);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


}
