package org.example.superheroes.services;

import org.example.superheroes.models.Superhero;
import org.example.superheroes.repositories.SuperheroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuperheroService {
    private final SuperheroRepository repository;

    public SuperheroService(SuperheroRepository repository) {
        this.repository = repository;
    }

    public void insertSuperhero(Superhero superhero) {
        repository.save(superhero);
    }

    public void insertManySuperheroes(List<Superhero> superheroList) {
        repository.saveAll(superheroList);
    }

    public Superhero updateSuperhero(String name, Superhero updatedSuperhero) {
        Superhero existingSuperhero = repository.findById(name).orElse(null);
        if (existingSuperhero != null) {
            existingSuperhero.setPower(updatedSuperhero.getPower());
            existingSuperhero.setGender(updatedSuperhero.getGender());
            existingSuperhero.setAge(updatedSuperhero.getAge());
            existingSuperhero.setUniverse(updatedSuperhero.getUniverse());
            return repository.save(existingSuperhero);
        }
        return null; // Superhero not found
    }

    public boolean deleteSuperhero(String name) {
        if (repository.existsById(name)) {
            repository.deleteById(name);
            return true;
        }
        return false;
    }

    public List<Superhero> getSuperheroes(String name, String universe) {
        // MongoRepository doesn't support filtering like this directly; add queries if needed
        return repository.findAll();
    }
}
