package cs447;

import jig.engine.physics.vpe.VanillaAARectangle;
import jig.engine.util.Vector2D;

public class Paddle extends VanillaAARectangle {
	//class Paddle extends VanillaAARectangle {
		public static final int NO_COLLISION = 0;
		public static final int DOWN = 1;
		public static final int LEFT = 2;
		public static final int RIGHT = 3;
		public static final int UP = 4;
		public static final int DOWN_LEFT = 5;
		public static final int DOWN_RIGHT = 6;
		public static final int UP_LEFT = 7;
		public static final int UP_RIGHT = 8;
		int shootEnabled;
		
		Paddle() {
			// if we didn't have a sprite sheet, we could just
			// load an individual image, or have the resource factory
			// create a temporary one for us:
			// super("paddle-64x16r-1-1-1.png");
			super(Pong.SPRITE_SHEET + "#paddle");
			position = new Vector2D(Pong.WORLD_WIDTH/2, Pong.WORLD_HEIGHT - 60);
			shootEnabled = 0;
		}
		public void update(long deltaMs) {
			position = position.translate(velocity.scale(deltaMs/100.0));
			position = position.clampX(0, Pong.WORLD_WIDTH - getWidth());
			
			
		}
		
	
		
		public int collideWith(Ball b){
			double ballX = b.getPosition().getX();
			double ballY = b.getPosition().getY();
			double d = b.getRadius() * 2;
			
			if(getBoundingBox().contains(ballX, ballY+d) 
					&& getBoundingBox().contains(ballX+d, ballY+d)){
				return UP;
			}else if(getBoundingBox().contains(ballX, ballY) 
					&& getBoundingBox().contains(ballX+d, ballY)){
				return DOWN;
			}else if(getBoundingBox().contains(ballX, ballY) 
					&& getBoundingBox().contains(ballX, ballY+d)){
				return RIGHT;
			}else if(getBoundingBox().contains(ballX+d, ballY) 
					&& getBoundingBox().contains(ballX+d, ballY+d)){
				return LEFT;
			}else if(getBoundingBox().contains(ballX+d, ballY+d)){
				return UP_LEFT;
			}else if(getBoundingBox().contains(ballX+d, ballY)){
				return DOWN_LEFT;
			}else if(getBoundingBox().contains(ballX, ballY+d)){
				return UP_RIGHT;
			}else if(getBoundingBox().contains(ballX, ballY)){
				return DOWN_RIGHT;
			}else{
				return NO_COLLISION;
			}
		}
	//}
}
