import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.*;


public class Tank {
	
	private static final int X_SPEED = 5;
	private static final int Y_SPEED = 5;
	
	private boolean live = true;
	public int health = 5;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] tankimgs = null;
	private static Map<String, Image> imgs = new HashMap<String, Image>();
	
	public static final int HEIGHT = 50;
	public static final int WIDTH = 50;
	
	static {
		tankimgs = new Image[] {
				tk.getImage(Tank.class.getClassLoader().getResource("images/L.png")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/R.png")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/U.png")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/D.png")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/LU.png")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/LD.png")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/RU.png")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/RD.png"))
		};
		
		imgs.put("L", tankimgs[0]);
		imgs.put("R", tankimgs[1]);
		imgs.put("U", tankimgs[2]);
		imgs.put("D", tankimgs[3]);
		imgs.put("LU", tankimgs[4]);
		imgs.put("LD", tankimgs[5]);
		imgs.put("RU", tankimgs[6]);
		imgs.put("RD", tankimgs[7]);
	}

// Direction 
	private Dir d = Dir.STOP;
	private static Random r = new Random();
	private Boolean dL = false, dR = false, dU = false, dD = false;
	private int moveCounter = r.nextInt(13) + 1;
	private int fireCounter = r.nextInt(78) + 1;
	
// Cannon's dir	
	private Dir caDir = Dir.D;
	
//Position
	public int x, y;
	public int oldX, oldY;
	
	
	public boolean good;
	TankClient tc;
	
	Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;
		this.good = good;
	}
	
	Tank(int x, int y, boolean good, TankClient tc, Dir d) {
		this(x, y, good);
		this.d = d;
		this.tc = tc;
	}
		
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
	
	public void draw(Graphics g) {
		if(!live) {
			if(!good) {
				tc.enemyTanks.remove(this);
			}
			return;
		}
		if(good) {
			new BloodBar().draw(g);
		}
		move(d);

//Draw tank
		switch(caDir) {
		case L:
			g.drawImage(imgs.get("L"), x, y, null);
			break;
		case LU:
			g.drawImage(imgs.get("LU"), x, y, null);
			break;
		case U:
			g.drawImage(imgs.get("U"), x, y, null);
			break;
		case RU:
			g.drawImage(imgs.get("RU"), x, y, null);
			break;
		case R:
			g.drawImage(imgs.get("R"), x, y, null);
			break;
		case RD:
			g.drawImage(imgs.get("RD"), x, y, null);
			break;
		case D:
			g.drawImage(imgs.get("D"), x, y, null);
			break;
		case LD:
			g.drawImage(imgs.get("LD"), x, y, null);
			break;
		case STOP:
			break;
		}	
	}
	
	public void move(Dir d) {
		this.oldX = x;
		this.oldY = y;
		switch(d) {
		case U:
			if(y > HEIGHT) {
				y -= Y_SPEED;
			}
			break;
		case D:
			if(y < TankClient.WINDOW_HEIGHT - HEIGHT) {
				y += Y_SPEED;
			}
			break;
		case L:
			if(x > 0) {
				x -= X_SPEED;
			}
			break;
		case R:
			if(x < TankClient.WINDOW_WIDTH - WIDTH) {
				x += X_SPEED;
			}
			break;
		case LU:
			if(x > 0 && y > HEIGHT) {
				x -= X_SPEED;
				y -= Y_SPEED;
			}
			break;
		case LD:
			if(x > 0 && y < TankClient.WINDOW_HEIGHT - HEIGHT) {
				x -= X_SPEED;
				y += Y_SPEED;
			}
			break;
		case RU:
			if(x < TankClient.WINDOW_WIDTH - WIDTH && y > HEIGHT) {
				x += X_SPEED;
				y -= Y_SPEED;
			}
			break;
		case RD:
			if(x < TankClient.WINDOW_WIDTH -WIDTH && y < TankClient.WINDOW_HEIGHT - HEIGHT) {
				x += X_SPEED;
				y += Y_SPEED;
			}
			break;
		case STOP:
			break;
		}
		
		if(this.d != Dir.STOP) {
			caDir = this.d;
		}
		
		if(!good) {
			Dir[] dirs = Dir.values();
			
			this.moveCounter += 1;
			this.fireCounter += 1;
			
			if(this.moveCounter == 15) {
				int randomNum = r.nextInt(dirs.length);
				this.d = dirs[randomNum];
				this.moveCounter = r.nextInt(13) + 1;
			}
			if(this.fireCounter >= 80) {
				tc.bullets.add(fire());
				this.fireCounter = r.nextInt(78) + 1;
			}
		}
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_F2:
			if(!this.live) {
				this.live = true;
				this.health = 5;
				dL = false;
				dR = false;
				dU = false;
				dD = false;
				tc.score = 0;
			}
			break;
		case KeyEvent.VK_LEFT:
			dL = true;
			break;
		case KeyEvent.VK_RIGHT:
			dR = true;
			break;
		case KeyEvent.VK_UP:
			dU = true;
			break;
		case KeyEvent.VK_DOWN:
			dD = true;
			break;
		}
		setDirection();
	}
	
	public void keyReleased(KeyEvent e) {
		if(!live) {
			return;
		}
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_F:
			tc.bullets.add(fire());
			break;
		case KeyEvent.VK_A:
			superFire();
			break;
		case KeyEvent.VK_B:
			bomb();
			break;
		case KeyEvent.VK_LEFT:
			dL = false;
			break;
		case KeyEvent.VK_RIGHT:
			dR = false;
			break;
		case KeyEvent.VK_UP:
			dU = false;
			break;
		case KeyEvent.VK_DOWN:
			dD = false;
			break;
		}
		setDirection();
	}

// Fire
	public Bullet fire() {
		Bullet b = new Bullet(x + WIDTH/2, y + HEIGHT/2, caDir, this.good, tc);
		return b;
	}
	
	public Bullet fire(Dir d) {
		Bullet b = new Bullet(x + WIDTH/2, y + HEIGHT/2, d, this.good, tc);
		return b;
	}
	
	public void superFire() {
		Dir[] dirs = Dir.values();
		for(int i=0;i<8;i++) {
			tc.bullets.add(fire(dirs[i]));
		}
	}
	
	public void bomb() {
		for(int i=0;i<100;i++) {
			Bullet b = new Bullet(x + WIDTH/2, y + HEIGHT/2, caDir, 25, 25, this.good, tc);
			tc.bullets.add(b);
		}
	}
	
	void setDirection() {
		if(dL && !dR && !dU && !dD) {
			d = Dir.L;
		} else if(!dL && dR && !dU && !dD) {
			d = Dir.R;
		} else if(!dL && !dR && dU && !dD) {
			d = Dir.U;
		} else if(!dL && !dR && !dU && dD) {
			d = Dir.D;
		} else if(dL && !dR && dU && !dD) {
			d = Dir.LU;
		} else if(dL && !dR && !dU && dD) {
			d = Dir.LD;
		} else if(!dL && dR && dU && !dD) {
			d = Dir.RU;
		} else if(!dL && dR && !dU && dD) {
			d = Dir.RD;
		} else if(!dL && !dR && !dU && !dD) {
			d = Dir.STOP;
		}
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	public boolean meetWall(Wall w) {
		if(this.live && this.getRect().intersects(w.getRect())) {
			x = oldX;
			y = oldY;
			return true;
		}
		return false;
	}
	
	public boolean meetWalls(List<Wall> walls) {
		for(int i=0; i<walls.size();i++) {
			if(meetWall(walls.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	public boolean meetTank(Tank t) {
		if(this.live && t.live && this.getRect().intersects(t.getRect())) {
			this.x = oldX;
			this.y = oldY;
			t.x = t.oldX;
			t.y = t.oldY;
			return true;
		}
		return false;
	}
	
	public boolean meetTanks(java.util.List<Tank> tanks) {
		for(int i=0;i<tanks.size();i++) {
			Tank t = tanks.get(i);
			if(t != this && meetTank(t)) {
				return true;
			}
		}
		return false;
	}
	
	private class BloodBar {
		public void draw(Graphics g) {
			Color c = g.getColor();
			g.setColor(Color.red);
			g.drawRect(x + 1, y - 10, WIDTH, 10);
			g.fillRect(x + 1, y - 10, WIDTH/5 * health, 10);
			g.setColor(c);
		}
	}

//Rehealth
	public void getBlood() {
		if(live && tc.blood.live && this.health < 5 && this.getRect().intersects(tc.blood.getRect())) {
			this.health += 1;
			tc.blood.live = false;
		}
	}


}
