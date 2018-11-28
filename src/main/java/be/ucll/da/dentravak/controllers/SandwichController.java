package be.ucll.da.dentravak.controllers;

import be.ucll.da.dentravak.model.Sandwich;
import be.ucll.da.dentravak.repositories.SandwichRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
public class SandwichController {

    private SandwichRepository repository;

    public SandwichController(SandwichRepository repository) {
        this.repository = repository;
    }

    @RequestMapping("/sandwiches")
    public Iterable<Sandwich> sandwiches() {
        return repository.findAll();
    }

    @RequestMapping(value = "/sandwiches", method = RequestMethod.POST)
    public Sandwich createSandwich(@RequestBody Sandwich sandwich) {
        return repository.save(sandwich);
    }

    @PutMapping("/sandwiches/{id}")
    public ResponseEntity updateSandwich(@PathVariable UUID id, @RequestBody Sandwich sandwich) {
        if (id.equals(sandwich.getId())) {
            Optional<Sandwich> original = repository.findById(id);
            if (original.isPresent()) {
                sandwich.setId(id);
                repository.save(sandwich);
                return ResponseEntity.status(HttpStatus.OK).body(repository.findById(id).get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
