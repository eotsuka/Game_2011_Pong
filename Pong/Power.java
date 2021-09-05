package cs447;

import jig.engine.hli.physics.SpriteUpdateRules;
import jig.engine.physics.vpe.VanillaAARectangle;
import jig.engine.util.Vector2D;

public class Power extends VanillaAARectangle{
		SpriteUpdateRules updateRule2;
		
		boolean miss;
		Power(double x, double y){
			super("resources/power.png");
			position = new Vector2D(x, y);
			updateRule2 = new SpriteUpdateRules(Pong.WORLD_WIDTH, Pong.WORLD_HEIGHT);
		}
		public void update(long deltaMs) {
			position = position.translate(velocity.scale(deltaMs/100.0));

			if (updateRule2.wrap(this, SpriteUpdateRules.SIDE_SOUTH) 
					== SpriteUpdateRules.SIDE_SOUTH) {
				miss = true;
			}
			
		}		
}
