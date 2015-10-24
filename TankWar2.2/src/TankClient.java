/*  
	Add pictures for tank and explodes.
 	                 
 */

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class TankClient extends Frame {
	
	private Font fontGameOver = new Font("Times New Roman", Font.BOLD, 50);
	private Font fontRestart = new Font("Times New Roman", Font.BOLD, 25);
	
// Window size and color
	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;
	public static final Color backgroundColor = Color.cyan;
	
	Image offScreenImage = null;
	
// Tank initial position 	
	Tank myTank = new Tank(50, 550, true, this, Dir.STOP);
	List<Tank> enemyTanks = new ArrayList<Tank>();
	
	
// Bullets initialization
	List<Bullet> bullets = new ArrayList<Bullet>();
	List<Explode> explodes = new ArrayList<Explode>();

// Walls initialization
	List<Wall> walls = new ArrayList<Wall>();

// Blood
	Blood blood = new Blood(this);
	
	int score = 0;
	
	PaintThread paintThread = new PaintThread();

	public static void main(String[] args) {
		new TankClient().launch();
	}
	
	public void launch() {
				
		addEnemys();
		addWalls();
		
		setBounds(570,270,WINDOW_WIDTH, WINDOW_HEIGHT);
		setResizable(false);
		setTitle("TankWar");
		setBackground(backgroundColor);
	
	// Close window
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				setVisible(false);
				System.exit(0);
			}
			
		});

		addKeyListener(new KeyMonitor());	
		new Thread (paintThread).start();
		setVisible(true);
	}
	
	public void paint(Graphics g) {	
		if(!myTank.isLive()) {
			Font f = g.getFont();
			Color c = g.getColor();
			g.setColor(Color.red);
			g.setFont(fontGameOver);
			g.drawString("Game Over", 280, 250);	
			g.setFont(fontRestart);
			g.drawString("Press F2 to restart the game", 250, 300);
			g.setColor(c);
			g.setFont(f);
			
		}
		g.drawString("Enemys count: " + enemyTanks.size(), 10, 50);
		g.drawString("Score: " + score, 10, 70);
		g.drawString("Health: " + myTank.health, 10, 90);

//Paint walls
		for(int i=0;i<walls.size();i++) {
			Wall w = walls.get(i);
			w.draw(g);
		}

//Paint blood
		blood.draw(g);
		
// Paint tanks
		myTank.draw(g);
		myTank.meetTanks(enemyTanks);
		myTank.meetWalls(walls);
		myTank.getBlood();
		if(enemyTanks.size()<=0) {
			addEnemys();
		}
		for(int i=0;i<enemyTanks.size();i++) {
			Tank t = enemyTanks.get(i);
			t.meetWalls(walls);
			t.meetTanks(enemyTanks);
			t.draw(g);
		}

		
// Paint bullets		
		for(int i=0; i<bullets.size(); i++) {
			Bullet b = bullets.get(i);
			if(!b.isLive()) {
				bullets.remove(b); 
			} else {
				b.hitWalls(walls);
				if(b.hitTanks(enemyTanks)) {
					score += 10;
				};
				b.hit(myTank);
				b.draw(g);
			}
		}
		
// Paint explodes
		for(int i=0; i<explodes.size(); i++) {
			Explode e = explodes.get(i);
			if(!e.isLive()) {
				explodes.remove(e);
			} else {
				e.draw(g);
			}
		}
		
	}
	
//Remove blick
	public void update(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(WINDOW_WIDTH, WINDOW_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(backgroundColor);
		gOffScreen.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		gOffScreen.setColor(c);
		paint(offScreenImage.getGraphics());
		g.drawImage(offScreenImage, 0, 0, null);
		
	}
	
	private class PaintThread implements Runnable {
		public void run() {
			while(true) {
				try {
					Thread.sleep(40);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				repaint();
			}
		}
	}
	
	private class KeyMonitor extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}
	}
	
	public void addEnemys() {
		for(int i=0;i<5;i++) {
			enemyTanks.add(new Tank(200 + 80 * i, 150, false, this, Dir.D));
			enemyTanks.add(new Tank(200 + 80 * i, 350, false, this, Dir.D));
		}
	}
	
	public void addWalls() {
		Wall w1 = new Wall(100, 130, 20, 400, this);
		Wall w2 = new Wall(200, 100, 500, 20, this);
		walls.add(w1);
		walls.add(w2);
	}
	
}



