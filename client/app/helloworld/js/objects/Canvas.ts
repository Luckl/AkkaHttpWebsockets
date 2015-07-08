/// <reference path="../services/Connector.ts" />
/// <reference path="../services/SocketConnector.ts" />
/// <reference path="World.ts" />
/// <reference path="Tile.ts" />
/// <reference path="Player.ts" />
class Canvas {

    canvas:any;
    world:World;
    player:Player;

    image:any;

    constructor(url:string) {
        this.canvas = oCanvas.create({
            canvas: "#game",
            fps: 60
        });

        this.world = new World(this.canvas.width, this.canvas.height);
        this.image = new Image();
        this.image.src = "img/spritesheet.png";

        this.canvas.world = this.world;

        this.render();

        this.player = new Player(this.canvas, this.world.getTiles()[0][0], this.world);
        this.canvas.player = this.player;

        this.canvas.bind("click tap", function (ev) {

            var x = this.mouse.x;
            var y = this.mouse.y;

            var target = [Math.floor(x / 32), Math.floor(y / 32)];

            this.player.image.stop();

            var currentLocation = [
                Math.floor(this.player.image.abs_x / this.world.tileWidth),
                Math.floor(this.player.image.abs_y / this.world.tileHeight)
            ];

            this.player.location = this.world.getTileFromCell(currentLocation);

            this.player.walkTo(target);
        });
    }

    render() {
        var tiles = this.world.getTiles();
        for (var x = 0; x < tiles.length; x++) {

            for (var y = 0; y < tiles[x].length; y++) {

                var t:Tile = tiles[x][y];

                var image = this.canvas.display.sprite({
                    x: t.x * this.world.tileWidth,
                    y: t.y * this.world.tileHeight,
                    origin: {x: 'top', y: 'top'},
                    image: this.image,
                    generate: true,
                    width: this.world.tileWidth,
                    height: this.world.tileHeight,
                    direction: 'x',
                    frame: t.movable
                });

                t.setCanvasImage(image);
                this.canvas.addChild(image);
            }
        }
    }
}