class SocketConnector {

    socketConnection: WebSocket;

    constructor(connectionString: string) {
        this.socketConnection = new WebSocket(connectionString);
        console.log("Created connection to " + connectionString);
        this.socketConnection.onmessage = function(event) {
            console.log(event.data)
        }
    }

    send(message: string) {
        this.socketConnection.send(message)
    }
}

var socketConnector = new SocketConnector("ws://localhost:8888/helloworld/");