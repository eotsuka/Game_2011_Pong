package cs447;

import jig.engine.physics.vpe.VanillaAARectangle;
import jig.engine.util.Vector2D;

public class Brick extends VanillaAARectangle{
	//class Brick extends VanillaAARectangle{
		public static final int NO_COLLISION = 0;
		public static final int DOWN = 1;
		public static final int LEFT = 2;
		public static final int RIGHT = 3;
		public static final int UP = 4;
		public static final int DOWN_LEFT = 5;
		public static final int DOWN_RIGHT = 6;
		public static final int UP_LEFT = 7;
		public static final int UP_RIGHT = 8;
		
		public boolean isDeleted;
		public int hitCount;
		public int power;
		
		Brick(int w, int h){
			super("resources/brick1.png");					
			position = new Vector2D(w, h);
			isDeleted = false;
			hitCount = 1;

		}
		Brick(int w, int h, int hit){
			super("resources/brick2.png");
			position = new Vector2D(w, h);
			isDeleted = false;
			hitCount = hit;
		}
		
		Brick(int w, int h, int hit, int power){
			super("resources/brick3.png");
			position = new Vector2D(w, h);
			isDeleted = false;
			hitCount = 1;
			this.power = 1;
		}
		
		
		public void update(long deltaMs){
			position = position.translate(velocity.scale(deltaMs/100.0));
			position = position.clampX(0, Pong.WORLD_WIDTH - getWidth());
			
		}
		
		public void delete(){
			setActivation(false);
			isDeleted = true;
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
