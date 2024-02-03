package graphics;

import java.awt.Rectangle;
import java.util.*;

public class Enemy extends Rectangle{
	//attributes
		int spawnPoint;
		int speed = 1;
		Random rand = new Random();
		
		Enemy(){
			spawnPoint = (rand.nextInt(4));;
			switch (spawnPoint) { //spawns the enemy randomly from the 4 entrances on the map
			case 0:
				x = 750;
				y = 30;
				break;
			case 1:
				x = 470;
				y = 355;
				break;
			case 2:
				x = 1030;
				y = 355;
				break;
			case 3:
				x = 750;
				y = 650;
				break;
			}
			width = 30;
			height = 30;
		}
		/*
		 * Moves the enemy
		 */
		void moveLeft() {
			x -= speed;
		}
		
		/*
		 * 
		 */
		void moveUp() {
			y -= speed;
		}
		
		void moveRight() {
			x += speed;
		}
		
		void moveDown() {
			y += speed;
		}
}
