package io.quarkus.workshop.superheroes.model.service;

import io.quarkus.workshop.superheroes.model.entity.Villain;
import io.quarkus.workshop.superheroes.model.repository.IVillainRepository;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;


import java.util.List;
import java.util.Random;

import static javax.transaction.Transactional.TxType.*;

@ApplicationScoped
@Transactional(REQUIRED)
public class VillainService {

    @Inject
    IVillainRepository villainRepository;

    @ConfigProperty(name = "level.multiplier", defaultValue="1.0") double levelMultiplier;

    @Transactional(SUPPORTS)
    public List<Villain> findAllVillains(){
        return villainRepository.findAll();
    }

    @Transactional(SUPPORTS)
    public Villain findVillainById(Long id){
        return villainRepository.findById(id).orElse(null);
    }

    @Transactional(SUPPORTS)
    public Villain findRandomVillain(){
        Villain randomVillain = null;
        while (randomVillain == null){
            randomVillain = findRandom();
        }
        return randomVillain;
    }

    public Villain persistVillain(@Valid Villain villain){
        villain.setLevel((int) Math.round(villain.getLevel() * levelMultiplier));
        return villainRepository.save(villain);
    }

    public Villain updateVillain(@Valid Villain villain){
        Villain editVillain = findVillainById(villain.getId());

        editVillain.setName(villain.getName());
        editVillain.setOtherName(villain.getOtherName());
        editVillain.setLevel(villain.getLevel());
        editVillain.setPicture(villain.getPicture());
        editVillain.setPowers(villain.getPowers());

        return villainRepository.save(editVillain);
    }

    public void deleteVillain(Long id){
        villainRepository.deleteById(id);
    }




    private Villain findRandom(){
        long countVillains = findAllVillains().stream().count();
        Random random = new Random();
        int randomVillain = random.nextInt((int) countVillains);
        Pageable pageable = PageRequest.of(randomVillain, 1);
        return  villainRepository.findAll(pageable).stream().findFirst().orElse(null);
    }

}
