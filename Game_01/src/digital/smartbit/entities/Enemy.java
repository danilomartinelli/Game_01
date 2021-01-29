package digital.smartbit.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import digital.smartbit.main.Game;
import digital.smartbit.world.Camera;
import digital.smartbit.world.World;

public class Enemy extends Entity {
	public int right_dir = 0, left_dir = 1, up_dir = 2, down_dir = 3;
	public int dir = right_dir;
	public int speed = 1;
	private boolean moved = false;

	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;

	private BufferedImage[] rightEnemy;
	private BufferedImage[] leftEnemy;
	private BufferedImage[] upEnemy;
	private BufferedImage[] downEnemy;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);

		rightEnemy = new BufferedImage[4];
		leftEnemy = new BufferedImage[4];
		upEnemy = new BufferedImage[4];
		downEnemy = new BufferedImage[4];

		for (int i = 0; i < 4; i++) {
			rightEnemy[i] = Game.spritesheet.getSprite(32 + (i * 16), 64, 16, 16);
		}

		for (int i = 0; i < 4; i++) {
			leftEnemy[i] = Game.spritesheet.getSprite(32 + (i * 16), 80, 16, 16);
		}

		for (int i = 0; i < 4; i++) {
			upEnemy[i] = Game.spritesheet.getSprite(32 + (i * 16), 96, 16, 16);
		}

		for (int i = 0; i < 4; i++) {
			downEnemy[i] = Game.spritesheet.getSprite(32 + (i * 16), 112, 16, 16);
		}
	}

	public void tick() {
		moved = false;

		if (!this.isCollidingWithPlayer()) {
			if (getX() < Game.player.getX() && World.isFree(getX() + speed, getY())
					&& !isColliding(getX() + speed, getY())) {
				moved = true;
				dir = right_dir;
				setX(getX() + speed);
			} else if (getX() > Game.player.getX() && World.isFree(getX() - speed, getY())
					&& !isColliding(getX() - speed, getY())) {
				moved = true;
				dir = left_dir;
				setX(getX() - speed);
			}

			if (getY() > Game.player.getY() && World.isFree(getX(), getY() - speed)
					&& !isColliding(getX(), getY() - speed)) {
				moved = true;
				dir = up_dir;
				setY(getY() - speed);
			} else if (getY() < Game.player.getY() && World.isFree(getX(), getY() + speed)
					&& !isColliding(getX(), getY() + speed)) {
				moved = true;
				dir = down_dir;
				setY(getY() + speed);
			}
		} else {
			if (Game.rand.nextInt(100) < 10) {
				Player.life -= Game.rand.nextInt(5);
				Game.player.isDamaged = true;
			}

		}

		if (moved) {
			frames++;
			if (frames == maxFrames) {
				frames = 0;
				index++;
				if (index > maxIndex) {
					index = 0;
				}
			}
		}
	}

	public boolean isCollidingWithPlayer() {
		Rectangle enemy = new Rectangle(this.getX(), this.getY(), World.TILE_SIZE, World.TILE_SIZE);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);

		return enemy.intersects(player);
	}

	public boolean isColliding(int xNext, int yNext) {
		Rectangle enemyCurrent = new Rectangle(xNext, yNext, World.TILE_SIZE, World.TILE_SIZE);

		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);

			if (e == this)
				continue;

			Rectangle targetEnemy = new Rectangle(e.getX(), e.getY(), World.TILE_SIZE, World.TILE_SIZE);

			if (enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
		}

		return false;
	}

	public void render(Graphics g) {
		if (dir == up_dir) {
			g.drawImage(upEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else if (dir == down_dir) {
			g.drawImage(downEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else if (dir == right_dir) {
			g.drawImage(rightEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else if (dir == left_dir) {
			g.drawImage(leftEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
	}
}
