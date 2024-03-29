package model.builderStrategy;

import java.awt.Color;
import model.Power;

// temporarily doubles amount of bullets shooter can have on field at one time
public class DoubleBulletsPowerBuilder extends PowerBuilder {

	@Override
	public void buildFallSpeed() {
		power.setFallSpeed(5);
	}

	@Override
	public void buildColor() {
		power.setColor(Color.red);
	}

	@Override
	public void buildSize() {
		power.setSize(Power.WIDTH, Power.HEIGHT);
	}

	@Override
	public void buildPowerType() {
		power.setPowerType(Power.PowerType.EXTRA_BULLETS);
	}
	
}
