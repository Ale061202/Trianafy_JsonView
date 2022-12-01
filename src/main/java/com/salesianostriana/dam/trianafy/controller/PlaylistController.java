package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.dto.*;
import com.salesianostriana.dam.trianafy.model.Playlist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.repos.PlaylistRepository;
import com.salesianostriana.dam.trianafy.repos.SongRepository;
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

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Tag(name = "Playlist",description = "Este es el controlador de las listas de reproducciones")
public class PlaylistController {

    private final PlaylistRepository repository;
    private final SongRepository repository2;

    private final PlaylistDtoConverter dtoConverter;


    @Operation(summary = "Este método devuelve todas las listas de reproducciones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado las listas de reproducciones",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Playlist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {"id": 12,"name": "Random","numberSongs": 4}
                                            ]                                          
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ninguna lista de reproducción",
                    content = @Content),
    })
    @GetMapping("/list")
    public ResponseEntity<List<GetPlaylistDto>> findAll(){
        List<GetPlaylistDto> result = new ArrayList();
        for (Playlist playList : repository.findAll()){
           result.add(dtoConverter.playlistToGetPlaylistDto(playList));
        }
        return  ResponseEntity.ok(result);
    }

    @Operation(summary = "Este método devuelve una lista de reproducción por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado la lista de reproducción por su id",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Playlist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                            {"id": 12,"name": "Random","description": "Una lista muy loca",
                                                "songs": [{"id": 9, "title": "Enter Sandman", "album": "Metallica", "year": "1991",
                                                    "artist": {"id": 3,"name": "Metallica" }
                                                    },{"id": 8, "title": "Love Again", "album": "Future Nostalgia", "year": "2021",
                                                    "artist": {"id": 2, "name": "Dua Lipa" }
                                                    },{ "id": 9, "title": "Enter Sandman","album": "Metallica", "year": "1991",
                                                    "artist": {"id": 3,"name": "Metallica"}
                                                    }, {"id": 11, "title": "Nothing Else Matters","album": "Metallica","year": "1991",
                                                    "artist": {"id": 3,"name": "Metallica"} }]}
                                            ]                                          
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ninguna lista de reproducción por su id",
                    content = @Content),
    })
    @GetMapping("/list/{id}")
    public ResponseEntity<Playlist> findById(@PathVariable Long id){
        return ResponseEntity.of(repository.findById(id));
    }

    @Operation(summary = "Este método devuelve la creación de una lista de reproducción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha creado un artista",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Playlist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                            {"name" : "Favoritas","description" : "Nueva playlist"}                                            
                                            ]                                          
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha podido crear ninguna lista de reproducción",
                    content = @Content),
    })
    @PostMapping("/list")
    public ResponseEntity<CreatePlaylistDto> createPlaylist(@RequestBody CreatePlaylistDto cs){
        Playlist p = dtoConverter.createPlaylistDtoToPlaylist(cs);
        repository.save(p);
        return ResponseEntity.status(HttpStatus.CREATED).body(cs);
    }

    @Operation(summary = "Este método actualiza la informacion de una lista de reproducción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha podido actualizar una lista de reproducción",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Playlist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {"id": 12,"name": "Random","numberSongs": 4}
                                            ]                                          
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha podido actualizar ninguna lista de reproducción",
                    content = @Content),
    })
    @PutMapping("/list/{id}")
    public ResponseEntity<GetPlaylistDto> customPlaylist(@RequestBody GetPlaylistDto getPlaylistDto, @PathVariable Long id){
        Optional<Playlist> p = repository.findById(id);
        p.get().setName(getPlaylistDto.getName());
        repository.save(p.get());
        return ResponseEntity.of(Optional.of(getPlaylistDto));
    }

    @Operation(summary = "Este método devuelve el borrado de una lista de reproducción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha borrado una lista de reproducción",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Playlist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [                                            
                                            {"id": 12,"name": "Random","description": "Una lista muy loca",
                                                "songs": [{"id": 9, "title": "Enter Sandman", "album": "Metallica", "year": "1991",
                                                    "artist": {"id": 3,"name": "Metallica" }
                                                    },{"id": 8, "title": "Love Again", "album": "Future Nostalgia", "year": "2021",
                                                    "artist": {"id": 2, "name": "Dua Lipa" }
                                                    },{ "id": 9, "title": "Enter Sandman","album": "Metallica", "year": "1991",
                                                    "artist": {"id": 3,"name": "Metallica"}
                                                    }, {"id": 11, "title": "Nothing Else Matters","album": "Metallica","year": "1991",
                                                    "artist": {"id": 3,"name": "Metallica"} }]}
                                            ]                                                                                     
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha podido borrar ninguna lista de reproducción",
                    content = @Content),
    })
    @DeleteMapping("/list/{id}")
    public ResponseEntity<Playlist> deletePlaylist(@PathVariable Long id){
        if (repository.existsById(id))
            repository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Este método devuelve todas las canciones que hay en una lista de reproducción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado las canciones que hay en una lista de reproducción",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Song.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [                                            
                                            {"id": 12,"name": "Random","description": "Una lista muy loca",
                                                "songs": [{"id": 9, "title": "Enter Sandman", "album": "Metallica", "year": "1991",
                                                    "artist": {"id": 3,"name": "Metallica" }
                                                    },{"id": 8, "title": "Love Again", "album": "Future Nostalgia", "year": "2021",
                                                    "artist": {"id": 2, "name": "Dua Lipa" }
                                                    },{ "id": 9, "title": "Enter Sandman","album": "Metallica", "year": "1991",
                                                    "artist": {"id": 3,"name": "Metallica"}
                                                    }, {"id": 11, "title": "Nothing Else Matters","album": "Metallica","year": "1991",
                                                    "artist": {"id": 3,"name": "Metallica"} }]}
                                            ]                                          
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ninguna canción",
                    content = @Content),
    })
    @GetMapping("/list/{id}/song")
    public ResponseEntity<Playlist> findSong(@PathVariable Long id){
        return ResponseEntity.of(repository.findById(id));
    }


    @Operation(summary = "Este método devuelve una canción que hay en una lista de reproducción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado la canción que hay en una lista de reproducción",
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
                    description = "No se ha encontrado ninguna canción",
                    content = @Content),
    })
    @GetMapping("/list/{id}/song/{id2}")
    public ResponseEntity<Song> findSongById(@PathVariable Long id, @PathVariable Long id2){
        Optional<Playlist> p = repository.findById(id);
        if (p.isPresent()){
            Playlist playlist = p.get();
            for (Song song : playlist.getSongs()) {
                if (song.getId() == id2){
                    return ResponseEntity.of(Optional.of(song));
                }
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Este método devuelve la agregación de una canción a la lista de reproducción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha agregado una canción a la lista de reproducción",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Song.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {"id": 12,"name": "Random","description": "Una lista muy loca",
                                                "songs": [{"id": 9, "title": "Enter Sandman", "album": "Metallica", "year": "1991",
                                                    "artist": {"id": 3,"name": "Metallica" }
                                                    },{"id": 8, "title": "Love Again", "album": "Future Nostalgia", "year": "2021",
                                                    "artist": {"id": 2, "name": "Dua Lipa" }
                                                    },{ "id": 9, "title": "Enter Sandman","album": "Metallica", "year": "1991",
                                                    "artist": {"id": 3,"name": "Metallica"}
                                                    }, {"id": 11, "title": "Nothing Else Matters","album": "Metallica","year": "1991",
                                                    "artist": {"id": 3,"name": "Metallica"} }]}                                            
                                            ]                                          
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha podido agregar ninguna canción a la lista de reproduccion",
                    content = @Content),
    })
    @PostMapping("/list/{id}/song/{id2}")
    public ResponseEntity<Playlist> addSongPlaylist(@PathVariable Long id, @PathVariable Long id2, @RequestBody CreateSongDto cs){
        Optional<Playlist> p = repository.findById(id);
        if (p.isPresent()){
            Playlist playlist = p.get();
            Optional<Song> s = repository2.findById(id2);
            if (s.isPresent()){
                playlist.addSong(s.get());
                repository.save(playlist);
                return ResponseEntity.ok(playlist);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Este método devuelve el borrado de una canción en una lista de reproducción")
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
    @DeleteMapping("/list/{id1}/song/{id2}")
    public ResponseEntity<Song> deleteSongById(@PathVariable Long id1, @PathVariable Long id2){
        Optional<Playlist> p = repository.findById(id1);
        if (p.isPresent()){
            Playlist playlist = p.get();
            for (Song song : playlist.getSongs()) {
                if (song.getId() == id2){
                    playlist.deleteSong(song);
                    repository.save(playlist);
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                }
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
