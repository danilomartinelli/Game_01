package digital.smartbit.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import digital.smartbit.main.Game;
import digital.smartbit.world.Camera;
import digital.smartbit.world.World;

public class Player extends Entity {
	public boolean right, up, left, down;
	public int right_dir = 0, left_dir = 1, up_dir = 2, down_dir = 3;
	public int dir = right_dir;
	public int speed = 2;
	private boolean moved = false;

	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;

	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] upPlayer;
	private BufferedImage[] downPlayer;

	private BufferedImage rightPlayerDamage;
	private BufferedImage leftPlayerDamage;
	private BufferedImage upPlayerDamage;
	private BufferedImage downPlayerDamage;

	public int ammo = 0;

	public boolean isDamaged = false;
	
	private int damageFrames = 0;

	public static double life = 100;

	public static final double MAX_LIFE = 100;

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);

		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		upPlayer = new BufferedImage[4];
		downPlayer = new BufferedImage[4];

		rightPlayerDamage = Game.spritesheet.getSprite(0, 16, 16, 16);
		leftPlayerDamage = Game.spritesheet.getSprite(0, 32, 16, 16);
		upPlayerDamage = Game.spritesheet.getSprite(16, 32, 16, 16);
		downPlayerDamage = Game.spritesheet.getSprite(16, 16, 16, 16);

		for (int i = 0; i < 4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 0, 16, 16);
		}

		for (int i = 0; i < 4; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 16, 16, 16);
		}

		for (int i = 0; i < 4; i++) {
			upPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 32, 16, 16);
		}

		for (int i = 0; i < 4; i++) {
			downPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 48, 16, 16);
		}
	}

	public void tick() {
		moved = false;

		if (right && World.isFree(getX() + speed, getY())) {
			moved = true;
			dir = right_dir;
			setX(getX() + speed);
		} else if (left && World.isFree(getX() - speed, getY())) {
			moved = true;
			dir = left_dir;
			setX(getX() - speed);
		}

		if (up && World.isFree(getX(), getY() - speed)) {
			moved = true;
			dir = up_dir;
			setY(getY() - speed);
		} else if (down && World.isFree(getX(), getY() + speed)) {
			moved = true;
			dir = down_dir;
			setY(getY() + speed);
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

		checkItems();
		
		if (isDamaged) {
			this.damageFrames++;
			
			if (this.damageFrames == 8) {
				this.damageFrames = 0;
				isDamaged = false;
			}
		}
		
		if (life <= 0) {
			System.exit(1);
		}

		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, World.WIDTH * 16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, World.HEIGHT * 16 - Game.HEIGHT);
	}

	public void checkItems() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);

			if (e instanceof LifePack) {
				if (Entity.isColliding(this, e)) {
					life += 10;

					if (life > 100)
						life = 100;

					Game.entities.remove(i);

					return;
				}
			}

			if (e instanceof Bullet) {
				if (Entity.isColliding(this, e)) {
					ammo += 10;

					Game.entities.remove(i);

					return;
				}
			}
		}
	}

	public void render(Graphics g) {
		if (!isDamaged) {
			if (dir == up_dir) {
				g.drawImage(upPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			} else if (dir == down_dir) {
				g.drawImage(downPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			} else if (dir == right_dir) {
				g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			} else if (dir == left_dir) {
				g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
		} else {
			if (dir == up_dir) {
				g.drawImage(upPlayerDamage, this.getX() - Camera.x, this.getY() - Camera.y, null);
			} else if (dir == down_dir) {
				g.drawImage(downPlayerDamage, this.getX() - Camera.x, this.getY() - Camera.y, null);
			} else if (dir == right_dir) {
				g.drawImage(rightPlayerDamage, this.getX() - Camera.x, this.getY() - Camera.y, null);
			} else if (dir == left_dir) {
				g.drawImage(leftPlayerDamage, this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
		}
	}
}
