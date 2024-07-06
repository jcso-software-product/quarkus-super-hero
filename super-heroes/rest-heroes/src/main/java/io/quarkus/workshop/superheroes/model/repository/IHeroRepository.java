package io.quarkus.workshop.superheroes.model.repository;

import io.quarkus.workshop.superheroes.model.entity.Hero;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface IHeroRepository extends JpaRepository<Hero, Long> {
}
