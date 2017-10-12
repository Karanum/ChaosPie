package eu.teamtime.chaospie.effects;

import java.util.concurrent.TimeUnit;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.BrickType;
import org.spongepowered.api.data.type.BrickTypes;
import org.spongepowered.api.data.type.SlabTypes;
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
 * Represents the "Corrosion" chaos effect, which when activated causes
 * various blocks around all players to gradually deform and wither away.
 * 
 * @author TheMightyDozen
 */
public class CorrosionEffect extends ChaosEffectBase {
	
	private Task corroder;
	
	@Override
	public void start() {
		MessageChannel.TO_PLAYERS.send(Text.of(TextColors.GRAY, "Oh no, everything's turning to dust!"));

		corroder = Task.builder()
				.execute(this::dirtify)
				.interval(1, TimeUnit.SECONDS)
				.name("ChaosPie-CorrosionEffect-Corroder")
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
						BlockType type = location.getBlockType();
						
						// TODO two things:
						// 1) isn't there supposed to be a way to add a ``Cause`` to this?
						// 2) add more blocks...
						if (type.equals(BlockTypes.STONE))
							location.setBlockType(BlockTypes.COBBLESTONE);

						else if (type.equals(BlockTypes.STONE_SLAB)
								 || type.equals(BlockTypes.STONE_SLAB2)
								 || type.equals(BlockTypes.WOODEN_SLAB)
								 || type.equals(BlockTypes.PURPUR_SLAB)
								 || type.equals(BlockTypes.LEAVES)
								 || type.equals(BlockTypes.LEAVES2))
							location.setBlockType(BlockTypes.AIR);

						else if (type.equals(BlockTypes.WHEAT)
								 || type.equals(BlockTypes.CARROTS)
								 || type.equals(BlockTypes.POTATOES)
								 || type.equals(BlockTypes.BEETROOTS))
							location.setBlockType(BlockTypes.TALLGRASS);

						else if (type.equals(BlockTypes.MELON_STEM)
								 || type.equals(BlockTypes.PUMPKIN_STEM))
							location.setBlockType(BlockTypes.TALLGRASS);

						else if (type.equals(BlockTypes.STONEBRICK)) {
							switch (player.getRandom().nextInt(3)) {
							case 0:
								location.setBlockType(BlockTypes.STONE_BRICK_STAIRS);
							case 1:
								location.setBlock(BlockTypes.STONE_SLAB.getDefaultState()
										.with(Keys.SLAB_TYPE, SlabTypes.BRICK).get());
							case 2:
								BrickType brickType = location.require(Keys.BRICK_TYPE);
								if (brickType.equals(BrickTypes.DEFAULT)
									|| brickType.equals(BrickTypes.CHISELED)) {
									if (player.getRandom().nextBoolean()) {
										location.setBlock(BlockTypes.STONEBRICK.getDefaultState()
												.with(Keys.BRICK_TYPE, BrickTypes.CRACKED).get());
									} else {
										location.setBlock(BlockTypes.STONEBRICK.getDefaultState()
												.with(Keys.BRICK_TYPE, BrickTypes.MOSSY).get());
									}
								} else {
									location.setBlockType(BlockTypes.AIR);
								}
							}
						} else if (type.equals(BlockTypes.GRASS)) {
							switch (player.getRandom().nextInt(3)) {
							case 0:
								location.setBlockType(BlockTypes.DIRT);
							case 1:
								location.setBlockType(BlockTypes.GRASS_PATH);
							case 2:
								location.setBlockType(BlockTypes.MYCELIUM);
							}
						} else if (type.equals(BlockTypes.GRASS_PATH)
								   || type.equals(BlockTypes.MYCELIUM)) {
							location.setBlockType(BlockTypes.DIRT);
						}				
					});
		}
	}

	@Override
	public void stop() {
		MessageChannel.TO_PLAYERS.send(Text.of(TextColors.GREEN, "Alright, I think it stopped."));
		corroder.cancel();
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

	@Override
	public String getName() {
		return "Corrosion";
	}
}