package io.quarkus.workshop.superheroes.villain;

import io.quarkus.workshop.superheroes.model.entity.Villain;
import io.quarkus.workshop.superheroes.model.service.VillainService;
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
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import static javax.ws.rs.core.MediaType.*;

@Path("/api/villains")
@Slf4j
@Tag(name = "villains")
public class VillainResource {

    @Inject
    VillainService villainService;

    @Operation(summary = "Returns a random villain")
    @GET
    @Path("/random")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON,
    schema = @Schema(implementation = Villain.class, required = true)))
    public Response getRandomVillain() {
        Villain villain = villainService.findRandomVillain();
        log.debug("Found random villain: " + villain);
        return Response.ok(villain).build();
    }

    @Operation(summary = "Returns all the villains from the database")
    @GET
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Villain.class, type = SchemaType.ARRAY)))
    @APIResponse(responseCode = "204", description = "No villains")
    public Response getAllVillains(){
        List<Villain> villains = villainService.findAllVillains();
        log.debug("Total number of villains: " + villains);
        return Response.ok(villains).build();
    }

    @Operation(summary = "Returns a villain for a given identifier")
    @GET
    @Path("/{id}")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Villain.class)))
    @APIResponse(responseCode = "204", description = "The villain is not found for a given identifier")
    public Response getVillain(@RestPath Long id){
        Villain villain = villainService.findVillainById(id);
        if(villain != null){
            log.debug("Found villain: " + villain);
            return Response.ok(villain).build();
        }else {
            log.debug("No villain found with id: " + id);
            return Response.noContent().build();
        }
    }

    @Operation(summary = "Creates a valid villain")
    @POST
    @APIResponse(responseCode = "201", description = "The URI of the created villain", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = URI.class)))
    public Response createVillain(@Valid Villain villain, @Context UriInfo uriInfo){
        villain = villainService.persistVillain(villain);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(villain.getId()));
        log.debug("New villain created with URI: " + builder.build().toString());
        return Response.created(builder.build()).build();
    }

    @Operation(summary = "Updates an exiting  villain")
    @PUT
    @APIResponse(responseCode = "200", description = "The updated villain", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Villain.class)))
    public Response updateVillain(@Valid Villain villain){
        villain = villainService.updateVillain(villain);
        log.debug("Villain updated with new value: " + villain);
        return Response.ok(villain).build();
    }

    @Operation(summary = "Deletes an exiting villain")
    @DELETE
    @Path("/{id}")
    @APIResponse(responseCode = "204")
    public Response deleteVillain(@RestPath Long id){
        villainService.deleteVillain(id);
        log.debug("Villain deleted with id: " + id);
        return Response.noContent().build();
    }

}
