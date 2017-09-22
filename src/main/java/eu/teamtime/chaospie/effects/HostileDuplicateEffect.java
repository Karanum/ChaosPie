package eu.teamtime.chaospie.effects;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.manipulator.mutable.entity.HealthData;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntitySnapshot;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.Hostile;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;

import eu.teamtime.chaospie.ChaosPie;
import eu.teamtime.chaospie.IChaosEffect;

public class HostileDuplicateEffect implements IChaosEffect {

	private ChaosPie plugin;
	
	public HostileDuplicateEffect(ChaosPie p) {
		plugin = p;
	}
	
	@Override
	public void start() {
		MessageChannel.TO_PLAYERS.send(Text.of(TextColors.RED, "Duplicate hostiles!"));
	}

	@Override
	public void stop() {
		MessageChannel.TO_PLAYERS.send(Text.of(TextColors.GREEN, "No more duplicates. Phew."));
	}

	@Override
	public int lengthInSeconds() {
		return 120;
	}

	@Override
	public int getWeight() {
		return 30;
	}
	
	@Listener
	public void onEntityDeath(DestructEntityEvent.Death e, @First EntityDamageSource src) {
		plugin.getLogger().info(e.getCause().toString());
		if (!(e.getTargetEntity() instanceof Hostile)) return;
		
		Living target = e.getTargetEntity();		
		World w = target.getWorld();
		Entity copy1 = w.createEntity(target.getType(), target.getLocation().getPosition());
		Entity copy2 = w.createEntity(target.getType(), target.getLocation().getPosition());
		Sponge.getCauseStackManager().addContext(EventContextKeys.SPAWN_TYPE, SpawnTypes.PLUGIN);
//		SpawnCause c = SpawnCause.builder().type(SpawnTypes.PLUGIN).build();
//		
//		w.spawnEntity(copy1, Cause.source(c).build());
//		w.spawnEntity(copy2, Cause.source(c).build());
		w.spawnEntity(copy1);
		w.spawnEntity(copy2);
	}

}
