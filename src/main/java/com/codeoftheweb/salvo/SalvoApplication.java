package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Arrays;


@SpringBootApplication
public class SalvoApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SalvoApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(GameRepository gameRepository, PlayerRepository playerRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, SalvoRepository salvoRepository, ScoreRepository scoreRepository) {
        return (args) -> {

            PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

            Player player1 = new Player("j.bauer@ctu.gov", passwordEncoder.encode("24"));
            Player player2 = new Player("c.obrian@ctu.gov", passwordEncoder.encode("42"));
            Player player3 = new Player("kim_bauer@gmail.com", passwordEncoder.encode("kb"));
            Player player4 = new Player("t.almeida@ctu.gov", passwordEncoder.encode("mole"));

            playerRepository.save(player1);
            playerRepository.save(player2);
            playerRepository.save(player3);
            playerRepository.save(player4);

            Game game1 = gameRepository.save(new Game(LocalDateTime.now()));
            Game game2 = gameRepository.save(new Game(LocalDateTime.now().plusHours(1)));
            Game game3 = gameRepository.save(new Game(LocalDateTime.now().plusHours(2)));
            Game game4 = gameRepository.save(new Game(LocalDateTime.now().plusHours(3)));
            Game game5 = gameRepository.save(new Game(LocalDateTime.now().plusHours(4)));
            Game game6 = gameRepository.save(new Game(LocalDateTime.now().plusHours(5)));
            Game game7 = gameRepository.save(new Game(LocalDateTime.now().plusHours(6)));
            Game game8 = gameRepository.save(new Game(LocalDateTime.now().plusHours(7)));

            GamePlayer gamePlayer1 = new GamePlayer(game1, player1, LocalDateTime.now());
            GamePlayer gamePlayer2 = new GamePlayer(game1, player2, LocalDateTime.now());

            GamePlayer gamePlayer3 = new GamePlayer(game2, player1, LocalDateTime.now());
            GamePlayer gamePlayer4 = new GamePlayer(game2, player2, LocalDateTime.now());

            GamePlayer gamePlayer5 = new GamePlayer(game3, player2, LocalDateTime.now());
            GamePlayer gamePlayer6 = new GamePlayer(game3, player4, LocalDateTime.now());

            GamePlayer gamePlayer7 = new GamePlayer(game4, player2, LocalDateTime.now());
            GamePlayer gamePlayer8 = new GamePlayer(game4, player1, LocalDateTime.now());

            GamePlayer gamePlayer9 = new GamePlayer(game5, player4, LocalDateTime.now());
            GamePlayer gamePlayer10 = new GamePlayer(game5, player1, LocalDateTime.now());

            GamePlayer gamePlayer11 = new GamePlayer(game6, player3, LocalDateTime.now());

            GamePlayer gamePlayer12 = new GamePlayer(game7, player4, LocalDateTime.now());

            GamePlayer gamePlayer13 = new GamePlayer(game8, player3, LocalDateTime.now());
            GamePlayer gamePlayer14 = new GamePlayer(game8, player4, LocalDateTime.now());


            List<String> location1 = Arrays.asList("H2", "H3", "H4");
            List<String> location2 = Arrays.asList("E1", "F1", "G1");
            List<String> location3 = Arrays.asList("B4", "B5");
            List<String> location4 = Arrays.asList("B5", "C5", "D5");
            List<String> location5 = Arrays.asList("F1", "F2");

            List<String> location6 = Arrays.asList("B5", "C5", "D5");
            List<String> location7 = Arrays.asList("C6", "C7");
            List<String> location8 = Arrays.asList("A2", "A3", "A4");
            List<String> location9 = Arrays.asList("G6", "H6");

            List<String> location10 = Arrays.asList("B5", "C5", "D5");
            List<String> location11 = Arrays.asList("C6", "C7");
            List<String> location12 = Arrays.asList("A2", "A3", "A4");
            List<String> location13 = Arrays.asList("G6", "H6");

            List<String> location14 = Arrays.asList("B5", "C5", "D5");
            List<String> location15 = Arrays.asList("C6", "C7");
            List<String> location16 = Arrays.asList("A2", "A3", "A4");
            List<String> location17 = Arrays.asList("G6", "H6");

            List<String> location18 = Arrays.asList("B5", "C5", "D5");
            List<String> location19 = Arrays.asList("C6", "C7");
            List<String> location20 = Arrays.asList("A2", "A3", "A4");
            List<String> location21 = Arrays.asList("G6", "H6");

            List<String> location22 = Arrays.asList("B5", "C5", "D5");
            List<String> location23 = Arrays.asList("C6", "C7");
            List<String> location24 = Arrays.asList("B5", "C5", "D5");
            List<String> location25 = Arrays.asList("C6", "C7");
            List<String> location26 = Arrays.asList("A2", "A3", "A4");
            List<String> location27 = Arrays.asList("G6", "H6");

//game1
            gamePlayer1.addShip(new Ship("destroyer", location1));
            gamePlayer1.addShip(new Ship("submarine", location2));
            gamePlayer1.addShip(new Ship("patrol_boat", location3));
            gamePlayer2.addShip(new Ship("destroyer", location4));
            gamePlayer2.addShip(new Ship("patrol_boat", location5));
//game2
            gamePlayer3.addShip(new Ship("destroyer", location6));
            gamePlayer3.addShip(new Ship("patrol_boat", location7));
            gamePlayer4.addShip(new Ship("submarine", location8));
            gamePlayer4.addShip(new Ship("patrol_boat", location9));
//game3
            gamePlayer5.addShip(new Ship("destroyer", location10));
            gamePlayer5.addShip(new Ship("patrol_boat", location11));
            gamePlayer6.addShip(new Ship("submarine", location12));
            gamePlayer6.addShip(new Ship("patrol_boat", location13));
//game4
            gamePlayer7.addShip(new Ship("destroyer", location14));
            gamePlayer7.addShip(new Ship("patrol_boat", location15));
            gamePlayer8.addShip(new Ship("submarine", location16));
            gamePlayer8.addShip(new Ship("patrol_boat", location17));
//game5
            gamePlayer9.addShip(new Ship("destroyer", location18));
            gamePlayer9.addShip(new Ship("patrol_boat", location19));
            gamePlayer10.addShip(new Ship("submarine", location20));
            gamePlayer10.addShip(new Ship("patrol_boat", location21));
//game6
            gamePlayer11.addShip(new Ship("destroyer", location22));
            gamePlayer11.addShip(new Ship("patrol_boat", location23));
//game7 empty for now
//game8
            gamePlayer13.addShip(new Ship("destroyer", location24));
            gamePlayer13.addShip(new Ship("patrol_boat", location25));
            gamePlayer14.addShip(new Ship("submarine", location26));
            gamePlayer14.addShip(new Ship("patrol_boat", location27));
//e agora....          shipRepository.saveAll(Arrays.asList(ship1, ship2, ship3, ship4, ship5, ship6, ship7, ship8, ship9, ship10, ship11, ship12, ship13, ship14, ship15, ship16, ship17, ship18, ship19, ship20, ship21, ship22, ship23, ship24, ship25, ship26, ship27));

//salvoes
            //game1
            List<String> location100 = Arrays.asList("B5", "C5", "F1"); //salvo bauer a obrian
            List<String> location101 = Arrays.asList("B4", "B5", "B6"); //obrian a bauer
            List<String> location102 = Arrays.asList("F2", "D5"); //salvo bauer a obrian
            List<String> location103 = Arrays.asList("E1", "H3", "A2"); //obrian a bauer
            //game2
            List<String> location104 = Arrays.asList("A2", "A4", "G6");
            List<String> location105 = Arrays.asList("B5", "D5", "C7");
            List<String> location106 = Arrays.asList("A3", "H6");
            List<String> location107 = Arrays.asList("C5", "C6");
            //game3
            List<String> location108 = Arrays.asList("G6", "H6", "A4");
            List<String> location109 = Arrays.asList("H1", "H2", "H3");
            List<String> location110 = Arrays.asList("A2", "A3", "D8");
            List<String> location111 = Arrays.asList("E1", "F2", "G3");
            //game4
            List<String> location112 = Arrays.asList("A3", "A4", "F7");
            List<String> location113 = Arrays.asList("B5", "B6", "H1");
            List<String> location114 = Arrays.asList("A2", "G6", "H6");
            List<String> location115 = Arrays.asList("C5", "C7", "D5");
            //game5
            List<String> location116 = Arrays.asList("A1", "A2", "A3");
            List<String> location117 = Arrays.asList("B5", "B6", "C7");
            List<String> location118 = Arrays.asList("G6", "G7", "G8");
            List<String> location119 = Arrays.asList("C6", "D6", "E6");
            List<String> location120 = Arrays.asList();
            List<String> location121 = Arrays.asList("H1", "H8");

            gamePlayer1.addSalvo(new Salvo(1, location100));
            gamePlayer2.addSalvo(new Salvo(1, location101));
            gamePlayer1.addSalvo(new Salvo(2, location102));
            gamePlayer2.addSalvo(new Salvo(2, location103));
            gamePlayer3.addSalvo(new Salvo(1, location104));
            gamePlayer4.addSalvo(new Salvo(1, location105));
            gamePlayer3.addSalvo(new Salvo(2, location106));
            gamePlayer4.addSalvo(new Salvo(2, location107));
            gamePlayer5.addSalvo(new Salvo(1, location108));
            gamePlayer6.addSalvo(new Salvo(1, location109));
            gamePlayer5.addSalvo(new Salvo(2, location110));
            gamePlayer6.addSalvo(new Salvo(2, location111));
            gamePlayer7.addSalvo(new Salvo(1, location112));
            gamePlayer8.addSalvo(new Salvo(1, location113));
            gamePlayer7.addSalvo(new Salvo(2, location114));
            gamePlayer8.addSalvo(new Salvo(2, location115));
            gamePlayer9.addSalvo(new Salvo(1, location116));
            gamePlayer10.addSalvo(new Salvo(1, location117));
            gamePlayer9.addSalvo(new Salvo(2, location118));
            gamePlayer10.addSalvo(new Salvo(2, location119));
            gamePlayer10.addSalvo(new Salvo(3, location121));
//e agora....          salvoRepository.saveAll(Arrays.asList(salvo1, salvo2, salvo3, salvo4, salvo5, salvo6, salvo7, salvo8, salvo9, salvo10, salvo11, salvo12, salvo13, salvo14, salvo15, salvo16, salvo17, salvo18, salvo19, salvo20, salvo21));

            //save gamePlayerRepository sólo luego de que se le agrega la información addSalvoes
            gamePlayerRepository.saveAll(Arrays.asList(gamePlayer1, gamePlayer2, gamePlayer3, gamePlayer4, gamePlayer5, gamePlayer6, gamePlayer7, gamePlayer8, gamePlayer9, gamePlayer10, gamePlayer11, gamePlayer12, gamePlayer13, gamePlayer14));

            Score score1 = new Score(1, game1, player1, LocalDateTime.now().plusHours(1));
            Score score2 = new Score(0, game1, player2, LocalDateTime.now().plusHours(1));
            Score score3 = new Score(0.5, game2, player1, LocalDateTime.now().plusHours(2));
            Score score4 = new Score(0.5, game2, player2, LocalDateTime.now().plusHours(2));
            Score score5 = new Score(1, game3, player2, LocalDateTime.now().plusHours(3));
            Score score6 = new Score(0, game3, player4, LocalDateTime.now().plusHours(3));
            Score score7 = new Score(0.5, game4, player2, LocalDateTime.now().plusHours(4));
            Score score8 = new Score(0.5, game4, player1, LocalDateTime.now().plusHours(4));
            Score score9 = new Score(0.5, game5, player4, LocalDateTime.now().plusHours(5));
            Score score10 = new Score(0.5, game5, player1, LocalDateTime.now().plusHours(5));
            scoreRepository.saveAll(Arrays.asList(score1, score2, score3, score4, score5, score6, score7, score8, score9, score10));
        };
    }
}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {
    @Autowired
    PlayerRepository playerRepository;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inputName -> {
            Player player = playerRepository.findByUsername(inputName);
            if (player != null) {
                return new User(player.getUsername(), player.getPassword(),
                        AuthorityUtils.createAuthorityList("USER"));
            } else {
                throw new UsernameNotFoundException("Unknown user: " + inputName);
            }
        });
    }
}

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/**", "/rest/**", "/web/games.html", "/web/js/*", "/api/games/**", "/api/games/players/**", "/api/players", "/api/leaderboard", "/web/css/*", "/login", "/logout").permitAll()
                .anyRequest().authenticated()
                .antMatchers("/web/*", "web/game.html?gp=1","/api/game_view/**").hasAuthority("USER");

//		http.formLogin().loginPage("/login");
        http.formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
                .loginPage("/login");

        http.logout().logoutUrl("/logout");

        // turn off checking for CSRF tokens
        http.csrf().disable();
        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));
        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
