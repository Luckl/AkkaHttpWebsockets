class Tile {

    x: number;
    y: number;
    url: string;
    movable: number;
    canvasImage: any;

    constructor(x: number, y: number, url: string, movable: number) {
        this.x = x;
        this.y = y;
        this.url = url;
        this.movable = movable;
    }

    getX() {
        return this.x;
    }

    getY() {
        return this.y;
    }

    getUrl() {
        return this.url;
    }

    setUrl(url: string) {
        this.url = url;
    }

    setCanvasImage(image: any) {
        this.canvasImage = image;
    }

    getCanvasImage() {
        return this.canvasImage;
    }


}