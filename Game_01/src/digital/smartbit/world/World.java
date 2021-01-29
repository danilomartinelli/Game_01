package digital.smartbit.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import digital.smartbit.entities.Bullet;
import digital.smartbit.entities.Enemy;
import digital.smartbit.entities.Entity;
import digital.smartbit.entities.LifePack;
import digital.smartbit.entities.Weapon;
import digital.smartbit.main.Game;

public class World {
	private static Tile[] tiles;

	public static int WIDTH;
	public static int HEIGHT;
	public static final int TILE_SIZE = 16;

	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));

			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();

			int[] pixels = new int[WIDTH * HEIGHT];

			tiles = new Tile[WIDTH * HEIGHT];

			map.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);

			for (int x = 0; x < WIDTH; x++) {
				for (int y = 0; y < HEIGHT; y++) {
					int currentPixel = pixels[x + (y * WIDTH)];

					tiles[x + (y * WIDTH)] = new FloorTile(x * 16, y * 16, Tile.TILE_FLOOR);

					if (currentPixel == 0xFF000000) { // Black
						// Floor
						tiles[x + (y * WIDTH)] = new FloorTile(x * 16, y * 16, Tile.TILE_FLOOR);
					} else if (currentPixel == 0xFFFFFFFF) { // White
						// Wall
						tiles[x + (y * WIDTH)] = new WallTile(x * 16, y * 16, Tile.TILE_WALL);
					} else if (currentPixel == 0xFF0026FF) { // Blue
						// Player
						Game.player.setX(x * 16);
						Game.player.setY(y * 16);
					} else if (currentPixel == 0xFFFF0000) { // Red
						// Enemy
						Enemy en = new Enemy(x * 16, y * 16, 16, 16, Entity.ENEMY_EN);
						
						Game.entities.add(en);
						Game.enemies.add(en);
					} else if (currentPixel == 0xFFFF6A00) { // Orange
						// Weapon
						Game.entities.add(new Weapon(x * 16, y * 16, 16, 16, Entity.WEAPON_EN));
					} else if (currentPixel == 0xFFFFB27F) { // Beige
						// Life pack
						Game.entities.add(new LifePack(x * 16, y * 16, 16, 16, Entity.LIFEPACK_EN));
					} else if (currentPixel == 0xFFFFD800) { // Yellow
						// Bullet
						Game.entities.add(new Bullet(x * 16, y * 16, 16, 16, Entity.BULLET_EN));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isFree(int nextX, int nextY) {
		int x1 = nextX / TILE_SIZE;
		int y1 = nextY / TILE_SIZE;

		int x2 = (nextX + TILE_SIZE - 1) / TILE_SIZE;
		int y2 = nextY / TILE_SIZE;

		int x3 = nextX / TILE_SIZE;
		int y3 = (nextY + TILE_SIZE - 1) / TILE_SIZE;

		int x4 = (nextX + TILE_SIZE - 1) / TILE_SIZE;
		int y4 = (nextY + TILE_SIZE - 1) / TILE_SIZE;

		return !(tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile
				|| tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile
				|| tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile
				|| tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile);
	}

	public void render(Graphics g) {
		int xStart = Camera.x >> 4;
		int yStart = Camera.y >> 4;

		int xFinal = xStart + (Game.WIDTH >> 4);
		int yFInal = yStart + (Game.HEIGHT >> 4);

		for (int x = xStart; x <= xFinal; x++) {
			for (int y = yStart; y <= yFInal; y++) {
				if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) {
					continue;
				}
				Tile tile = tiles[x + (y * WIDTH)];
				tile.render(g);
			}
		}
	}
}
