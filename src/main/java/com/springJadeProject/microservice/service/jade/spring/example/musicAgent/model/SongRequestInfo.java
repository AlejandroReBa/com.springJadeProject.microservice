package com.springJadeProject.microservice.service.jade.spring.example.musicAgent.model;

import java.io.Serializable;

/**
 * source: https://github.com/ubenzer/Agent-Based-Programming-Experiments-with-JADE/tree/master/src/pojo
 * MIT License
 *
 * modified and adapted by Alejandro Reyes
 */

public class SongRequestInfo implements Serializable {

  private static final long serialVersionUID = -4333298041938820117L;
  public Song.Genre genre;
  public float maxPricePerSong;
  public float minRating;
  
  public SongRequestInfo(Song.Genre genre, float maxPricePerSong, float minRating) {
    super();
    this.genre = genre;
    this.maxPricePerSong = maxPricePerSong;
    this.minRating = minRating;
  }

}
