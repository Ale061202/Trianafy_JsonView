package com.salesianostriana.dam.trianafy.dto;

import com.salesianostriana.dam.trianafy.model.Playlist;
import org.springframework.stereotype.Component;

@Component
public class PlaylistDtoConverter {

    public Playlist createPlaylistDtoToPlaylist(CreatePlaylistDto c){
        return new Playlist(
                c.getName(),
                c.getDescription()
        );
    }

    public GetPlaylistDto playlistToGetPlaylistDto(Playlist p){
        return GetPlaylistDto
                .builder()
                .id(p.getId())
                .name(p.getName())
                .numberSongs(p.getSongs().size())
                .build();
    }
}
