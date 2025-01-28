package org.example.superheroes.repositories;

import org.example.superheroes.models.Superhero;
import org.springframework.stereotype.Repository;


import org.example.superheroes.models.Superhero;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface SuperheroRepository extends MongoRepository<Superhero, String> {
    // Custom query methods if needed
    Superhero findByName(String name);
}
