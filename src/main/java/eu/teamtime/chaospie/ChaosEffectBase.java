package eu.teamtime.chaospie;

/**
 * Defines the methods that every chaos effect must implement.
 * 
 * @author Dennis
 */
public abstract class ChaosEffectBase {

	public abstract void start();

	public abstract void stop();

	public abstract int lengthInSeconds();

	public abstract int getWeight();

	protected void stopSelf() {
		ChaosPie.instance().stopCurrentEffect();
	}
}