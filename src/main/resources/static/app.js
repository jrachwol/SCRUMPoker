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

function sendVote() {
    stompClient.publish({
        destination: "/app/hello",
        body: JSON.stringify({'name': $("#name").val()})
    });
}

function showVote(message) {
    console.log(message);
    $("#votes").append("<tr><td>" + message + "</td></tr>");
    // $("#votes").html("<tr><td>" + message + "</td></tr>");
}

function sendNumber() {
    let radios = document.getElementsByName('card');

    for (let i = 0; i < radios.length; i++) {
        if (radios[i].checked) {
            console.log('radios[i].value', radios[i].value);
            stompClient.publish({
                destination: "/app/hello",
                // body: radios[i].value.toString()
                body: radios[i].value.toString()
            });
            break;
        }
    }
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#send" ).click(() => sendVote());
    $( "#send-btn" ).click(() => sendNumber());
});
