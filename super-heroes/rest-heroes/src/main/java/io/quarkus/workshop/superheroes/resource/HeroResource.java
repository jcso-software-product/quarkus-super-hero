package io.quarkus.workshop.superheroes.resource;

import io.quarkus.workshop.superheroes.model.entity.Hero;
import io.quarkus.workshop.superheroes.model.service.HeroService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestPath;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/api/heroes")
@Tag(name = "heroes")
@Slf4j
public class HeroResource {


    @Inject
    HeroService heroService;

    @Operation(summary = "Returns all the heroes from the database")
    @GET
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Hero.class, type = SchemaType.ARRAY)))
    @APIResponse(responseCode = "204", description = "No Heroes")
    public Response getAllHeroes() {
        List<Hero> heroes = heroService.findAll();
        return Response.ok(heroes).build();
    }


    @Operation(summary = "Returns a random hero")
    @GET
    @Path("/random")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Hero.class, required = true)))
    public Response getRandomHero(){
        Hero hero = heroService.findRandomHero();
        return Response.ok(hero).build();
    }

    @Operation(summary = "Returns a hero for a given identifier")
    @GET
    @Path("/{id}")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Hero.class)))
    @APIResponse(responseCode = "204", description = "The hero is not found for a given identifier")
    public Response getHeroById(@RestPath Long id){
        Hero hero = heroService.findHeroById(id);
        if(hero == null){
            return Response.noContent().build();
        }else{
            return Response.ok(hero).build();
        }



    }

    @Operation(summary = "Creates a valid hero")
    @POST
    @APIResponse(responseCode = "201", description = "The URI of the created hero", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = URI.class)))
    public Response create(@Valid Hero hero, @Context UriInfo uriInfo){
      Hero create = heroService.persistHero(hero);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(hero.getId()));
        log.debug("New hero created with URI: " + builder.build().toString());
        return Response.created(builder.build()).build();
    }

    @Operation(summary = "Updates an exiting  hero")
    @PUT
    @APIResponse(responseCode = "200", description = "The updated hero", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Hero.class)))
    public Response updateVillain(@Valid Hero hero){
        hero = heroService.updateHero(hero);
        log.debug("Hero updated with new value: " + hero);
        return Response.ok(hero).build();
    }

    @Operation(summary = "Deletes an exiting hero")
    @DELETE
    @Path("/{id}")
    @APIResponse(responseCode = "204")
    public Response deleteVillain(@RestPath Long id){
        heroService.deleteHero(id);
        log.debug("Hero deleted with id: " + id);
        return Response.noContent().build();
    }


}
