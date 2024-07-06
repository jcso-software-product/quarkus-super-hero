package io.quarkus.workshop.superheroes.model.repository;

import io.quarkus.workshop.superheroes.model.entity.Villain;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.inject.Singleton;

@Singleton
public interface IVillainRepository extends  JpaRepository<Villain, Long> {
}
