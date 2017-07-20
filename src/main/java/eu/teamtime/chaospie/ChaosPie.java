package eu.teamtime.chaospie;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import eu.teamtime.chaospie.effects.*;

@Plugin(id = "chaospie", name = "ChaosPie", version = "0.1")
public class ChaosPie {
	
	private Task schedule = null;
	private Task effectTask = null;
	private List<IChaosEffect> effects = Lists.newArrayList();
	private IChaosEffect activeEffect = null;
	
	@Inject
	private Logger logger;
	public Logger getLogger() {
		return logger;
	}
	
	@Listener
	public void onInit(GameInitializationEvent e) {
		effects.add(new HeliumPunchEffect(this));
	}
	
	@Listener
	public void onPostInit(GamePostInitializationEvent e) {
		schedule = Task.builder()
				.execute(() -> startRandomChaosEvent())
				//.delay(2, TimeUnit.MINUTES)
				.delay(10, TimeUnit.SECONDS)
				.interval(1, TimeUnit.MINUTES)
				.submit(this);
	}
	
	@Listener
	public void onStop(GameStoppingServerEvent e) {
		if (schedule != null)
			schedule.cancel();
	}
	
	
	private void startRandomChaosEvent() {
		logger.info("Aaaeeaeaeae event");

		stopCurrentEffect();
		
		activeEffect = effects.get(0);
		
		Sponge.getEventManager().registerListeners(this, activeEffect);
		activeEffect.start();
		
		int length = activeEffect.lengthInSeconds();
		if (length > 0)
			effectTask = Task.builder()
					.execute(() -> stopCurrentEffect())
					.delay(length, TimeUnit.SECONDS)
					.submit(this);
	}
	
	private void stopCurrentEffect() {
		if (activeEffect == null) return;
		Sponge.getEventManager().unregisterListeners(activeEffect);
		activeEffect.stop();
		activeEffect = null;
		
		if (effectTask != null) {
			effectTask.cancel();
			effectTask = null;
		}
	}
	
}
