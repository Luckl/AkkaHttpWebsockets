/// <reference path="services/Connector.ts" />
/// <reference path="services/SocketConnector.ts" />
/// <reference path="jquery.d.ts" />
class Canvas {

    connection: Connector;
    canvas: any;

    constructor(url: string) {
        this.connection = new SocketConnector(url);
        this.canvas = $('#game');
        console.log(this.canvas)
    }
}