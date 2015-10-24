import java.awt.*;


public class Wall {
	TankClient tc;
// Position
	int x, y;
	int width, height;
	
	Wall(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	Wall(int x, int y, int width, int height, TankClient tc) {
		this(x, y, width, height);
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.black);
		g.fillRect(x, y, width, height);
		g.setColor(c);
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, width, height);
	}

}
