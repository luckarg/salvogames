package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity //Spring - create table for player class into database

public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String username;
    private String password;

    public String getPassword(){
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

//    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
//    private Set<GamePlayer> gamePlayers;

//    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
//    private Set<Score> scores;
    @OneToMany(mappedBy="player", fetch=FetchType.EAGER, cascade= CascadeType.ALL)
    private Set<GamePlayer> gamePlayers = new HashSet<>();

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER, cascade= CascadeType.ALL)
    private Set<Score> scores = new HashSet<>();


    public Player() { }

    public Player(String userName, String password) {
        this.username = userName;
        this.password= password;
    }

    public String getUsername() {
        return username;
    }

    public long getId() {
        return this.id;
    }

    public Set<Score> getScores() {
        return scores;
    }
    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }


    public void addGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayers.add(gamePlayer);
        gamePlayer.setPlayer(this);
    }

    public void addScore(Score score){
        this.scores.add(score);
        score.setPlayer(this);
    }

    @JsonIgnore
    public List<Game> getGames() {
        return this.gamePlayers.stream().map(x -> x.getGame()).collect(Collectors.toList());
    }


    public Score getScoreByGame(Game game) {
        return this.scores
                .stream()
                .filter(score -> score.getGame().equals(game))
                .findFirst().orElse(null);
    }


    public Map<String,Object> getPlayerDTO() {
        Map<String,Object>  playerDTO = new LinkedHashMap<>();
        playerDTO.put("id", this.getId());
        playerDTO.put("email", this.getUsername());
        return playerDTO;
    }


    public Map<String,Object> getLeaderboardDTO() {
        Map<String,Object>  leaderboardDTO = new LinkedHashMap<>();
        leaderboardDTO.put("Name", this.username);
        float won = this.scores.stream().filter(score -> score.getScore() == 1D).count();
        leaderboardDTO.put("Won", won);
        leaderboardDTO.put("Lost", this.scores.stream().filter(score -> score.getScore() == 0D).count());
        float tied = this.scores.stream().filter(score -> score.getScore() == 0.5D).count();
        leaderboardDTO.put("Tied", tied);
        float total = won+(tied/2);
        leaderboardDTO.put("Total", total);
        return leaderboardDTO;
    }





}
