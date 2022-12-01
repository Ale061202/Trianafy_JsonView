package com.salesianostriana.dam.trianafy.dto;

import com.salesianostriana.dam.trianafy.model.Song;
import org.springframework.stereotype.Component;

@Component
public class SongDtoConverter {

    public Song createSongDtoToSong(CreateSongDto c) {
        return new Song(
                c.getTitle(),
                c.getAlbum(),
                c.getYear()
        );
    }

    public GetSongDto songtoGetSongDto(Song s){
        return GetSongDto
                .builder()
                .title(s.getTitle())
                .album(s.getAlbum())
                .year(s.getYear())
                .artistName(s.getArtist().getName())
                .build();
    }
}
