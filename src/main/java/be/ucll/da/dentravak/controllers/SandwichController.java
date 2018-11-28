package be.ucll.da.dentravak.controllers;

import be.ucll.da.dentravak.model.Sandwich;
import be.ucll.da.dentravak.repositories.SandwichRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@RestController
public class SandwichController {

    private SandwichRepository repository;

    public SandwichController(SandwichRepository repository) {
        this.repository = repository;

        Sandwich s1 = new Sandwich();
        s1.setName("Smos kaas & hesp");
        s1.setIngredients("Sla, tomatte, kaas, hesp, mayo");
        s1.setPrice(new BigDecimal("2.60"));

        Sandwich s2 = new Sandwich();
        s2.setName("Broodje american");
        s2.setIngredients("Uitjes, americain");
        s2.setPrice(new BigDecimal("2.30"));

        Sandwich s3 = new Sandwich();
        s3.setName("Kip hawaii");
        s3.setIngredients("Kippenblokjes, ananas, tuinkers, cocktailsaus");
        s3.setPrice(new BigDecimal("3.0"));

        repository.save(s1);
        repository.save(s2);
        repository.save(s3);
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
