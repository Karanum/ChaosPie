package eu.teamtime.chaospie.effects;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.Hostile;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;

import eu.teamtime.chaospie.ChaosEffectBase;
import eu.teamtime.chaospie.ChaosPie;

/**
 * Represents the "Hostile Duplication" chaos effect, which when activated
 * causes any hostile mob slain by a player to spawn 2 copies of itself.
 * 
 * @author Dennis
 */
public class HostileDuplicateEffect extends ChaosEffectBase {

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
	
	@Override
	public String getName() {
		return "Duplicate Hostiles";
	}
	
	@Listener
	public void onEntityDeath(DestructEntityEvent.Death e, @First EntityDamageSource source, @Getter("getTargetEntity") Hostile target) {
		if (!(source.getSource() instanceof Player)) return;
		ChaosPie.instance().getLogger().info(e.getCause().toString());

		Sponge.getCauseStackManager().addContext(EventContextKeys.SPAWN_TYPE, SpawnTypes.PLUGIN);
		World w = target.getWorld();
		w.spawnEntity(w.createEntity(target.getType(), target.getLocation().getPosition()));
		w.spawnEntity(w.createEntity(target.getType(), target.getLocation().getPosition()));
	}
}