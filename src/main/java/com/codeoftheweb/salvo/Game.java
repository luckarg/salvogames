package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.*;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.stream.Collectors;


@Entity

public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private LocalDateTime creationDate;


    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;
    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<Score> scores;

    public Game() {}

    public Game(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Set<GamePlayer> getGamePlayers(){return gamePlayers;}

    public void addGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayers.add(gamePlayer);
        gamePlayer.setGame(this);
    }

    public Set<Score> getScores() {
        return this.scores;
    }
    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    public void addScore(Score score){
        this.scores.add(score);
        score.setGame(this);
    }

    public List<Player> getPlayers() {
        return this.gamePlayers.stream().map(gp -> gp.getPlayer()).collect(Collectors.toList());
    }


    public Map<String,Object> getGameDTO() {
        Map<String,Object>  gameDTO = new LinkedHashMap<>();
        gameDTO.put("id", this.id);
        gameDTO.put("created", this.getCreationDate());
//variante        gameDTO.put("gamePlayers", this.gamePlayers.stream().map(gamePlayer -> gamePlayer.getGamePlayerDTO()));
        gameDTO.put("gamePlayers", this.getGamePlayers().stream().map(GamePlayer::getGamePlayerDTO).collect(Collectors.toList()));
        return gameDTO;
    }
}
