package graphics;

import java.awt.Rectangle;

public class Bullet extends Rectangle{
	//attributes
		int speed = 5;
		char direction;
		
	Bullet(){
		x = 750;
		y = 355;
		width = 5;
		height = 5;
		direction = 'a';
	}
	
	void moveLeft() {
		x -= speed;
	}
	
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
