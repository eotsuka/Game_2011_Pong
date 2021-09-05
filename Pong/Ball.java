package cs447;

import jig.engine.hli.physics.SpriteUpdateRules;
import jig.engine.physics.vpe.VanillaSphere;
import jig.engine.util.Vector2D;

public class Ball extends VanillaSphere{
		SpriteUpdateRules updateRule;
		int reflections;
		int drop;
		
		Ball() {
			super(Pong.SPRITE_SHEET + "#ball");
			position = new Vector2D(200, 200);
			velocity = new Vector2D(20, 20);
			
			updateRule = new SpriteUpdateRules(Pong.WORLD_WIDTH, Pong.WORLD_HEIGHT);
			reflections = 0;
			drop = 0;
		}

		@Override
		public void update(long deltaMs) {
			position = position.translate(velocity.scale(deltaMs/100.0));
			// bounces of top, left and side walls
			if (updateRule.wrapOrReflect(this, SpriteUpdateRules.SIDE_SOUTH) 
					== SpriteUpdateRules.SIDE_NORTH) {
				reflections++;
			}else if(updateRule.wrap(this, SpriteUpdateRules.SIDE_SOUTH) == SpriteUpdateRules.SIDE_SOUTH){
				drop++;
			}
		}
		
		
		public void boundX(){
			setVelocity(new Vector2D(-getVelocity().getX(), getVelocity().getY()));
		}
		
		public void boundY(){
			setVelocity(new Vector2D(getVelocity().getX(), -getVelocity().getY()));
		}
		
		public void boundXY(){
			setVelocity(new Vector2D(-getVelocity().getX(), -getVelocity().getY()));
		}
		
	
}
