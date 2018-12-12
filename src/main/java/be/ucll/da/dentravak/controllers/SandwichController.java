package be.ucll.da.dentravak.controllers;

import be.ucll.da.dentravak.model.Sandwich;
import be.ucll.da.dentravak.model.SandwichPreferences;
import be.ucll.da.dentravak.repositories.SandwichRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.naming.ServiceUnavailableException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@CrossOrigin
public class SandwichController {

    private SandwichRepository repository;

    //@Autowired
    //private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

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
        try {
            return getSandwichesSortedByRecommendations();
        } catch (Exception e) {
            return repository.findAll();
        }
    }

    List<Sandwich> getSandwichesSortedByRecommendations() throws ServiceUnavailableException {
        SandwichPreferences preferences = getPreferences("03koffie");
        List<Sandwich> sandwiches = toList(repository.findAll());
        Collections.sort(sandwiches, compareByRating(preferences));
        return sandwiches;
    }

    private Comparator<Sandwich> compareByRating(SandwichPreferences preferences) {
        return (Sandwich sandwichA, Sandwich sandwichB) -> rating(preferences, sandwichB).compareTo(rating(preferences, sandwichA));
    }

    private Float rating(SandwichPreferences preferences, Sandwich sandwich) {
        return preferences.getRatingForSandwich(sandwich.getId());
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

    // why comment: for testing
    @GetMapping("/getpreferences/{phoneNr}")
    public SandwichPreferences getPreferences(@PathVariable String phoneNr) throws RestClientException, ServiceUnavailableException {
        URI service = recommendationServiceUrl()
                .map(s -> s.resolve("/recommend/" + phoneNr))
                .orElseThrow(ServiceUnavailableException::new);
        return restTemplate
                .getForEntity(service, SandwichPreferences.class)
                .getBody();
    }

    public Optional<URI> recommendationServiceUrl() {
        try {
            return Optional.of(new URI("http://localhost:8081"));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> toList(final Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

//    public Optional<URI> recommendationServiceUrl() {
//        return discoveryClient.getInstances("recommendation")
//                .stream()
//                .map(si -> si.getUri())
//                .findFirst();
//    }
}
