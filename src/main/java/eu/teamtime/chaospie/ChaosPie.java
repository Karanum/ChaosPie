package eu.teamtime.chaospie;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import eu.teamtime.chaospie.commands.*;
import eu.teamtime.chaospie.effects.*;

/**
 * A pie that tastes like chaos. This is a plugin that adds a touch of chaotic mess into your server.
 * 
 * @author Dennis
 */
@Plugin(id = "chaospie", name = "ChaosPie", version = "0.1")
public class ChaosPie {
	
	public static final int TIME_BETWEEN_EFFECTS = 120;
	
	private static ChaosPie instance;
	public static ChaosPie instance() {
		return instance;
	}
	
	private Task schedule = null;
	private Task stopper = null;
	private List<ChaosEffectBase> effects = Lists.newArrayList();
	private ChaosEffectBase activeEffect = null;
	private EffectMetrics metrics = null;
	
	@Inject
	private Logger logger;
	public Logger getLogger() {
		return logger;
	}
	
	@Listener
	public void onInit(GameInitializationEvent e) {
		instance = this;

		// TODO Make sure all effects are in
		effects.add(new CakeCreeperEffect());
		effects.add(new FishingNightmareEffect());
		effects.add(new FloatingEffect());
		effects.add(new HeliumPunchEffect());
		effects.add(new HostileDuplicateEffect());
		effects.add(new MiniCookEffect());
		effects.add(new NascarEffect());
		effects.add(new RocketJumperEffect());
		effects.add(new SpeedySpidersEffect());
		effects.add(new SolarFlareEffect());
		
		metrics = new EffectMetrics();
		registerCommands();
	}
	
	@Listener
	public void onServerStart(GameStartedServerEvent e) {
		schedule = Task.builder()
				.execute(this::startRandomChaosEvent)
				.delay(10, TimeUnit.SECONDS)
				.interval(TIME_BETWEEN_EFFECTS, TimeUnit.SECONDS)
				.name("ChaosPie-ChaosEffectScheduler")
				.submit(this);
	}
	
	@Listener
	public void onStop(GameStoppingServerEvent e) {
		if (schedule != null)
			schedule.cancel();
		
		stopCurrentEffect();
	}
	
	
	public EffectMetrics getEffectMetrics() {
		return metrics;
	}
	
	public List<ChaosEffectBase> getAllEffects() {
		return new ArrayList<>(effects);
	}
	
	public ChaosEffectBase getCurrentEffect() {
		return activeEffect;
	}
	
	public void startRandomChaosEvent() {
		List<ChaosEffectBase> validEffects = new ArrayList<>(effects);
		if (activeEffect != null && effects.size() > 1)
			validEffects.remove(activeEffect);
		
		stopCurrentEffect();
		
		// Select event by random and weight
		int totalWeight = 0;
		for (ChaosEffectBase effect : validEffects) {
			totalWeight += effect.getWeight();
		}
		
		long randWeight = (new Random()).nextInt(totalWeight);
		for (ChaosEffectBase effect : validEffects) {
			randWeight -= effect.getWeight();
			if (randWeight <= 0) {
				activeEffect = effect;
				break;
			}
		}
		
		if (activeEffect.containsListeners())
			Sponge.getEventManager().registerListeners(this, activeEffect);
		activeEffect.start();
		Sponge.getServer().getConsole().sendMessage(Text.of(TextColors.RED, "Starting effect: ", TextColors.GOLD, activeEffect.getName()));
		
		int length = activeEffect.lengthInSeconds();
		if (length > 0)
			stopper = Task.builder()
					.execute(this::stopCurrentEffect)
					.delay(length, TimeUnit.SECONDS)
					.name("ChaosPie-ChaosEffectStopper")
					.submit(this);
		
		metrics.addEffectCount(activeEffect);
	}
	
	public void stopCurrentEffect() {
		if (activeEffect == null) return;
		if (activeEffect.containsListeners()) 
			Sponge.getEventManager().unregisterListeners(activeEffect);
		activeEffect.stop();
		Sponge.getServer().getConsole().sendMessage(Text.of(TextColors.GREEN, "Stopping effect: ", TextColors.GOLD, activeEffect.getName()));
		
		activeEffect = null;
		if (stopper != null) {
			stopper.cancel();
			stopper = null;
		}
	}
	
	private void registerCommands() {
		CommandSpec reportCommand = CommandSpec.builder()
				.permission("chaospie.command.report")
				.executor(new CommandReport())
				.build();
		
		CommandSpec mainCommand = CommandSpec.builder()
				.permission("chaospie.command.base")
				.description(Text.of("Main command for ChaosPie"))
				.child(reportCommand, "report")
				.build();
		
		Sponge.getCommandManager().register(this, mainCommand, "chaos");
	}
}