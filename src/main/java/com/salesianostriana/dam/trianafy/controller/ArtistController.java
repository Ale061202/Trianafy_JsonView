package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.service.ArtistService;
import com.salesianostriana.dam.trianafy.service.SongService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Tag(name = "Artist",description = "Este es el controlador de los artistas")
public class ArtistController {

    private final ArtistService service;
    private final SongService songService;

    @Operation(summary = "Este método devuelve todos los artistas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado los artistas",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Artist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {"id": 1, "nombre": "Omar Montes"},
                                                {"id": 2, "nombre": "David Bisbal"}
                                            ]                                          
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ningún artista",
                    content = @Content),
    })
    @GetMapping("/artist/")
    public ResponseEntity<List<Artist>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Este método devuelve un artista por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado el artista por su id",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Artist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {"id": 1, "nombre": "Omar Montes"}                                                
                                            ]                                          
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ningún artista por su id",
                    content = @Content),
    })
    @GetMapping("/artist/{id}")
    public ResponseEntity<Artist> findById(@PathVariable Long id){
        return ResponseEntity.of(service.findById(id));
    }

    @Operation(summary = "Este método devuelve la creación de un artista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha creado un artista",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Artist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {"id": 3, "nombre": "Bad Bunny"}
                                            ]                                          
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha podido crear ningún artista",
                    content = @Content),
    })
    @PostMapping("/artist/")
    public ResponseEntity<Artist> createArtist(@RequestBody Artist artist){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.add(artist));
    }

    @Operation(summary = "Este método actualiza la informacion de un artista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha podido actualizar un artista",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Artist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {"id": 1, "nombre": "David Bisbal"}
                                            ]                                          
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha podido actualizar ningún artista",
                    content = @Content),
    })
    @PutMapping("/artist/{id}")
    public ResponseEntity<Artist> customArtist(@RequestBody Artist artist, @PathVariable Long id){
        return ResponseEntity.of(service.findById(id)
                .map(old -> {
                    old.setName(artist.getName());
                    return Optional.of(service.edit(old));
                })
                .orElse(Optional.empty())
        );
    }

    @Operation(summary = "Este método devuelve el borrado de un artista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha borrado un artista",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Artist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {"id": 3, "nombre": "Bad Bunny"}
                                            ]                                          
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha podido borrar ningún artista",
                    content = @Content),
    })
    @DeleteMapping("/artist/{id}")
    public ResponseEntity<Artist> deleteArtist(@PathVariable Long id, Artist artist){
        List<Song> listSong = songService.findAll();
        for (Song song: listSong) {
            if (song.getArtist().getId() == id){
                song.setArtist(null);
            }
        }
        service.delete(artist);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
