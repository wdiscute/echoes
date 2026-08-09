package com.wdiscute.echoes.blocks.portal;

import com.mojang.serialization.MapCodec;
import com.wdiscute.echoes.registry.ECBlockEntities;
import com.wdiscute.utils.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;


public class PortalBlock extends HorizontalDirectionalBlock implements EntityBlock, SculkBehaviour
{
    public static final EnumProperty<State> STATE = EnumProperty.create("state", State.class);
    private static final VoxelShape SHAPE_EMPTY = Block.column(16.0, 0.0, 13.0);
    private static final VoxelShape SHAPE_FULL = Shapes.or(SHAPE_EMPTY, Block.column(8.0, 13.0, 16.0));

    public PortalBlock(Properties properties)
    {
        super(properties);
    }

    public enum State implements StringRepresentable
    {
        CLOSED("closed"),
        OPEN("open"),
        LOOTING("looting");

        final String key;

        State(String key)
        {
            this.key = key;
        }

        @Override
        public String getSerializedName()
        {
            return key;
        }
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(STATE, State.OPEN);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(HorizontalDirectionalBlock.FACING);
        builder.add(STATE);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec()
    {
        return null;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return state.getValue(STATE).equals(State.OPEN) ? SHAPE_FULL : SHAPE_EMPTY;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        if (itemStack.is(Items.ECHO_SHARD.asItem()) && state.getValue(STATE).equals(State.CLOSED))
        {
            itemStack.consume(1, player);

            level.setBlockAndUpdate(pos, state.trySetValue(STATE, State.OPEN));

            if (!level.isClientSide())
                level.playSound(null, pos, SoundEvents.BEACON_ACTIVATE,  SoundSource.BLOCKS);

            return InteractionResult.SUCCESS;
        }

        return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type)
    {
        return TickableBlockEntity.getTicketHelper(level);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState)
    {
        return ECBlockEntities.PORTAL.get().create(worldPosition, blockState);
    }

    @Override
    public int attemptUseCharge(
            SculkSpreader.ChargeCursor cursor, LevelAccessor level, BlockPos originPos, RandomSource random, SculkSpreader spreader, boolean spreadVein
    )
    {
        int charge = cursor.getCharge();
        if (charge != 0 && random.nextInt(spreader.chargeDecayRate()) == 0)
        {
            BlockPos chargePos = cursor.getPos();
            boolean isCloseToCatalyst = chargePos.closerThan(originPos, spreader.noGrowthRadius());
            if (!isCloseToCatalyst && canPlaceGrowth(level, chargePos))
            {
                int xpPerGrowthSpawn = spreader.growthSpawnCost();
                if (random.nextInt(xpPerGrowthSpawn) < charge)
                {
                    BlockPos growthPlacement = chargePos.above();
                    BlockState growthState = this.getRandomGrowthState(level, growthPlacement, random, spreader.isWorldGeneration());
                    level.setBlock(growthPlacement, growthState, 3);
                    level.playSound(null, chargePos, growthState.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
                }

                return Math.max(0, charge - xpPerGrowthSpawn);
            }
            else
            {
                return random.nextInt(spreader.additionalDecayRate()) != 0
                        ? charge
                        : charge - (isCloseToCatalyst ? 1 : getDecayPenalty(spreader, chargePos, originPos, charge));
            }
        }
        else
        {
            return charge;
        }
    }

    private static boolean canPlaceGrowth(LevelAccessor level, BlockPos pos)
    {
        BlockState stateAbove = level.getBlockState(pos.above());
        if (stateAbove.isAir() || stateAbove.is(Blocks.WATER) && stateAbove.getFluidState().is(Fluids.WATER))
        {
            int growthCount = 0;

            for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-4, 0, -4), pos.offset(4, 2, 4)))
            {
                BlockState state = level.getBlockState(blockPos);
                if (state.is(Blocks.SCULK_SENSOR) || state.is(Blocks.SCULK_SHRIEKER))
                {
                    growthCount++;
                }

                if (growthCount > 2)
                {
                    return false;
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    private BlockState getRandomGrowthState(LevelAccessor level, BlockPos pos, RandomSource random, boolean isWorldGen)
    {
        BlockState state;
        if (random.nextInt(11) == 0)
        {
            state = Blocks.SCULK_SHRIEKER.defaultBlockState().setValue(SculkShriekerBlock.CAN_SUMMON, isWorldGen);
        }
        else
        {
            state = Blocks.SCULK_SENSOR.defaultBlockState();
        }

        return state.hasProperty(BlockStateProperties.WATERLOGGED) && !level.getFluidState(pos).isEmpty()
                ? state.setValue(BlockStateProperties.WATERLOGGED, true)
                : state;
    }

    private static int getDecayPenalty(SculkSpreader spreader, BlockPos pos, BlockPos originPos, int charge)
    {
        int noGrowthRadius = spreader.noGrowthRadius();
        float outerDistanceSquared = Mth.square((float) Math.sqrt(pos.distSqr(originPos)) - noGrowthRadius);
        int maxReachSquared = Mth.square(24 - noGrowthRadius);
        float distanceFactor = Math.min(1.0F, outerDistanceSquared / maxReachSquared);
        return Math.max(1, (int) (charge * distanceFactor * 0.5F));
    }
}
