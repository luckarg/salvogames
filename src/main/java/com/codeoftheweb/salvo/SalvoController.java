package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.*;


@RestController
@RequestMapping("/api")

public class SalvoController {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GamePlayerRepository gamePlayerRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private ScoreRepository scoreRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /* >>original sin user or guest
        @RequestMapping("/games")
        public List<Object> getGamesInfo () {
            return gameRepository.findAll().stream().map(Game::getGameDTO).collect(Collectors.toList());
        }
    */
    @RequestMapping("/games")
    public Map<String, Object> getGamesInfo(Authentication authentication) {
        Map<String, Object> gameDTO = new LinkedHashMap<>();
        gameDTO.put("games", gameRepository.findAll().stream().map(Game::getGameDTO).collect(Collectors.toList()));
        if (isGuest(authentication)) {
            gameDTO.put("player", "guest");
        } else {
            gameDTO.put("player", authentication.getName());
        }
        return gameDTO;
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }


    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createGame(Authentication authentication) {
        ResponseEntity<Map<String, Object>> response;
        if (isGuest(authentication)) {
            response = new ResponseEntity<>(makeMap("error", "You must be logged in first"), HttpStatus.UNAUTHORIZED);
        } else {
            Player player = playerRepository.findByUsername(authentication.getName());
            Game newGame = gameRepository.save(new Game(LocalDateTime.now()));
            GamePlayer newGamePlayer = gamePlayerRepository.save(new GamePlayer(newGame, player, newGame.getCreationDate()));

            response = new ResponseEntity<>(makeMap("gamePlayerId", newGamePlayer.getId()), HttpStatus.CREATED);
        }

        return response;
    }


    @RequestMapping(path = "/games/{gameId}/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> joinGame(Authentication authentication, @PathVariable long gameId) {
        ResponseEntity<Map<String, Object>> response;
        if (isGuest(authentication)) {
            response = new ResponseEntity<>(makeMap("error", "You must be logged in first"), HttpStatus.UNAUTHORIZED);
        } else {

            Game game = gameRepository.findById(gameId).orElse(null);
            if (game == null) {
                response = new ResponseEntity<>(makeMap("error", "No such game"), HttpStatus.NOT_FOUND);
            } else if (game.getGamePlayers().size() > 1) {
                response = new ResponseEntity<>(makeMap("error", "game is full"), HttpStatus.FORBIDDEN);
            } else {
                Player player = playerRepository.findByUsername(authentication.getName());
                if (game.getGamePlayers().stream().anyMatch(gp -> gp.getPlayer().getId() == player.getId())) {
                    response = new ResponseEntity<>(makeMap("error", "you can't play against yourself!"), HttpStatus.FORBIDDEN);
                } else {
                    GamePlayer newGamePlayer = gamePlayerRepository.save(new GamePlayer(game, player, LocalDateTime.now()));
                    response = new ResponseEntity<>(makeMap("gamePlayerId", newGamePlayer.getId()), HttpStatus.CREATED);
                }
            }

        }

        return response;
    }


    // spring doc condetodo https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-requestmapping
// y seguir con https://www.leveluplunch.com/java/tutorials/014-post-json-to-spring-rest-webservice/
// https://spring.io/guides/tutorials/bookmarks/#_building_a_rest_service
// https://www.leveluplunch.com/java/tutorials/014-post-json-to-spring-rest-webservice/
    @RequestMapping("/game_view/{gamePlayerId}")
    public ResponseEntity<Map<String, Object>> getGameView(@PathVariable long gamePlayerId, Authentication authentication) {
        ResponseEntity<Map<String, Object>> response;
        if (isGuest(authentication)) {
            response = new ResponseEntity<>(makeMap("error", "You must be logged in first"), HttpStatus.UNAUTHORIZED);
        } else {
            GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).orElse(null);
            Player player = playerRepository.findByUsername(authentication.getName());
            if (gamePlayer == null) {
                response = new ResponseEntity<>(makeMap("error", "No such game"), HttpStatus.NOT_FOUND);
            } else if (gamePlayer.getPlayer().getId() != player.getId()) {
                response = new ResponseEntity<>(makeMap("error", "This is not your game"), HttpStatus.UNAUTHORIZED);
            } else {
                response = new ResponseEntity<>(this.gameViewDTO(gamePlayer), HttpStatus.OK);
            }
        }
        return response;
    }

    private Map<String, Object> gameViewDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<>();

        if (gamePlayer != null) {
            dto.put("id", gamePlayer.getGame().getId());
            dto.put("creationDate", gamePlayer.getGame().getCreationDate());
            dto.put("gamePlayers", gamePlayer.getGame().getGamePlayers().stream().map(GamePlayer::getGamePlayerDTO));
            dto.put("ships", gamePlayer.getShips().stream().map(Ship::getShipDTO));
            dto.put("salvoes", gamePlayer.getGame().getGamePlayers()
                    .stream().flatMap(gp -> gp.getSalvoes()
                            .stream().map(Salvo::getSalvoDTO)));
        } else {
            dto.put("error", "no such game");
        }
        return dto;
    }


    @RequestMapping("/leaderboard")
    public List<Map<String, Object>> getLeaderboard() {
        return playerRepository.findAll()
                .stream().map(Player::getLeaderboardDTO)
                .collect(Collectors.toList());
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }


    @RequestMapping("/players")
    public List<Object> getPlayerInfo() {
        return playerRepository.findAll().stream().map(Player::getPlayerDTO).collect(Collectors.toList());
    }

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createUser(
            @RequestParam("username") String username, @RequestParam("password") String password) {
        ResponseEntity<Map<String, Object>> response;
        Player player = playerRepository.findByUsername(username);

        if (username.isEmpty() || password.isEmpty()) {
            response = new ResponseEntity<>(makeMap("error", "Empty field(s)"), HttpStatus.FORBIDDEN);
        } else if (player != null) {
            response = new ResponseEntity<>(makeMap("error", "Username already exists"), HttpStatus.CONFLICT);
        } else {
            Player newPlayer = playerRepository.save(new Player(username, passwordEncoder.encode(password)));
            response = new ResponseEntity<>(makeMap("id", newPlayer.getId()), HttpStatus.CREATED);
        }
        return response;
    }


    @RequestMapping(path = "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> addShips(Authentication authentication, @PathVariable long gamePlayerId, @RequestBody List<Ship> ships) {
        ResponseEntity<Map<String, Object>> response;
        if (isGuest(authentication)) {
            response = new ResponseEntity<>(makeMap("error", "you must be logged in"), HttpStatus.UNAUTHORIZED);
        } else {
            GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).orElse(null);
            Player player = playerRepository.findByUsername(authentication.getName());
            if (gamePlayer == null) {
                response = new ResponseEntity<>(makeMap("error", "no such game"), HttpStatus.NOT_FOUND);
            } else if (gamePlayer.getPlayer().getId() != player.getId()) {
                response = new ResponseEntity<>(makeMap("error", "this is not your game"), HttpStatus.UNAUTHORIZED);
            } else if (gamePlayer.getShips().size() > 0) {
                response = new ResponseEntity<>(makeMap("error", "you already have ships"), HttpStatus.FORBIDDEN);
            } else if (ships == null || ships.size() != 5) {
                response = new ResponseEntity<>(makeMap("error", "you must add 5 ships"), HttpStatus.FORBIDDEN);
            } else {
                if (ships.stream().anyMatch(this::isOutOfRange)) {
                    response = new ResponseEntity<>(makeMap("error", "you have ships out of range"), HttpStatus.FORBIDDEN);
                } else if (ships.stream().anyMatch(this::isNotConsecutive)) {
                    response = new ResponseEntity<>(makeMap("error", "your ships are not consecutive"), HttpStatus.FORBIDDEN);
                } else if (this.areOverlapped(ships)) {
                    response = new ResponseEntity<>(makeMap("error", "your ships are overlapped"), HttpStatus.FORBIDDEN);
                } else {

                    ships.forEach(gamePlayer::addShip);

                    gamePlayerRepository.save(gamePlayer);

                    response = new ResponseEntity<>(makeMap("success", "ships added"), HttpStatus.CREATED);
                }
            }
        }

        return response;
    }

    @RequestMapping(path = "/games/players/{gamePlayerId}/salvoes", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> addSalvo(Authentication authentication, @PathVariable long gamePlayerId, @RequestBody List<String> shots) {
        ResponseEntity<Map<String, Object>> response;
        if (isGuest(authentication)) {
            response = new ResponseEntity<>(makeMap("error", "you must be logged in"), HttpStatus.UNAUTHORIZED);
        } else {
            GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).orElse(null);
            Player player = playerRepository.findByUsername(authentication.getName());
            if (gamePlayer == null) {
                response = new ResponseEntity<>(makeMap("error", "no such game"), HttpStatus.NOT_FOUND);
            } else if (gamePlayer.getPlayer().getId() != player.getId()) {
                response = new ResponseEntity<>(makeMap("error", "this is not your game"), HttpStatus.UNAUTHORIZED);
            } else if (shots.size() != 5) {
                response = new ResponseEntity<>(makeMap("error", "wrong number of shots"), HttpStatus.FORBIDDEN);
            } else {
                int turn = gamePlayer.getSalvoes().size() + 1;

                Salvo salvo = new Salvo(turn, shots);
                gamePlayer.addSalvo(salvo);

                gamePlayerRepository.save(gamePlayer);

                response = new ResponseEntity<>(makeMap("success", "salvo added"), HttpStatus.CREATED);

            }
        }

        return response;
    }


    private boolean isOutOfRange(Ship ship) {

        for (String cell : ship.getLocations()) {
            if (!(cell instanceof String) || cell.length() < 2) {
                return true;
            }
            char y = cell.substring(0, 1).charAt(0);
            Integer x;
            try {
                x = Integer.parseInt(cell.substring(1));
            } catch (NumberFormatException e) {
                x = 99;
            }
            ;

            if (x < 1 || x > 10 || y < 'A' || y > 'J') {
                return true;
            }
        }

        return false;
    }

    private boolean isNotConsecutive(Ship ship) {

        List<String> cells = ship.getLocations();

        boolean isVertical = cells.get(0).charAt(0) != cells.get(1).charAt(0);

        for (int i = 0; i < cells.size(); i++) {

            if (i < cells.size() - 1) {
                if (isVertical) {
                    char yChar = cells.get(i).substring(0, 1).charAt(0);
                    char compareChar = cells.get(i + 1).substring(0, 1).charAt(0);
                    if (compareChar - yChar != 1) {
                        return true;
                    }
                } else {
                    Integer xInt = Integer.parseInt(cells.get(i).substring(1));
                    Integer compareInt = Integer.parseInt(cells.get(i + 1).substring(1));
                    if (compareInt - xInt != 1) {
                        return true;
                    }
                }
            }


            for (int j = i + 1; j < cells.size(); j++) {

                if (isVertical) {
                    if (!cells.get(i).substring(1).equals(cells.get(j).substring(1))) {
                        return true;
                    }

                } else {
                    if (!cells.get(i).substring(0, 1).equals(cells.get(j).substring(0, 1))) {
                        return true;
                    }

                }
            }
        }

        return false;
    }

    private boolean areOverlapped(List<Ship> ships) {
        List<String> allCells = new ArrayList<>();

        ships.forEach(ship -> allCells.addAll(ship.getLocations()));

        for (int i = 0; i < allCells.size(); i++) {
            for (int j = i + 1; j < allCells.size(); j++) {
                if (allCells.get(i).equals(allCells.get(j))) {
                    return true;
                }
            }
        }

        return false;
    }

}