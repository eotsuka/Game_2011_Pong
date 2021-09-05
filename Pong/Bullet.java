package cs447;

import jig.engine.hli.physics.SpriteUpdateRules;
import jig.engine.physics.vpe.VanillaAARectangle;
import jig.engine.util.Vector2D;

public class Bullet extends VanillaAARectangle {
	SpriteUpdateRules updateRule;
	boolean miss;
		Bullet(){
			super("resources/bullet.png");
			position = new Vector2D(-10, 0);
			velocity = new Vector2D(0, 0);
			miss = false;
			updateRule = new SpriteUpdateRules(Pong.WORLD_WIDTH, Pong.WORLD_HEIGHT);
		}
		public void update(long deltaMs) {
			position = position.translate(velocity.scale(deltaMs/100.0));
			velocity = new Vector2D(0, -50);
			
			if (updateRule.wrap(this, SpriteUpdateRules.SIDE_NORTH) 
					== SpriteUpdateRules.SIDE_NORTH) {
				miss = true;
				
			}
			
		}
	
}
