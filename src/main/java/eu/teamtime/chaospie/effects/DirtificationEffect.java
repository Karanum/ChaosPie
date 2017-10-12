package eu.teamtime.chaospie.effects;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.blockray.BlockRay;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;

import eu.teamtime.chaospie.ChaosEffectBase;
import eu.teamtime.chaospie.ChaosPie;

/**
 * Represents the "Dirtification" chaos effect, which when activated causes
 * house-like blocks around all players to gradually turn to dirt.
 * 
 * @author TheMightyDozen
 */
public class DirtificationEffect extends ChaosEffectBase {
	
	private static final Collection<BlockType> DIRTY_BLOCKS = Arrays.asList // TODO feel free to add more blocks as wanted
	(
		BlockTypes.PLANKS, BlockTypes.BRICK_BLOCK, BlockTypes.STONEBRICK, BlockTypes.DOUBLE_STONE_SLAB,
		BlockTypes.DOUBLE_STONE_SLAB2, BlockTypes.DOUBLE_WOODEN_SLAB, BlockTypes.HARDENED_CLAY,
		BlockTypes.COBBLESTONE, BlockTypes.COBBLESTONE_WALL, BlockTypes.MOSSY_COBBLESTONE, BlockTypes.WOOL,
		BlockTypes.QUARTZ_BLOCK
	);

	private Task dirtifier;
	
	@Override
	public void start() {
		MessageChannel.TO_PLAYERS.send(Text.of(TextColors.GOLD, "Aw jeez, the house is so dirty!"));

		dirtifier = Task.builder()
				.execute(this::dirtify)
				.interval(1, TimeUnit.SECONDS)
				.name("ChaosPie-DirtificationEffect-Dirtifier")
				.submit(ChaosPie.instance());
	}
	
	private void dirtify() {
		for (Player player : Sponge.getServer().getOnlinePlayers()) {
			BlockRay.from(player)
					.direction(Vector3d.createRandomDirection(player.getRandom()))
					.distanceLimit(8)
					.build()
					.end()
					.ifPresent(hit -> {
						Location<World> location = hit.getLocation();
						if (DIRTY_BLOCKS.contains(location.getBlockType())) {
							// TODO isn't there supposed to be a way to add a ``Cause`` to this?
							location.setBlockType(BlockTypes.DIRT);
						}
					});
		}
	}

	@Override
	public void stop() {
		MessageChannel.TO_PLAYERS.send(Text.of(TextColors.GREEN, "It's okay, the dirt has stopped now."));
		dirtifier.cancel();
	}

	@Override
	public int lengthInSeconds() {
		// TODO unspecified effect duration
		return 120;
	}

	@Override
	public int getWeight() {
		return 30;
	}
}