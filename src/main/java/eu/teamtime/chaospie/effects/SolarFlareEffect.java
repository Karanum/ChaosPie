package eu.teamtime.chaospie.effects;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.event.cause.entity.damage.DamageTypes;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.AABB;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;

import eu.teamtime.chaospie.ChaosEffectBase;
import eu.teamtime.chaospie.ChaosPie;

public class SolarFlareEffect extends ChaosEffectBase {

	private static final double INTERVAL_REGRESSION = 0.725;
	
	private final DamageSource DMG_SOURCE;
	private final ParticleEffect PARTICLE_EFFECT;
	
	private ChaosPie plugin;
	private long interval = 0;
	private Task damageTask = null;
	private Task particleTask = null;
	
	public SolarFlareEffect() {
		plugin = ChaosPie.instance();
		
		DMG_SOURCE = DamageSource.builder()
				.absolute()
				.bypassesArmor()
				.type(DamageTypes.FIRE)
				.build();
		
		PARTICLE_EFFECT = ParticleEffect.builder()
				.type(ParticleTypes.FLAME)
				.quantity(25)
				.offset(new Vector3d(0.5, 0.5, 0.5))
				.build();
	}
	
	@Override
	public void start() {
		interval = 10000;
		MessageChannel.TO_PLAYERS.send(Text.of(TextColors.RED, "A solar flare is approaching! Take cover!"));
		
		damageTask = Task.builder()
				.delay(interval, TimeUnit.MILLISECONDS)
				.execute(() -> doDamage())
				.submit(plugin);
		
		particleTask = Task.builder()
				.delay(interval / 4, TimeUnit.MILLISECONDS)
				.execute(() -> spawnParticles())
				.submit(plugin);
	}

	@Override
	public void stop() {
		MessageChannel.TO_PLAYERS.send(Text.of(TextColors.GREEN, "The solar flare seems to have passed."));
		damageTask.cancel();
		particleTask.cancel();
	}

	@Override
	public int lengthInSeconds() {
		return 90;
	}

	@Override
	public int getWeight() {
		return 10;
	}
	
	private Collection<Entity> getEntitiesInSunlight() {
		List<Entity> list = Sponge.getServer().getOnlinePlayers().stream().filter(p -> {
			Location<World> loc = p.getLocation();
			return (loc.getBlockY() >= loc.asHighestLocation().getBlockY());
		}).collect(Collectors.toList());
		
		for (World w : Sponge.getServer().getWorlds()) {
			list.addAll(w.getEntities(e -> {
				if (!(e instanceof Living))
					return false;
				Location<World> loc = e.getLocation();
				return (loc.getBlockY() >= loc.asHighestLocation().getBlockY());
			}));
		}
		return list;
	}
	
	private void spawnParticles() {
		for (Entity e : getEntitiesInSunlight()) {
			Optional<AABB> aabb = e.getBoundingBox();
			if (aabb.isPresent())
				e.getWorld().spawnParticles(PARTICLE_EFFECT, aabb.get().getCenter());
		}
		
		particleTask = Task.builder()
				.delay(Math.max(interval / 4, 500), TimeUnit.MILLISECONDS)
				.execute(() -> spawnParticles())
				.submit(plugin);
	}
	
	private void doDamage() {		
		for (Entity e : getEntitiesInSunlight()) {
			e.damage(2.0, DMG_SOURCE);
		}
		
		interval = Math.max(Math.round(interval * INTERVAL_REGRESSION), 1000);
		damageTask = Task.builder()
				.delay(interval, TimeUnit.MILLISECONDS)
				.execute(() -> doDamage())
				.submit(plugin);
	}

}
