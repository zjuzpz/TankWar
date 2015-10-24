import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;


public class Bullet {
	private static final int HEIGHT = 10;
	private static final int WIDTH = 10;
	
	private static final int X_SPEED = 10;
	private static final int Y_SPEED = 10;
	
	private TankClient tc;
// Direction 	
	private Dir d = Dir.STOP;
	
//Position
	private int x, y;
	private int height = HEIGHT;
	private int width = WIDTH;
	private boolean good;
	private boolean live = true;
	
	public boolean isLive() {
		return live;
	}


	Bullet(int x, int y, Dir d) {
		this.x = x - width/2;
		this.y = y - height/2;
		this.d = d;
	}
	
	Bullet(int x, int y, Dir d, boolean good, TankClient tc) {
		this(x, y, d);
		this.good = good;
		this.tc = tc;
	}
	
	Bullet(int x, int y, Dir d, int width, int height, boolean good, TankClient tc) {
		this.x = x - width/2;
		this.y = y - height/2;
		this.d = d;
		this.height = height;
		this.width = width;
		this.good = good;
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.black);
		if(good) {
			g.setColor(Color.blue);
		}
		g.fillOval(x,y,width,height);
		g.setColor(c);
		
		move(d);
	}
	
	public void move(Dir d) {
		switch(d) {
		case U:
			y -= Y_SPEED;
			break;
		case D:
			y += Y_SPEED;
			break;
		case L:
			x -= X_SPEED;
			break;
		case R:
			x += X_SPEED;
			break;
		case LU:
			x -= X_SPEED;
			y -= Y_SPEED;
			break;
		case LD:
			x -= X_SPEED;
			y += Y_SPEED;
			break;
		case RU:
			x += X_SPEED;
			y -= Y_SPEED;
			break;
		case RD:
			x += X_SPEED;
			y += Y_SPEED;
			break;
		case STOP:
			break;
		}
		
		if(x<0 || y<0 || x>TankClient.WINDOW_WIDTH || y>TankClient.WINDOW_HEIGHT) {
			live = false;
		}
		
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, width, height);
	}
	
	public boolean hit(Wall w) {
		if(this.live && this.getRect().intersects(w.getRect())) {
			live = false;
			return true;
		}
		return false;
	}

//Collision detection	
	
	public boolean hit(Tank t) {
		if(this.live && this.good != t.good && t.isLive() && this.getRect().intersects(t.getRect())) {
			if(t.good) {
				t.health -= 1;
				if(t.health <= 0) {
					t.setLive(false);
					tc.explodes.add(new Explode(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, tc));
				}
			} else {
				t.setLive(false);
				tc.explodes.add(new Explode(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, tc));
			}
			live = false;
			return true;
		}
		return false;
	}
	
	public boolean hitWalls(List<Wall> walls) {
		for(int i=0; i<walls.size();i++) {
			if(hit(walls.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hitTanks(List<Tank> tanks) {
		for(int i=0; i<tanks.size(); i++) {
			if(hit(tanks.get(i))) {
				return true;
			}
		}
		return false;
	}

}