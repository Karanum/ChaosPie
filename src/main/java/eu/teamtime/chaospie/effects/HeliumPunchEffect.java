package eu.teamtime.chaospie.effects;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Agent;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.entity.AttackEntityEvent;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColors;

import com.flowpowered.math.vector.Vector3d;

import eu.teamtime.chaospie.ChaosPie;
import eu.teamtime.chaospie.ChaosEffectBase;

public class HeliumPunchEffect extends ChaosEffectBase {

	private ChaosPie plugin;
	
	public HeliumPunchEffect(ChaosPie p) {
		plugin = p;
	}
	
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
	public void onAttackEntity(DamageEntityEvent e, @First EntityDamageSource source) {
		Entity target = e.getTargetEntity();
		if (!(target instanceof Living)) return;
		
		Task.builder()
				.execute(() -> {
					Vector3d v = target.getVelocity();
					target.setVelocity(new Vector3d(v.getX(), 2, v.getZ()));
				})
				.submit(plugin);
	}

}
