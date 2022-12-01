package com.salesianostriana.dam.trianafy.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.salesianostriana.dam.trianafy.views.View;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Song {

    @Id
    @GeneratedValue
    private Long id;

    @JsonView(View.Base.class)
    private String title;
    @JsonView(View.Base.class)
    private String album;
    @Column(name = "year_of_song")
    @JsonView(View.Base.class)
    private String year;

    @ManyToOne(fetch = FetchType.EAGER)
    private Artist artist;

    public Song(String title, String album, String year){
        this.title = title;
        this.album = album;
        this.year = year;
    }


}
