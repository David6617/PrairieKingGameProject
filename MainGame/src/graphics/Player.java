package graphics;

import java.awt.Rectangle;

public class Player extends Rectangle{
	//attributes
	int speed = 2;
	
	Player(){
		x = 750;
		y = 355;
		width = 30;
		height = 30;
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
