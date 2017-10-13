package eu.teamtime.chaospie.effects;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.explosion.Explosion;

import com.google.common.collect.Lists;

import eu.teamtime.chaospie.ChaosEffectBase;
import eu.teamtime.chaospie.ChaosPie;

/**
 * Chaos effect that causes sporadic explosions underneath players,
 * sending them flying into the air.
 * 
 * @author Karanum
 */
public class RocketJumperEffect extends ChaosEffectBase {

	private static final double UPWARD_VELOCITY = 1.25;
	
	private Task task;
	private Random random;
	private Explosion.Builder explosionTemplate;
	private List<Player> cooldownPlayers;
	
	public RocketJumperEffect() {
		random = new Random();
	}
	
	@Override
	public void start() {
		MessageChannel.TO_PLAYERS.send(Text.of(TextColors.RED, "Did you hear something? Get your market gardener ready!"));
		cooldownPlayers = Lists.newArrayList();		
		explosionTemplate = Explosion.builder()
				.canCauseFire(false)
				.shouldDamageEntities(false)
				.shouldBreakBlocks(false)
				.shouldPlaySmoke(true)
				.radius(0.5f);
		
		scheduleTask();
	}

	@Override
	public void stop() {
		MessageChannel.TO_PLAYERS.send(Text.of(TextColors.GREEN, "The explosions stop. The market is closed for the day."));
		if (task != null)
			task.cancel();
	}

	@Override
	public int lengthInSeconds() {
		return 60;
	}

	@Override
	public int getWeight() {
		return 30;
	}

	@Override
	public String getName() {
		return "Rocket Jumper";
	}
	
	private void scheduleTask() {
		task = Task.builder()
				.delay(random.nextInt(750) + 750, TimeUnit.MILLISECONDS)
				.execute(() -> doTick())
				.submit(ChaosPie.instance());
	}
	
	private void doTick() {
		Sponge.getCauseStackManager().pushCause(ChaosPie.instance());
		List<Player> explodedPlayers = Lists.newArrayList();
		
		for (Player p : Sponge.getServer().getOnlinePlayers()) {
			if (cooldownPlayers.contains(p) || random.nextDouble() < 0.7)
				continue;
			explodedPlayers.add(p);
			launchPlayer(p);
		}
		
		Sponge.getCauseStackManager().popCause();
		cooldownPlayers.clear();
		cooldownPlayers = explodedPlayers;
		scheduleTask();
	}
	
	private void launchPlayer(Player p) {
		Explosion explosion = explosionTemplate.location(p.getLocation()).build();
		p.getWorld().triggerExplosion(explosion);
		p.setVelocity(p.getVelocity().add(0, UPWARD_VELOCITY, 0));
	}

}
