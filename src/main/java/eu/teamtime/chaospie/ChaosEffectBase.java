package eu.teamtime.chaospie;

import java.lang.reflect.Method;

import org.spongepowered.api.event.Listener;

/**
 * Defines the methods that every chaos effect must implement.
 * 
 * @author Dennis
 */
public abstract class ChaosEffectBase {
	
	private boolean hasListeners = false;
	private boolean listenersChecked = false;
	
	public abstract void start();

	public abstract void stop();

	public abstract int lengthInSeconds();

	public abstract int getWeight();

	public abstract String getName();
	
	protected void stopSelf() {
		ChaosPie.instance().stopCurrentEffect();
	}
	
	public boolean containsListeners() {
		if (listenersChecked)
			return hasListeners;
		
		listenersChecked = true;
		for (Method m : this.getClass().getMethods()) {
			if (m.isAnnotationPresent(Listener.class)) {
				hasListeners = true;
				return true;
			}
		}
		return false;
	}
}