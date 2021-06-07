package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private double score;
    private LocalDateTime finishDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "playerID")
    private Player player;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gameID")
    private Game game;
//ver variante @OneToOne si es mas sencillo (menos codigo); en realidad la parte del DTO se haría más larga


    public Score() {
    }

    public Score(double score, Game game, Player player, LocalDateTime finishDate) {
        this.score = score;
        this.game = game;
        this.player = player;
        this.finishDate = finishDate;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDateTime finishDate) {
        this.finishDate = finishDate;
    }

//    @JsonIgnore
    public Game getGame() {
        return game;
    }

    public double getScore() {
        return this.score;
    }
    public void setScore(double score){
        this.score = score;
    }


//    @JsonIgnore
    public Player getPlayer() {
        return player;
    }

//need? delete...
    public void setId(long id) {
        this.id = id;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }
    public void setGame(Game game) {
        this.game = game;
    }

}