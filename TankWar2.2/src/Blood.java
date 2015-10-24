import java.awt.*;
import java.util.*;
import java.util.List;

// The blood will rehealth you
public class Blood {
	public boolean live = true;
	private int x, y, width, height;
	private int step = 0;
	TankClient tc;
	
	private static Random r_x = new Random();
	private static Random r_y = new Random();
	
	public Blood() {
		x = 300;
		y = 400;
		width = 10;
		height = 10;
	}
	
	public Blood(TankClient tc) {
		this();
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		if(!live){
			step ++;
			if(step >= 200) {
				move();
				live = true;
				step = 0;
			}
			return;
		}
		Color c = g.getColor();
		g.setColor(Color.orange);
		g.fillRect(x, y, width, height);
		g.setColor(c);
		step ++;
		//25 -> 1s;  100 -> 4s;
		if(step >= 150) {
			move();
			step = 0;
		}	
	}
	private void move() {
		x = r_x.nextInt(TankClient.WINDOW_WIDTH - 100);
		y = r_y.nextInt(TankClient.WINDOW_HEIGHT - 100);
		while(hitWalls(tc.walls)) {
			x = r_x.nextInt(TankClient.WINDOW_WIDTH - 100);
			y = r_y.nextInt(TankClient.WINDOW_HEIGHT - 100);
		}
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, width, height);
	}
	
	public boolean hit(Wall w) {
		if(this.getRect().intersects(w.getRect())) {
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

}
