package be.ucll.da.dentravak.controllers;

import be.ucll.da.dentravak.model.SandwichOrder;
import be.ucll.da.dentravak.repositories.SandwichOrderRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@CrossOrigin
public class SandwichOrderController {

    private SandwichOrderRepository repository;

    public SandwichOrderController(SandwichOrderRepository repository) {
        this.repository = repository;
    }

    @RequestMapping("/orders")
    public Iterable<SandwichOrder> orders() {
        return repository.findAll();
    }

    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    public SandwichOrder createSandwichOrder(@RequestBody SandwichOrder sandwichOrder) {
        sandwichOrder.setCreationDate(LocalDateTime.now());
        return repository.save(sandwichOrder);
    }


}

