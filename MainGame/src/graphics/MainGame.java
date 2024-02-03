/*
 * Journey of the Prairie King
 * By: David Hang
 * Date finished: June 19th, 2023
 * A simple 8-bit smashTV style mini game inspired by the one from Stardew Valley.
 * In this game, the player will attempt to eliminate enemies and wait until the timer runs out
 * Enemies will constantly spawn from all sides and approach the player
 * The player will then have to use the arrowkeys to shoot at the enemies
 */

package graphics;

import java.awt.Color;
import java.util.*;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.Timer;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.Rectangle;

//extra stuff
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import hsa2.GraphicsConsole;


public class MainGame implements ActionListener{
	// all global variables, objects, and graphics console setup
	Player p1 = new Player();
	Rectangle r1 = new Rectangle(652, 448, 205, 105); //for the beginning play button
	Rectangle r2 = new Rectangle(900, 450, 200, 100); //for the restart button on death/victory screen
	Rectangle r3 = new Rectangle(400, 460, 200, 80); //for the exit button on death/victory screen
	BufferedImage logo, play, map, instructions, player1, gg, again, exit, clock, hp, winner, instructions2, goblin;
	GraphicsConsole title = new GraphicsConsole(1500, 790, "Journey of the Prairie King");
	GraphicsConsole gc = new GraphicsConsole(1500, 790, "Overworld");
	GraphicsConsole death = new GraphicsConsole(1500, 790, "Death");
	GraphicsConsole victory = new GraphicsConsole(1500, 790, "Victory");
	String lose = "You lost!";
	String win = "You Won!";
	
	boolean move = false, bullTrigL = false, bullTrigU = false, bullTrigR = false, bullTrigD = false;
	boolean intro = true, restart = true, tryAgain = false, leave = false, trigger = false;
	int introTime = 100, sleepTime = 5, timerSpeed = 1000, timerLim = 45, clockSize = 515, timerCount = 0;
	int health = 3;
	ArrayList<Bullet> bullets = new ArrayList<Bullet>(); //arraylist to store bullets
	ArrayList<Enemy> enemies = new ArrayList<Enemy>(); //arraylist to store enemies spawned
	Timer timer = new Timer(timerSpeed, this);
	
	public static void main(String[] args) {
		new MainGame();
	} // end main
	
	/*
	 * Constructor
	 */
	MainGame(){
	loadGraphics();
	while(true) {
		//sets the graphic console visibilities
		gc.setVisible(false);
		title.setVisible(true);
		death.setVisible(false);
		victory.setVisible(false);
		
		//title
		titleSetup();
		//resets some variables for restart
		trigger = false;
		intro = true;
		while (intro) {
			checkClick();
			title.sleep(introTime);
		}
		
		//code for main game
		String filepath = "G:/My Drive/Music For ICS 3U1/TheOutlawOST.wav";
		PlayMusic(filepath);
		mainSetup();
		while (true) { //change later so its when the timer 
			if(health <= 0) {
				break;
			}
			if (timerLim == 0) {
				break;
			}
			movePlayer();
			playerShoot();
			moveBullet();
			enemyCollide();
			moveEnemy();
			drawGraphics();
			gc.sleep(sleepTime);
		}
		if (timerLim == 0) {
			victory();
			while (restart) {
				checkRestartVic();
				title.sleep(introTime); //resuses the time for the button check in the beginning
				checkExitVic();
				title.sleep(introTime);
				}
			
			if (tryAgain == true) {
				continue;
				}
			if (leave == true) {
				break;
					}
		}
	//checks for if you died or the timer ran out
		if (health <= 0) {
		death();
		while (restart) {
			checkRestartDeath();
			title.sleep(introTime); //resuses the time for the button check in the beginning
			checkExitDeath();
			title.sleep(introTime);
			}
		
		if (tryAgain == true) {
			continue;
			}
		if (leave == true) {
			break;
				}
			}
		}
	
		//exits the program if leave is true
		gc.close();
		title.close();
		victory.close();
		death.close();
		
	} //end MainGame

	private static void PlayMusic(String location) {
        try {
            File musicPath = new File(location);

            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
            }
            else {
                System.out.println("Can't find file");
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }

    }
	
	/*
	 * Loads all of the sprites into the game
	 */
	void loadGraphics() {
		try {
			logo = ImageIO.read(new File ("logo.png"));
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		try {
			play = ImageIO.read(new File ("Play.png"));
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		try {
			map = ImageIO.read(new File ("map.png"));
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		try {
			instructions = ImageIO.read(new File ("instructions.png"));
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		try {
			player1 = ImageIO.read(new File ("playerOne.png"));
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		try {
			gg = ImageIO.read(new File ("gg.png"));
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		try {
			again = ImageIO.read(new File ("again.png"));
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		try {
			exit = ImageIO.read(new File ("exit.png"));
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		try {
			clock = ImageIO.read(new File ("timer.png"));
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		try {
			hp = ImageIO.read(new File ("lives.png"));
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		try {
			winner = ImageIO.read(new File ("victory.png"));
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		try {
			instructions2 = ImageIO.read(new File ("winCond.png"));
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		try {
			goblin = ImageIO.read(new File ("goblin.png"));
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	} //end loadGraphics()
	
	/*
	 * Sets up the title screen and graphics console
	 */
	void titleSetup() {
		//reset variables for restart (so program doesn't bug)
		health = 3;
		tryAgain = false;
		leave = false;
		restart = true; 
		
		//other setup
		title.setAntiAlias(true);
 		title.setLocationRelativeTo(null);
		title.enableMouse();
		title.enableMouseMotion();
		
		//clears arraylists for restart
		enemies.clear();
		bullets.clear();
		
		//background drawing
		title.setBackgroundColor(Color.BLACK);
		title.clear();
		title.drawImage(logo, 550, 30, 400, 400); //draws the imported logo
		
		//Draws the play button
		title.setColor(Color.WHITE);
		title.fillRect(652, 448, 205, 105);
		title.setColor(Color.GRAY);
		title.fillRect(655, 450, 200, 100);
		title.drawImage(play, 660, 405, 200, 200);
		
	} //end titleSetup()

	/*
	 * Checks for clicks on play button on the title screen
	 */
	void checkClick() {	
		int mx = title.getMouseX();
		int my = title.getMouseY();
		if (title.getMouseClick() > 0) {	//if someone clicks the mouse				
			if (r1.contains(mx, my)) { //get the mouse position and see if it is inside the button
				intro = false;
				gc.setVisible(true);
				title.setVisible(false);
			}
		} 
	} //end checkClick()
	
	/*
	 * Sets up the main game and graphics console
	 */
	void mainSetup() {
		gc.setAntiAlias(true);
		gc.setAntiAlias(true);
 		gc.setLocationRelativeTo(null);
 		timer.start();
		gc.setBackgroundColor(Color.BLACK);
		gc.clear();
		
		//variable reset for restart
		timerLim = 45;
		timerCount = 0;
		health = 3;
	} //end mainSetup()
	
	/*
	 * Allows the player to move in different directions via WASD keys
	 */
	void movePlayer() {
	if (gc.isKeyDown(65)) {	//left
		if (p1.x >= 510) p1.moveLeft();
	}
	
	if (gc.isKeyDown(87)) {	//up
		if (p1.y >= 120) p1.moveUp();
	}
	
	if (gc.isKeyDown(68)) {	//right
		if (p1.x <= 960) p1.moveRight();
	}
	
	if (gc.isKeyDown(83)) {	//down
		if (p1.y <= 560) p1.moveDown();
		}
	} //end movePlayer()
	
	/*
	 * Creates a bullet and gives the object the direction in which the player shoots in
	 */
	void playerShoot() {
		if (gc.isKeyDown(37)) {	//left
			if (!bullTrigL) {
				bullTrigL = true;
				bullets.add(new Bullet());
				bullets.get(bullets.size()-1).x = p1.x;
				bullets.get(bullets.size()-1).y = p1.y;
				bullets.get(bullets.size()-1).direction = 'l';
			}	
		} if (!gc.isKeyDown(37)) bullTrigL = false;
		
		if (gc.isKeyDown(38)) {	//up
			if (!bullTrigU) {
				bullTrigU = true;
				bullets.add(new Bullet());
				bullets.get(bullets.size()-1).x = p1.x;
				bullets.get(bullets.size()-1).y = p1.y;
				bullets.get(bullets.size()-1).direction = 'u';
			}
		} if (!gc.isKeyDown(38)) bullTrigU = false;
		
		if (gc.isKeyDown(39)) {	//right
			if (!bullTrigR) {
				bullTrigR = true;
				bullets.add(new Bullet());
				bullets.get(bullets.size()-1).x = p1.x;
				bullets.get(bullets.size()-1).y = p1.y;
				bullets.get(bullets.size()-1).direction = 'r';
			}
		}if (!gc.isKeyDown(39)) bullTrigR = false;
		
		if (gc.isKeyDown(40)) {	//down
			if (!bullTrigD) {
				bullTrigD = true;
			bullets.add(new Bullet());
			bullets.get(bullets.size()-1).x = p1.x;
			bullets.get(bullets.size()-1).y = p1.y;
			bullets.get(bullets.size()-1).direction = 'd';
			}
		}if (!gc.isKeyDown(40)) bullTrigD = false;
		
	} //end playerShoot()
	
	/*
	 * Moves the bullet in the direction that the player shoots in
	 */
	void moveBullet() {
		if (bullets.size() > 0) {
		for (Bullet b: bullets) {
			if (b.direction == 'l') { //the direction "l" will move the bullet left
				b.moveLeft();
			} else if (b.direction =='u') { //the direction "u" will move the bullet up
				b.moveUp();
			} else if (b.direction == 'd') { //the direction "d" will move the bullet down
				b.moveDown();
			} else if (b.direction == 'r') { //the direction "r" will move the bullet right
				b.moveRight(); 
				}
			}
		}
	} //end moveBullet()
  
	/*
	 * Moves the enemy closer and closer towards the player
	 */
	void moveEnemy() {
		for (int a = 0; a < enemies.size(); a++) {
		if (enemies.get(a).x <= p1.x) {
			enemies.get(a).moveRight();
		}
		
		if (enemies.get(a).x >= p1.x) {
			enemies.get(a).moveLeft();
		}
		
		if (enemies.get(a).y >= p1.y) {
			enemies.get(a).moveUp();
		}
		
		if (enemies.get(a).y <= p1.y) {
			enemies.get(a).moveDown();
			}
		}
		
	} //end moveEnemy
	
	/*
	 * Checks for collision between the enemy and the player, as well as the enemy and the bullet. In both cases the enemy will disappear.
	 * If the enemy collides with the player, the player loses a life, and if the bullet collides with an enemy, the enemy and the bullet disappear
	 */
	void enemyCollide() {
		for (int e = 0;e < enemies.size(); e++) {
			if (enemies.get(e).intersects(p1)) {
				health -= 1;
				enemies.remove(e);
				break;
			}
			
		if (bullets.size() >= 1 || enemies.size() >= 1) {
			for (int b = 0;b < bullets.size(); b++) {
			if (enemies.get(e).intersects(bullets.get(b))) {
				enemies.remove(e);
				bullets.remove(b);
				break;
					}
				}
			}
		}
	} //end enemyCollide()

	/*
	 * Draws the graphics for the game
	 */
	void drawGraphics() {
		synchronized(gc) {
		gc.clear();
		gc.drawImage(map, 475, 80, 550, 550); //draws the map
		
		for (Bullet b: bullets) { //draws the bullets
			gc.setColor(Color.BLACK);
			gc.fillRect(b.x, b.y, b.width, b.height);
		}
		
		for (Enemy e: enemies) { //draws the enemies
			gc.setColor(Color.WHITE);
			gc.drawImage(goblin, e.x, e.y, e.width, e.height);
		}
		
		//draws the instructions
		gc.drawImage(instructions, 650, 650, 200, 80); //draws the instructions
		gc.drawImage(player1, p1.x, p1.y, p1.width, p1.height); //draws the player
		gc.setColor(Color.BLACK); //black bar to hide enemies spawning
		gc.fillRect(475, 0, 550, 80);
		gc.drawImage(clock, 470, 40, 40, 40); //draws the timer icon
		gc.setColor(new Color(118, 231, 56));
		gc.fillRect(510, 55, clockSize - ((clockSize/45)*timerCount), 15);
		gc.drawImage(hp, 390, 140, 40, 40); //health sprites
		gc.setColor(Color.WHITE);
		gc.drawString("x " + String.valueOf(health), 430, 170); //health number
		gc.drawImage(instructions2, 950, 80, 500, 400); //draws the map
		gc.drawString("Don't Touch the Gobins!", 1135, 350);
	
		}
	} //end drawGraphics()
	
	/*
	 * Displays and sets up the victory screen for when the timer runs out
	 */
	void victory() {
		gc.setVisible(false);
		victory.setVisible(true);
		victory.setAntiAlias(true);
 		victory.setLocationRelativeTo(null);
		victory.enableMouse();
		victory.enableMouseMotion();
		
		//draws background
		victory.setBackgroundColor(Color.BLACK);
		victory.clear();
		victory.drawImage(winner, 600, 30, 300, 350); //draws the GG
		
		//displays text
		Font verdana = new Font("Verdana", Font.BOLD, 32);
		victory.setColor(Color.WHITE);
		victory.setFont(verdana);
		victory.drawString(win, 670, 400);
		
		//Draws the button
		victory.setColor(Color.GRAY);
		victory.fillRect(400, 450, 200, 100);
		
		victory.drawImage(exit, 400, 460, 200, 80); //draws the exit button
		
		//draws the exit button
		victory.setColor(Color.GRAY);
		victory.fillRect(900, 450, 200, 100);
		
		victory.drawImage(again, 900, 450, 200, 100); //draws the restart button
	} //end victory()

	/*
	 * Displays and sets up the death screen for when you lose all of your lives
	 */
	void death() {
		gc.setVisible(false);
		death.setVisible(true);
		death.setAntiAlias(true);
 		death.setLocationRelativeTo(null);
		death.enableMouse();
		death.enableMouseMotion();
		
		//draws background
		death.setBackgroundColor(Color.BLACK);
		death.clear();
		death.drawImage(gg, 550, 30, 400, 350); //draws the GG
		
		//displays text
		Font verdana = new Font("Verdana", Font.BOLD, 32);
		death.setColor(Color.WHITE);
		death.setFont(verdana);
		death.drawString(lose, 670, 400);
		
		//Draws the button
		death.setColor(Color.GRAY);
		death.fillRect(400, 450, 200, 100);
		
		death.drawImage(exit, 400, 460, 200, 80); //draws the exit button
		
		//draws the exit button
		death.setColor(Color.GRAY);
		death.fillRect(900, 450, 200, 100);
		
		death.drawImage(again, 900, 450, 200, 100); //draws the restart button
		
	} //end death()
	
	/*
	 * Checks if the player clicks the restart button on the death screen
	 */
	void checkRestartDeath() {
		int mx = death.getMouseX();
		int my = death.getMouseY();
		if (death.getMouseClick() > 0) {	//if someone clicks the mouse				
			if (r2.contains(mx, my)) { //get the mouse position and see if it is inside the button
				restart = false;
				tryAgain = true;
			}
		} 
	} //end checkRestartDeath()
	
	/*
	 * Check if the player clicks the restart button for the victory screen
	 */
	void checkRestartVic() {
		int mx = victory.getMouseX();
		int my = victory.getMouseY();
		if (victory.getMouseClick() > 0) {	//if someone clicks the mouse				
			if (r2.contains(mx, my)) { //get the mouse position and see if it is inside the button
				restart = false;
				tryAgain = true;
			}
		} 
	} //end checkRestartVic()
	
	/*
	 * Checks if the player clicks the exit button on the death screen
	 */
	void checkExitDeath() {
		int mx = death.getMouseX();
		int my = death.getMouseY();
		if (death.getMouseClick() > 0) {	//if someone clicks the mouse				
			if (r3.contains(mx, my)) { //get the mouse position and see if it is inside the button
				restart = false;
				leave = true;
			}
		} 
	} //end checkExitDeath()

	/*
	 * Checks if the player clicks on the exit button on the victory screen
	 */
	void checkExitVic() {
		int mx = victory.getMouseX();
		int my = victory.getMouseY();
		if (victory.getMouseClick() > 0) {	//if someone clicks the mouse				
			if (r3.contains(mx, my)) { //get the mouse position and see if it is inside the button
				restart = false;
				leave = true;
			}
		} 
	} //end checkExitVic()
	
	/*
	 *Actions performed each time the timer counts
	 */
	@Override
	
	public void actionPerformed(ActionEvent ev) {
		if (ev.getSource() == timer) {
			if (timerLim % 3 == 0) {
				enemies.add(new Enemy());
			}
			timerLim--;
			timerCount++;
		}
	}//end actionPerformed(ActionEvent ev)
} 
