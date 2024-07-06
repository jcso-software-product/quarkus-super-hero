package io.quarkus.workshop.superheroes.model.service;

import io.quarkus.workshop.superheroes.model.entity.Hero;
import io.quarkus.workshop.superheroes.model.repository.IHeroRepository;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Random;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

@ApplicationScoped
@Slf4j
@Transactional(REQUIRED)
public class HeroService {

    @Inject
    IHeroRepository heroRepository;

    public List<Hero> findAll(){
        return heroRepository.findAll();
    }

    @Transactional(SUPPORTS)
    public Hero findHeroById(Long id){
        return heroRepository.findById(id).orElse(null);
    }

    @Transactional(SUPPORTS)
    public Hero findRandomHero() {
        Hero randomHero = null;
        while (randomHero == null) {
            randomHero = findRandom();
        }
        return randomHero;
    }

    public Hero persistHero(@Valid Hero hero){
        return heroRepository.save(hero);
    }

    public Hero updateHero(@Valid Hero hero){
        Hero heroUpdate = findHeroById(hero.getId());

        heroUpdate.setLevel(hero.getLevel());
        heroUpdate.setName(hero.getName());
        heroUpdate.setPicture(hero.getPicture());
        heroUpdate.setOtherName(hero.getOtherName());
        heroUpdate.setPowers(hero.getPowers());

        return heroRepository.save(heroUpdate);

    }

    public void deleteHero(Long id){
        heroRepository.deleteById(id);
    }



    private Hero findRandom(){
        long countHeroes = findAll().stream().count();
        Random random = new Random();
        int randomHero = random.nextInt((int) countHeroes);
        Pageable pageable = PageRequest.of(randomHero, 1);
        return heroRepository.findAll(pageable).stream().findFirst().orElse(null);
    }
}
