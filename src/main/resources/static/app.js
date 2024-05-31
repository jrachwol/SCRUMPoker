const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:9090/votes-websocket'
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/votes', (vote) => {
        console.log("Received payload:", vote);
        showVote(vote.body);
    });
    stompClient.subscribe('/topic/votings', (voting) => {
        console.log("Received payload:", voting);
        showVotes(voting.body);
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

// function sendVote() {
//     stompClient.publish({
//         destination: "/app/hello",
//         body: JSON.stringify({'name': $("#name").val()})
//     });
// }

function showVote(message) {
    // $("#vote_" + voter).html("<tr><td>" + message + "</td></tr>");
    if (window.room == JSON.parse(message).room)
        $("#vote_" + JSON.parse(message).voter).html("<tr><td>" + JSON.parse(message).content + "</td></tr>");
}

function showVotes(message) {
    console.log('Received string:', message);

    $('#voting').empty();

    let messageArray;

    try {
        messageArray = JSON.parse(message);
    } catch(e) {
        console.error('Error parsing message string into array:', e);
        return;
    }

    console.log('Parsed message array:', messageArray);

    if (!Array.isArray(messageArray)) {
        console.error('Error: Parsed message is not an array');
        return;
    }

    for (let i = 0; i < messageArray.length; i++) {
        var voteJSON;

        try {
            voteJSON = JSON.parse(messageArray[i]);
        } catch(e) {
            console.error('Error parsing array item into JSON:', e);
            continue;
        }

        console.log('Current voteJSON:', voteJSON);

        let deleteURL = '/deleteVoterws/' + voteJSON.voterId + '/'+ window.roomCode;
        // var voteHtml = "<tr><td id='vote_" + voteJSON.voter + "'>" + voteJSON.voter + "</td><td>" + voteJSON.vote + "</td></tr><a href='" + deleteURL + "'>Usuń</a></td></tr>";
        var voteHtml = "<tr><td id='vote_" + voteJSON.voter + "'>" + voteJSON.voter + "</td><td>" + voteJSON.vote + "</td><td><a href='" + deleteURL + "'>Usuń</a></td></tr>";
        $('#voting').append(voteHtml);
    }
}

function sendVote() {
    let radios = document.getElementsByName('card')

    for (let i = 0; i < radios.length; i++) {
        if (radios[i].checked) {
            console.log('radios[i].value', radios[i].value);
            console.log('radios[i].text', radios[i].nextSibling.textContent);
            console.log('radios[i].title', radios[i].title);
            console.log('voter: ', window.voter);
            console.log('[i]', i);
            stompClient.publish({
                destination: "/app/hello",
                body: JSON.stringify({
                    content: radios[i].nextSibling.textContent,
                    voter: window.voter,
                    room: window.room
                })
            });
            break;
        }
    }
}

$(function () {
    // $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    // $( "#send" ).click(() => sendVote());
    $( "#send-btn" ).click(() => sendVote());
});

$(document).ready(function() {
    connect();
});

$(window).on('unload', function() {
    disconnect();
});