package eu.teamtime.chaospie.effects;

import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.DamageTypes;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColors;

import com.flowpowered.math.vector.Vector3d;

import eu.teamtime.chaospie.ChaosEffectBase;

/**
 * Represents the "Helium Punch" chaos effect, which when activated makes it so
 * whenever any living entity damages another entity, the target entity gets
 * launched into the air.
 * 
 * @author Dennis
 */
public class HeliumPunchEffect extends ChaosEffectBase {

	@Override
	public void start() {
		MessageChannel.TO_PLAYERS.send(Text.of(TextColors.RED, "HELIUM TIME!"));
	}

	@Override
	public void stop() {
		MessageChannel.TO_PLAYERS.send(Text.of(TextColors.GREEN, "No moar helium. Phew."));
	}

	@Override
	public int lengthInSeconds() {
		return 30;
	}

	@Override
	public int getWeight() {
		return 30;
	}

	@Listener
	public void onAttackEntity(DamageEntityEvent e, @First EntityDamageSource source, @Getter("getTargetEntity") Living target) {
		if (source.getType().equals(DamageTypes.ATTACK)
			|| source.getType().equals(DamageTypes.PROJECTILE)) { // TODO maybe also add ``DamageTypes.CONTACT``?
			Vector3d v = target.getVelocity();
			target.setVelocity(new Vector3d(v.getX(), 2, v.getZ()));
		}
	}
}