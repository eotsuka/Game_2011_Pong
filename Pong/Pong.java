package cs447;


import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
//import java.awt.geom.Rectangle2D;




import jig.engine.FontResource;
import jig.engine.RenderingContext;
import jig.engine.ResourceFactory;
import jig.engine.hli.StaticScreenGame;
//import jig.engine.hli.physics.SpriteUpdateRules;
import jig.engine.physics.AbstractBodyLayer;
import jig.engine.physics.Body;
import jig.engine.physics.BodyLayer;
//import jig.engine.physics.vpe.VanillaAARectangle;
//import jig.engine.physics.vpe.VanillaSphere;
import jig.engine.util.Vector2D;
//import jig.engine.audio.jsound.AudioClip;
//import jig.engine.physics.*;

/**
 * A simple paddle and ball game in about 100 SLOC.
 * 
 * @author Scott Wallace (the original ver.) 
 * @author Eriko Otsuka  
 * 
 */
public class Pong extends StaticScreenGame {
	
	static final int WORLD_WIDTH = 800;
	static final int WORLD_HEIGHT = 600;
	static final int PADDLE_SPEED = 40;
	static final int TOTAL_BRICK = 96; 
	static final int BRICK_ROW = 8; 
	static final int BRICK_HEIGHT = 16;
	static final int BRICK_WIDTH = 48; 
	static final int LIFE = 3;
	static final int NUM_LEVEL = 3;
		
	static final String SPRITE_SHEET = "resources/demo-spritesheet.png";
	
	//flags
	boolean gameRunning;
	boolean gameCleared;
	boolean gameEnded;
	boolean allLevelCleared;
	boolean gameReady;
	boolean dropPowerUsed;
	
	//variables
	int score, displayScore, displayDrop;
	int brickNum;
	int trackBrick;
	int brickPerRow;
	int left;
	int top;
	int h;
	int hasPower;
	int level;
	int level1Brick, level2Brick, level3Brick;
	
	//instances
	Ball b;
	Paddle paddle;
	Bullet bullet;
	Brick[] br;
	Power powerup;	
	FontResource scoreboardFont;
	FontResource resultFont;
	
	public Pong() {
		//Constructor
		super(WORLD_WIDTH, WORLD_HEIGHT, false);
				
		ResourceFactory.getFactory().loadResources("resources/", "demo-resources.xml");
		
		//create instances
		b = new Ball();
		paddle = new Paddle();
		bullet = new Bullet();
		br = new Brick[TOTAL_BRICK];
		
		//init variables
		score = 0;
		displayScore = 0;
		displayDrop = 0;
		level1Brick = 60;
		level2Brick = 84;
		level3Brick = 96;
		brickNum = level1Brick; //for Level 1
		brickPerRow = TOTAL_BRICK/BRICK_ROW;
		left = (WORLD_WIDTH/2) - (BRICK_WIDTH * 6);
		top = BRICK_HEIGHT * 3;
		h = 1;
		hasPower = 1;
		trackBrick = 0;
		level = 1;
		
		
		//init flags
		gameRunning = true;
		gameCleared = false;
		allLevelCleared = false;
		gameEnded = false;
		gameReady = false;
		dropPowerUsed = false;
		
				
		/****************************************
		 * Set up bricks for all levels				
		 ****************************************/
		for(int i=0; i<TOTAL_BRICK; i++){
			if(i == 48){
				//POWER BRICK
				br[i]=new Brick(left, top, h, hasPower);	
			}else if(h==2){
				//HARD BRICK
				br[i]=new Brick(left, top, h);
			}else{
				//NORMAL BRICK
				br[i]=new Brick(left, top);	
			}
			
			
			left = left + BRICK_WIDTH;
			
			
			if(i==brickPerRow - 1){
				//SECOND ROW : NORMAL BRICKS
				top=top + BRICK_HEIGHT;
				left = (WORLD_WIDTH/2) - (BRICK_WIDTH * 6);
			}else if(i==brickPerRow*2 - 1){
				//THIRD ROW : NORMAL BRICKS
				top = top + BRICK_HEIGHT;
				left = (WORLD_WIDTH/2) - (BRICK_WIDTH * 6);
			}else if(i==brickPerRow * 3 - 1){
				//FOURTH ROW : NORMAL BRICKS
				top = top + BRICK_HEIGHT;
				left = (WORLD_WIDTH/2) - (BRICK_WIDTH * 6);
			}else if(i==brickPerRow * 4 - 1){
				//FIFTH ROW : HARD BRICKS
				top = top + BRICK_HEIGHT;
				left = (WORLD_WIDTH/2) - (BRICK_WIDTH * 6);
				h = 2;
			}else if(i==brickPerRow * 5 - 1){
				//SIXTH ROW : NORMAL BRICKS
				top = top + BRICK_HEIGHT;
				left = (WORLD_WIDTH/2) - (BRICK_WIDTH * 6);
				h = 0;
			}else if(i==brickPerRow * 6 - 1){
				//SEVENTH ROW : NORMAL BRICKS
				top = top + BRICK_HEIGHT;
				left = (WORLD_WIDTH/2) - (BRICK_WIDTH * 6);
				h = 0;
			}else if(i==brickPerRow * 7 - 1){
				//EIGHTH ROW : HARD BRICKS
				top = top + BRICK_HEIGHT;
				left = (WORLD_WIDTH/2) - (BRICK_WIDTH * 6);
				h = 2;
			}
		}//END BRICK SET UP FOR LOOP
		
				
		powerup = new Power(br[48].getPosition().getX() + 16, br[48].getPosition().getY());

		scoreboardFont = ResourceFactory.getFactory().getFontResource(new Font("Sans Serif", Font.PLAIN, 12), Color.black, null);
		resultFont = ResourceFactory.getFactory().getFontResource(new Font("ARIAL", Font.BOLD, 25), Color.BLUE, null);		
		BodyLayer<Body> ball_layer = new AbstractBodyLayer.IterativeUpdate<Body>();
		BodyLayer<Body> puddle_layer = new AbstractBodyLayer.IterativeUpdate<Body>();
		BodyLayer<Body> brick_layer = new AbstractBodyLayer.IterativeUpdate<Body>();
		BodyLayer<Body> power_layer = new AbstractBodyLayer.IterativeUpdate<Body>();
		BodyLayer<Body> bullet_layer = new AbstractBodyLayer.IterativeUpdate<Body>();
		
		ball_layer.add(b);
		puddle_layer.add(paddle);
		power_layer.add(powerup);
		bullet_layer.add(bullet);
		for(int i=0; i<TOTAL_BRICK; i++){
			brick_layer.add(br[i]);
		}
		
		gameObjectLayers.add(brick_layer);
		gameObjectLayers.add(ball_layer);
		gameObjectLayers.add(power_layer);
		gameObjectLayers.add(bullet_layer);
		gameObjectLayers.add(puddle_layer);
		
		//INITIAL BRICK SET UP do not need br[60] - br[95]
		for (int i=level1Brick; i<level3Brick; i++){
			br[i].delete();
		}
	}

	
	/***********************************************
	 * render                                      
	 ***********************************************/
	public void render(RenderingContext rc) {
		super.render(rc);
		scoreboardFont.render("Score: " + score, rc, AffineTransform.getTranslateInstance(30, 570));
		scoreboardFont.render("Level: " + level, rc, AffineTransform.getTranslateInstance(750, 10));
		
		if(level == 2){
			scoreboardFont.render("Ball Speed + 5", rc, AffineTransform.getTranslateInstance(15, 10));
		}else if(level == 3){
			scoreboardFont.render("Ball Speed + 10", rc, AffineTransform.getTranslateInstance(15, 10));
		}
		
		if(paddle.shootEnabled==1){
			scoreboardFont.render("Power Up : Bullets activated", rc, AffineTransform.getTranslateInstance(15, 25));
		}
		
		if(gameEnded){
			resultFont.render("Hit Space Key to start", rc, AffineTransform.getTranslateInstance(200, 180));
		}else if(allLevelCleared){
			resultFont.render("Conguratulations!", rc, AffineTransform.getTranslateInstance(330, 300));
			resultFont.render("Score: " + displayScore, rc, AffineTransform.getTranslateInstance(330, 330));
			resultFont.render("Drop: " + displayDrop + "(N/A in this Ver.)", rc, AffineTransform.getTranslateInstance(330, 360));
			resultFont.render("   Hit space", rc, AffineTransform.getTranslateInstance(330, 400));
			
		}else if(gameCleared){
			int displayLevel = level - 1;
			resultFont.render("Level " + displayLevel + " Cleared", rc, AffineTransform.getTranslateInstance(330, 300));
			resultFont.render(" Hit space", rc, AffineTransform.getTranslateInstance(330, 360));
		}
	}


	/***********************************************
	 * update                                      
	 ***********************************************/
	public void update(long deltaMs) {
		
		if(gameRunning){
			
			super.update(deltaMs);
						
			/* variables */
			double r = b.getRadius();
			double d = r * 2;
			int ball_paddle_collision;
			int brick_ball_collision;
			boolean left, right, up;
			boolean two, three, dropPower;
			
			
			/**********************************
			 * handle BALL & PADDLE collision
			 **********************************/
			ball_paddle_collision = paddle.collideWith(b);
					
			if(ball_paddle_collision != Paddle.NO_COLLISION){
				switch(ball_paddle_collision){
					case Paddle.DOWN:
						b.setPosition(new Vector2D(b.getPosition().getX(), b.getPosition().getY() + d));
						b.boundY();
						break;
					case Paddle.UP:
						b.setPosition(new Vector2D(b.getPosition().getX(), b.getPosition().getY() - d));
						b.boundY();
						break;
					case Paddle.LEFT:
						b.setPosition(new Vector2D(b.getPosition().getX() - r, b.getPosition().getY()));
						b.boundX();
						break;
					case Paddle.RIGHT:
						b.setPosition(new Vector2D(b.getPosition().getX() + r, b.getPosition().getY()));
						b.boundX();
						break;
					case Paddle.UP_LEFT:
						b.setPosition(new Vector2D(b.getPosition().getX() - r, b.getPosition().getY() - r));
						b.boundXY();
						break;
					case Paddle.UP_RIGHT:
						b.setPosition(new Vector2D(b.getPosition().getX() + r, b.getPosition().getY() - r));
						b.boundXY();
						break;
					case Paddle.DOWN_LEFT:
						b.setPosition(new Vector2D(b.getPosition().getX() - r, b.getPosition().getY() + r));
						b.boundXY();
						break;
					case Paddle.DOWN_RIGHT:
						b.setPosition(new Vector2D(b.getPosition().getX() + r, b.getPosition().getY() + r));
						b.boundXY();
						break;
				}
			}

			
			
			
			/**********************************
			 * handle BRICK & BALL   collision
			 * handle BRICK & BULLET collision
			 **********************************/
			for (int i=0; i< brickNum; i++){
				
				if(br[i].isDeleted==false){
					
					/**********************************
					 * handle BRICK & BULLET collision
					 **********************************/
					if(paddle.shootEnabled == 1 && (br[i].getBoundingBox().contains(bullet.getPosition().getX(), bullet.getPosition().getY()) 
						|| br[i].getBoundingBox().contains(bullet.getPosition().getX() + 16, bullet.getPosition().getY()))){
						br[i].hitCount--;
						bullet.setPosition(new Vector2D(paddle.getPosition().getX() + 32, paddle.getPosition().getY()));
						if(br[i].hitCount==0){
							br[i].delete();	
							score = score + 2;
							displayScore = displayScore + 2;
							trackBrick++;
							bullet.setPosition(new Vector2D(paddle.getPosition().getX() + 32, paddle.getPosition().getY()));
							bullet.setVelocity(new Vector2D(0, -50));
						}
					}
					
					/**********************************
					 * handle BRICK & BALL collision
					 **********************************/
					brick_ball_collision = br[i].collideWith(b);
						
					if(brick_ball_collision != Brick.NO_COLLISION){
						br[i].hitCount--;
						if(br[i].hitCount == 0){
							br[i].delete();
							score++;
							displayScore++ ;
							trackBrick++;
							if(br[i].power == 1){
								//powerup.setPosition(new Vector2D(br[i].getPosition().getX() + 16, br[i].getPosition().getY()));
								powerup.setVelocity(new Vector2D(0,5));
								
							}
						}
						switch(brick_ball_collision){
							case Brick.DOWN:
							case Brick.UP:
								b.boundY();
								break;
							case Brick.LEFT:
							case Brick.RIGHT:
								b.boundX();
								break;
							case Brick.UP_LEFT:
							case Brick.UP_RIGHT:
							case Brick.DOWN_LEFT:
							case Brick.DOWN_RIGHT:
								b.boundXY();
								break;
						}//END SWITCH
						break;
					}//END IF (BRICK_BALL_COLLISION)	
				}//END IF (NOT DELETED)
			}//END FOR LOOP (EACH BRICK)
			
			
		
			
			/**********************************
			 * handle POWER & PADDLE collision
			 **********************************/
			if(paddle.getBoundingBox().contains(powerup.getPosition().getX(), powerup.getPosition().getY() + 16) 
				|| paddle.getBoundingBox().contains(powerup.getPosition().getX() + 16, powerup.getPosition().getY() + 16)){
				powerup.setVelocity(new Vector2D(0, 0));
				powerup.setActivation(false);
				bullet.setActivation(true);
				paddle.shootEnabled = 1;
				//bullet.setPosition(new Vector2D(paddle.getPosition().getX() + 32, paddle.getPosition().getY()));
				bullet.setVelocity(new Vector2D(0, -50));
			}
		
			//if unable to get powerup, it's gone
			if(powerup.miss){
				powerup.setVelocity(new Vector2D(0, 0));
				powerup.miss = false;
				powerup.setActivation(false);
			}
			
			//if miss the shot, reset the bullet position 
			if(paddle.shootEnabled == 1 && bullet.miss == true){
				bullet.setPosition(new Vector2D(paddle.getPosition().getX() + 32, paddle.getPosition().getY()));
				bullet.setVelocity(new Vector2D(0, -50));
				bullet.miss = false; 
			}
			
			
			
			displayDrop = b.drop;
			
			
			/**********************************
			 * key action
			 **********************************/
			left = keyboard.isPressed(KeyEvent.VK_LEFT);
			right = keyboard.isPressed(KeyEvent.VK_RIGHT);
			two = keyboard.isPressed(KeyEvent.VK_2);
			three = keyboard.isPressed(KeyEvent.VK_3);
			dropPower = keyboard.isPressed(KeyEvent.VK_D);
			
			/* paddle LEFT <-> RIGHT */
			if (left && !right) {
				paddle.setVelocity(new Vector2D(-PADDLE_SPEED, 0));
			} else if (right && !left) {
				paddle.setVelocity(new Vector2D(PADDLE_SPEED, 0));
			}else{
				paddle.setVelocity(new Vector2D(0,0));
			}
			
			
			
			/* cheat code */
			if(two){
				gameCleared = true;
				level = 2;
				gameRunning = false;
				two = false;
			}else if(three){
				gameCleared = true;
				level = 3;
				gameRunning = false;
				three = false;
			}else if(dropPower){
				br[48].delete();
				//score++;
				//displayScore++ ;
				//trackBrick = trackBrick +1;
				powerup.setVelocity(new Vector2D(0,5));
				dropPowerUsed = true;
				dropPower = false;
			}
			
			//check if all bricks are destroyed
			if(dropPowerUsed){
				if((level == 1) && (trackBrick+1 == level1Brick)){
					level = 2;
					gameRunning = false;
					gameCleared = true;
					allLevelCleared = false;
				}else if((level == 2) && (trackBrick+1 == level2Brick)){
					level = 3;
					gameRunning = false;
					gameCleared = true;
					allLevelCleared = false;
				}else if((level == 3) && (trackBrick+1 == level3Brick)){
					level = 1;
					gameRunning = false;
					gameCleared = false;
					allLevelCleared = true;
				}	
			}else{
				if((level == 1) && (trackBrick == level1Brick)){
					level = 2;
					gameRunning = false;
					gameCleared = true;
					allLevelCleared = false;
				}else if((level == 2) && (trackBrick == level2Brick)){
					level = 3;
					gameRunning = false;
					gameCleared = true;
					allLevelCleared = false;
				}else if((level == 3) && (trackBrick == level3Brick)){
					level = 1;
					gameRunning = false;
					gameCleared = false;
					allLevelCleared = true;
				}	
			}
			
		
			
			
			
		}else if(gameCleared || allLevelCleared){
			if(level==1){
				//set up for level 1
				trackBrick = 0;
				brickNum = level1Brick;
				setScreen(level1Brick);

			}else if(level==2){
				//set up for level 2
				trackBrick = 0;
				brickNum = level2Brick;
				setScreen(level2Brick);

			}else if(level==3){
				//set up for level 3
				trackBrick = 0;
				brickNum = level3Brick;
				setScreen(level3Brick);	
			}
			
			
			boolean space = keyboard.isPressed(KeyEvent.VK_SPACE);
			if(space){
					gameRunning = true;
					gameEnded = false;
					gameCleared = false;
					allLevelCleared = false;
					space = false;
			}
			
			
		}else if(gameEnded){
			//No GAMEOVER for this version
			
		}//END IF checking flag gameRunning/gameCleared/gameEnded
		
		
	}//END UPDATE()
		
		
	
	public void setScreen(int brick){
		
		//change the ball speed 
		if(level == 1){
			b.setVelocity(new Vector2D(20, 20));
			top = BRICK_HEIGHT * 3;
		}if(level == 2){
			b.setVelocity(new Vector2D(25, 25));
			top = BRICK_HEIGHT * 5;
		}if(level == 3){
			b.setVelocity(new Vector2D(30, 30));
			top = BRICK_HEIGHT * 5;
		}
		
		//set ball position
		b.setPosition(new Vector2D(200, 220));
		
		//deactivate bullet
		paddle.shootEnabled = 0;
		bullet.setPosition(new Vector2D(paddle.getPosition().getX() + 32, paddle.getPosition().getY()));
		bullet.setVelocity(new Vector2D(0, 0));
		bullet.setActivation(false);
		
		//reset power
		powerup.setActivation(true);
		powerup.setPosition(new Vector2D(br[48].getPosition().getX() + 16, br[48].getPosition().getY()));
		powerup.setVelocity(new Vector2D(0,0));
		
		//re-initialize variables
		trackBrick = 0;
		score = 0;
		b.drop = 0;
		left = (WORLD_WIDTH/2) - (BRICK_WIDTH * 6);
		
		
		for(int i=0; i<brick; i++){
			
			br[i].setActivation(true);		
			br[i].setPosition(new Vector2D(left, top));
			br[i].isDeleted = false;
			
			left = left + BRICK_WIDTH;
			
			if(i==brickPerRow - 1 || i==brickPerRow*2-1 ||
					i==brickPerRow*3-1 || i==brickPerRow*4-1 ||
					i==brickPerRow*5-1 || i==brickPerRow*6-1 || i==brickPerRow*7-1){
				top=top + BRICK_HEIGHT;
				left = (WORLD_WIDTH/2) - (BRICK_WIDTH * 6);
			}
			
			if(i == 48){
				//POWER BRICK
				br[i].power = 1;
				br[i].hitCount = 1;
			}else if((i >= 48 && i <= 59) || (i >= 84 && i <=95)){
				//HARD BRICK
				br[i].hitCount = 2;
			}else{
				//NORMAL BRICK
				br[i].hitCount = 1;	
			}
			
		}//END BRICK SET UP FOR LOOP
		
		

	}
		
	
	public static void main(String[] args) {
		Pong p = new Pong();
		p.trackBrick = 0;
		p.run();
	}
	

}
	
	



