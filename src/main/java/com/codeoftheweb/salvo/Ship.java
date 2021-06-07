package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import java.util.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.ElementCollection;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayerId")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name = "locations")
    private List<String> locations;
    // private List<String> locations = new ArrayList<>(); con esto en lo que estoy copiando, ojo de romper todo...


    public Ship() {
    }

    public Ship(String type, List<String> locations/*, GamePlayer gamePlayer*/) {
        this.type = type;
        this.locations = locations;
        //       this.gamePlayer = gamePlayer;       //es necesario el gameplayer acá?
    }

    public Map<String, Object> getShipDTO() {
        Map<String, Object> shipDTO = new LinkedHashMap<>();
        shipDTO.put("type", this.type);
        shipDTO.put("locations", this.locations);
        return shipDTO;
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

}

