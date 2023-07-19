package net.larsmans.infinitybuttons.block.custom.letterbutton;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.larsmans.infinitybuttons.InfinityButtonsUtil;
import net.larsmans.infinitybuttons.block.custom.button.AbstractLeverableButton;
import net.larsmans.infinitybuttons.block.custom.button.LargeButtonShape;
import net.larsmans.infinitybuttons.block.custom.letterbutton.gui.LetterButtonGui;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class LetterButton extends AbstractLeverableButton {

    public static final EnumProperty<LetterButtonEnum> CHARACTER = EnumProperty.of("character", LetterButtonEnum.class);

    public LetterButton(FabricBlockSettings settings, boolean lever) {
        super(lever, settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(PRESSED, false).with(FACE, WallMountLocation.FLOOR).with(CHARACTER, LetterButtonEnum.NONE));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ClientPlayNetworkHandler connection = MinecraftClient.getInstance().getNetworkHandler();
        assert connection != null;
        GameMode gameMode = Objects.requireNonNull(connection.getPlayerListEntry(player.getGameProfile().getId())).getGameMode();
        if (player.isSneaking()) {
            if (gameMode == GameMode.ADVENTURE) {
                return super.onUse(state, world, pos, player, hand, hit);
            }
            if (world.isClient) {
                MinecraftClient.getInstance().setScreen(new LetterButtonGui(this, state, world, pos));
            }
            return ActionResult.success(world.isClient);
        } else {
            return super.onUse(state, world, pos, player, hand, hit);
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return LargeButtonShape.outlineShape(state);
    }

    @Override
    public int getPressTicks() {
        return 30;
    }

    @Override
    protected SoundEvent getClickSound(boolean pressed) {
        return SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON;
    }

    public int getEnumId(BlockState state) {
        return state.get(CHARACTER).ordinal();
    }

    public void setState(BlockState state, World world, BlockPos pos, LetterButtonEnum buttonEnum) {
        world.setBlockState(pos, state.with(CHARACTER, buttonEnum), 3);
        this.updateNeighbors(state, world, pos);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(CHARACTER);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        InfinityButtonsUtil.tooltip(tooltip, "letter_button");
    }
}
