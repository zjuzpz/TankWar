import java.awt.*;

public class Explode {
	
	private int x, y;
	private boolean live = true;
	
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	private TankClient tc;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static boolean init = false;
	
	private static Image[] imgs = {
		tk.getImage(Explode.class.getClassLoader().getResource("images/1.png")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/2.png")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/3.png")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/4.png")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/5.png")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/6.png")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/7.png")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/8.png")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/9.png"))		
	};
	
	int step = 0;
	
	Explode(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	Explode(int x, int y, TankClient tc) {
		this(x,y);
		this.tc = tc;
	}
	
//To load the pictures first	
	public boolean draw(Graphics g) {
		if(false == init) {
			for(int i=0; i<imgs.length; i++) {
				g.drawImage(imgs[i], -100, -100, null);
			}
			init = true;
		}
		
		if(!live) {
			return false;
		}
		if(step == imgs.length) {
			live = false;
			step = 0;
			return false;
		}
		g.drawImage(imgs[step], x - 45, y - 35, null);
		step += 1;
		return true;
	}
	

}
