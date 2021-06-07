package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private int turn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayerId")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name = "locations")
    private List<String> locations;
// así de donde copio, sin el @column     private List<String> locations = new ArrayList<>();

    public Salvo() {
    }

    public Salvo(int turn, List<String> locations) {
        this.turn = turn;
//        this.gamePlayer = gamePlayer;           //sacar el gameplayer acá??
        this.locations = locations;
    }

    public GamePlayer getGamePlayer() {
        return this.gamePlayer;
    }

    public List<String> getHits(List<String> myShots, Set<Ship> opponentShips){

        List<String> allEnemyLocs = new ArrayList<>();

        opponentShips.forEach(ship -> allEnemyLocs.addAll(ship.getLocations()));

        return myShots
                .stream()
                .filter(shot -> allEnemyLocs
                        .stream()
                        .anyMatch(loc -> loc.equals(shot)))
                .collect(Collectors.toList());

    }

    public List<Ship> getSunkenShips(Set<Salvo> mySalvoes, Set<Ship> opponentShips){

        List<String> allShots = new ArrayList<>();

        mySalvoes.forEach(salvo -> allShots.addAll(salvo.getLocations()));

        return opponentShips
                .stream()
                .filter(ship -> allShots.containsAll(ship.getLocations()))
                .collect(Collectors.toList());
    }


    public Map<String, Object> getSalvoDTO() {
        Map<String, Object> salvoDTO = new LinkedHashMap<>();
        salvoDTO.put("turn", this.getTurn());
        salvoDTO.put("player", this.getGamePlayer().getPlayer().getId());
        salvoDTO.put("locations", this.getLocations());

        GamePlayer opponent = this.getGamePlayer().getOpponent();

        if(opponent != null){

            Set<Ship> enemyShips = opponent.getShips();

            salvoDTO.put("hits", this.getHits(this.getLocations(),enemyShips));

            Set<Salvo> mySalvoes = this.getGamePlayer()
                    .getSalvoes()
                    .stream()
                    .filter(salvo -> salvo.getTurn() <= this.getTurn())
                    .collect(Collectors.toSet());

            salvoDTO.put("sunken", this.getSunkenShips(mySalvoes, enemyShips).stream().map(Ship::getShipDTO));
        }


        return salvoDTO;
    }


    public long getId() {
        return this.id;
    }

    public List<String> getLocations() {
        return this.locations;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public int getTurn() {
        return this.turn;
    }

}
