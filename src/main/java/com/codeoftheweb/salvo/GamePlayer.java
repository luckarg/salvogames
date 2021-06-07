package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Entity

public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="playerID")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gameID")
    private Game game;

    @OneToMany(mappedBy="gamePlayer", cascade= CascadeType.ALL)
    private Set<Ship> ships = new HashSet<>();

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER, cascade= CascadeType.ALL)
    private Set<Salvo> salvoes = new HashSet<>();



    private LocalDateTime joinDate;

    public GamePlayer() {}

    public GamePlayer(Game game, Player player, LocalDateTime joinDate) {
    this.player = player;
    this.game = game;
    this.joinDate = joinDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JsonIgnore
    public Player getPlayer() {
        return player;
    }
    public Game getGame() {
        return game;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }
    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    public void setGame(Game game) {
        this.game = game;
    }

    public void addShip(Ship ship){
        this.ships.add(ship);
        ship.setGamePlayer(this);
    }
    public void addSalvo(Salvo salvo){
        this.salvoes.add(salvo);
        salvo.setGamePlayer(this);
    }

    public Set<Ship> getShips(){
        return this.ships;
    }
    public Set<Salvo> getSalvoes(){
        return this.salvoes;
    }

    public GamePlayer getOpponent(){
        return this.getGame().getGamePlayers()
                .stream().filter(gp -> gp.getId() != this.getId())
                .findFirst()
                .orElse(null);
    }

    public Score getScore() {
        return this.player.getScoreByGame(this.game);
    }

    public Map<String,Object> getGamePlayerDTO() {
        Map<String,Object>  gamePlayerDTO = new LinkedHashMap<>();
        gamePlayerDTO.put("gamePlayerId", this.getId());                  //diferencia entre .id. y .getId(). ??
        gamePlayerDTO.put("player", this.getPlayer().getPlayerDTO());   //diferencia entre .player. y .getPlayer(). ??
        gamePlayerDTO.put("score", this.getScore());
                return gamePlayerDTO;
    }



// fijarme esta alternativa en casos de score NULL   (requiere modificar class score etc)
//        Score score =this.getPlayer().getScoreByGame(this.getGame());
//       if(score != null)
//            gamePlayerDTO.put("score", score.getPoints());
//       else
//            gamePlayerDTO.put("score", null);
}