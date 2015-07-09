/// <reference path="Tile.ts" />
/// <reference path="World.ts" />
class Player {

    canvas:any;
    location:Tile;
    world:World;
    image:any;

    stepsTaken:number = 0;

    constructor(canvas:any, location:Tile, world:World) {
        this.canvas = canvas;
        this.location = this.checkLocation(world, location);
        this.world = world;

        this.image = this.canvas.display.sprite({
            x: this.location.x,
            y: this.location.y,
            origin: {x: 'top', y: 'top'},
            image: "img/spritesheet.png",
            generate: true,
            width: world.tileWidth,
            height: world.tileHeight,
            direction: 'x',
            frame: 3
        });

        this.canvas.addChild(this.image);
    }

    private checkLocation(world:World, location:Tile) {
        if (world.getTiles()[location.x][location.y].movable != 1) {
            location.x = location.x + 1;

            return this.checkLocation(world, location);
        } else {
            return location;
        }
    }

    walkTo(cell:any) {
        var path = this.world.findPath([this.location.x, this.location.y], cell);

        if (path.length > 0) {
            console.log(path);
            this.walk(path);
        }
    }

    private walk(path) {

        var direction = path.shift();

        var oldLocation = this.location;
        this.location = this.world.getTileFromCell(direction);

        var currentX = oldLocation.x * this.world.tileWidth;
        var currentY = oldLocation.y * this.world.tileHeight;

        var newX = direction[0] * this.world.tileWidth;
        var newY = direction[1] * this.world.tileHeight;

        var duration = 100;
        if (newX != currentX && newY != currentY) {
            duration = 141;
        }

        this.image.animate({
                x: newX,
                y: newY
            }, {
                duration: duration * 2,
                easing: "linear",
                queue: "Player"
            }
        );
        if(path.length > 0)
            this.walk(path);
    }
}