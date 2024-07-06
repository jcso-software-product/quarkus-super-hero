package io.quarkus.workshop.superheroes.openApi;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ProfileManager;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
@Slf4j
public class VillainApplicationLifeCycle {

    void onStart(@Observes StartupEvent ev) {
        log.info(" __     ___ _ _       _             _    ____ ___ ");
        log.info(" \\ \\   / (_) | | __ _(_)_ __       / \\  |  _ \\_ _|");
        log.info("  \\ \\ / /| | | |/ _` | | '_ \\     / _ \\ | |_) | | ");
        log.info("   \\ V / | | | | (_| | | | | |   / ___ \\|  __/| | ");
        log.info("    \\_/  |_|_|_|\\__,_|_|_| |_|  /_/   \\_\\_|  |___|");

        log.info("The application VILLAIN is starting with profile " + ProfileManager.getActiveProfile());
    }

    void onStop(@Observes ShutdownEvent ev) {
        log.info("The application VILLAIN is stopping...");
    }

}
