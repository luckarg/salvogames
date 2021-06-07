Vue.filter('formatDate', function(value) {
  if (value) {
    return moment(String(value)).format('DD/MM/YYYY hh:mm:ss')
  }
})

function initialize(){
fetch("/api/games")
        .then(res => res.json())
        .then(json => {
            app.games= json.games;
            app.playerLogged=json.player
    })
    fetch("/api/leaderboard")
        .then(res => res.json())
        .then(json => {
            app.player = json;
    })
}

var app = new Vue({
    el: "#app",
    data: {
        games: [],
        player: [],
        playerLogged:""
    },
     computed: {
       orderedUsers: function () {
         return _.orderBy(this.player, 'Total', 'desc')
       }
     },
    created(){
        initialize()
    },
    methods: {


            joinGame(gameId){
            			fetch('/api/games/' + gameId + '/players', {
            				method: 'POST'
            			})
            			.then(res => {
            				if(res.ok){
            					return res.json()
            				}else{
            					return Promise.reject(res.json())
            				}
            			})
            			.then(json => {
            				console.log(json)
            				location.href = "/web/game.html?gp=" + json.gamePlayerId
            			})
            			.catch(error => error)
            			.then(error => console.log(error))
            		},


            enterGame: function (gamePlayerId) {
                        location.href = "/web/game.html?gp=" + gamePlayerId
                    }
         }
});


function login() {
  $.post("/login", {
    username: document.getElementById("username").value,
    password: document.getElementById("password").value
  })
    .done(function() {
      console.log("Logged in!");
      window.location.reload();
    })
    .fail(function() {
      alert("wrong user or password");
    });
}

function signup() {
    event.preventDefault();
    $.post("/api/players", {
            username: $("#username").val(),
            password: $("#password").val()
        })
        .done(function () {
            alert("user created!");
            login();
        })
        .fail(function () {
            console.log("Failed to login");
            alert("user already in use, try again")
        });
}

function create() {
    event.preventDefault();
    $.post("/api/games")
     .done(
      function(data){
      console.log(data);
        alert("Game created!")
        window.location.replace('game.html?gp='+data.gamePlayerId)
      }
     )
     .fail(
      function(){
        alert("cannot create game");
      }
     );
}



function logout() {
    event.preventDefault();
    $.post("/logout")
     .done(
      function(){
        alert("you have logged out");
      window.location.reload();
      }
     )
     .fail(
      function(){
        alert('Logout failed');
      }
     );
}
