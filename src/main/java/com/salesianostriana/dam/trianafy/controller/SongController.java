package com.salesianostriana.dam.trianafy.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.salesianostriana.dam.trianafy.dto.CreateSongDto;
import com.salesianostriana.dam.trianafy.dto.SongDtoConverter;
import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.repos.ArtistRepository;
import com.salesianostriana.dam.trianafy.repos.SongRepository;
import com.salesianostriana.dam.trianafy.views.View;
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
@Tag(name = "Song",description = "Este es el controlador de los canciones")
public class SongController {

    private final SongRepository repository;
    private final SongDtoConverter dtoConverter;

    private final ArtistRepository artistRepository;

    @Operation(summary = "Este método devuelve todas las canciones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado las canciones",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Song.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {"id": 4, title": "19 días y 500 noches","album": "19 días y 500 noches","year": "1999","artist": {"id": 1,"name": "Joaquín Sabina"}},
                                                {"id": 5, "title": "Donde habita el olvido","album": "19 días y 500 noches","year": "1999","artist": {"id": 1,"name": "Joaquín Sabina"}}
                                            ]                                          
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ninguna canción",
                    content = @Content),
    })
    @GetMapping("/song/")
    public ResponseEntity<List<Song>> findAll(){
        return ResponseEntity.ok(repository.findAll());
    }

    @Operation(summary = "Este método devuelve una canción por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado la canción por su id",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Song.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {"id": 4, title": "19 días y 500 noches","album": "19 días y 500 noches","year": "1999","artist": {"id": 1,"name": "Joaquín Sabina"}},
                                            ]                                          
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ninguna canción por su id",
                    content = @Content),
    })
    @GetMapping("/song/{id}")
    public ResponseEntity<Song> findbyId(@PathVariable Long id){
        return ResponseEntity.of(repository.findById(id));
    }

    @Operation(summary = "Este método devuelve la creación de una canción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha creado una canción",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Song.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {"id": 4, title": "19 días y 500 noches","album": "19 días y 500 noches","year": "1999","artist": {"id": 1,"name": "Joaquín Sabina"}},
                                            ]                                          
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha podido crear ninguna canción",
                    content = @Content),
    })
    @PostMapping("/song/")
    public ResponseEntity<Song> createSong(@RequestBody CreateSongDto cs){
        if (cs.getArtistId() == null){
            return ResponseEntity.badRequest().build();
        }

        Song song = dtoConverter.createSongDtoToSong(cs);
        Artist artist = artistRepository.findById(cs.getArtistId()).orElse(null);

        song.setArtist(artist);

        song = repository.save(song);

        return ResponseEntity.status(HttpStatus.CREATED).body(song);
    }

    @Operation(summary = "Este método actualiza la informacion de una canción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha podido actualizar una canción",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Song.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {"id": 4, title": "19 días y 500 noches","album": "19 días y 500 noches","year": "1999","artist": {"id": 1,"name": "Joaquín Sabina"}},
                                            ]                                          
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha podido actualizar ninguna canción",
                    content = @Content),
    })
    @PutMapping("/song/{id}")
    public ResponseEntity<Song> customSong(@RequestBody CreateSongDto cs, @PathVariable Long id){
        if (cs.getArtistId() == null){
            return ResponseEntity.badRequest().build();
        }

        Song song = dtoConverter.createSongDtoToSong(cs);
        Artist artist = artistRepository.findById(cs.getArtistId()).orElse(null);

        return ResponseEntity.of(repository.findById(id)
                .map(old -> {
                    old.setYear(song.getYear());
                    old.setTitle(song.getTitle());
                    old.setAlbum(song.getAlbum());
                    old.setArtist(artist);
                    return Optional.of(repository.save(old));
                })
                .orElse(Optional.empty())
        );
    }

    @Operation(summary = "Este método devuelve el borrado de una canción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha borrado una canción",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Song.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {"id": 4, "title": "19 días y 500 noches","album": "19 días y 500 noches","year": "1999","artist": {"id": 1,"name": "Joaquín Sabina"}},
                                            ]                                          
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha podido borrar ninguna canción",
                    content = @Content),
    })
    @DeleteMapping("/song/{id}")
    public ResponseEntity<Song> deleteSong(@PathVariable Long id){
        if(repository.existsById(id))
            repository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/song/jsonview")
    @JsonView(View.Base.class)
    List<Song> findSongsByJsonView(){
        return repository.findAll();
    }
}
