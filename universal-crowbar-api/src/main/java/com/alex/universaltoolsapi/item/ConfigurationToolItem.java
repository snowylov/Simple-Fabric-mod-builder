package com.alex.universaltoolsapi.item;

import com.alex.universaltoolsapi.UniversalToolsApiMod;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public final class ConfigurationToolItem extends Item {
    private static final Map<UUID, BlockState> COPIED_STATES = new ConcurrentHashMap<>();
    private final boolean activeMode;

    public ConfigurationToolItem(Properties properties, boolean activeMode) {
        super(properties);
        this.activeMode = activeMode;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            Item target = activeMode
                    ? UniversalToolsApiMod.CONFIGURATION_TOOL
                    : UniversalToolsApiMod.CONFIGURATION_TOOL_ACTIVE;
            player.setItemInHand(hand, new ItemStack(target));
            player.displayClientMessage(
                    Component.literal(activeMode ? "Configuration Tool: rotate/reset mode" : "Configuration Tool: copy/edit mode"),
                    true
            );
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) {
            return InteractionResult.PASS;
        }

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        BlockState result = state;

        if (activeMode) {
            if (player.isShiftKeyDown()) {
                COPIED_STATES.put(player.getUUID(), state);
                player.displayClientMessage(Component.literal("Copied state: " + describe(state)), true);
                return InteractionResult.SUCCESS;
            }

            BlockState copied = COPIED_STATES.get(player.getUUID());
            if (copied != null) {
                result = pasteCompatibleProperties(copied, state);
                if (!result.equals(state)) {
                    if (!level.isClientSide()) {
                        level.setBlockAndUpdate(pos, result);
                    }
                    player.displayClientMessage(Component.literal("Pasted compatible state: " + describe(result)), true);
                    return InteractionResult.SUCCESS;
                }
            }

            result = cycleFirstProperty(state, false);
            if (result.equals(state)) {
                player.displayClientMessage(Component.literal("No editable block-state properties"), true);
                return InteractionResult.PASS;
            }
            if (!level.isClientSide()) {
                level.setBlockAndUpdate(pos, result);
            }
            player.displayClientMessage(Component.literal("Cycled state: " + describe(result)), true);
            return InteractionResult.SUCCESS;
        }

        if (player.isShiftKeyDown()) {
            result = state.getBlock().defaultBlockState();
            if (state.hasProperty(BlockStateProperties.WATERLOGGED) && result.hasProperty(BlockStateProperties.WATERLOGGED)) {
                result = result.setValue(
                        BlockStateProperties.WATERLOGGED,
                        state.getValue(BlockStateProperties.WATERLOGGED)
                );
            }
            if (!level.isClientSide()) {
                level.setBlockAndUpdate(pos, result);
            }
            player.displayClientMessage(Component.literal("Reset state: " + describe(result)), true);
            return InteractionResult.SUCCESS;
        }

        result = state.rotate(Rotation.CLOCKWISE_90);
        if (result.equals(state)) {
            result = cycleFirstProperty(state, false);
        }
        if (result.equals(state)) {
            player.displayClientMessage(Component.literal("State: " + describe(state)), true);
            return InteractionResult.PASS;
        }
        if (!level.isClientSide()) {
            level.setBlockAndUpdate(pos, result);
        }
        player.displayClientMessage(Component.literal("Configured state: " + describe(result)), true);
        return InteractionResult.SUCCESS;
    }

    private static BlockState cycleFirstProperty(BlockState state, boolean reverse) {
        List<Property<?>> properties = new ArrayList<>(state.getProperties());
        properties.sort(Comparator.comparing(Property::getName));
        if (properties.isEmpty()) {
            return state;
        }
        return cyclePropertyUnchecked(state, properties.get(0), reverse);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static BlockState cyclePropertyUnchecked(BlockState state, Property property, boolean reverse) {
        List<Comparable> values = new ArrayList<>(property.getPossibleValues());
        if (values.size() < 2) {
            return state;
        }
        Comparable current = (Comparable) state.getValue(property);
        int index = values.indexOf(current);
        int nextIndex = Math.floorMod(index + (reverse ? -1 : 1), values.size());
        return state.setValue(property, values.get(nextIndex));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static BlockState pasteCompatibleProperties(BlockState source, BlockState target) {
        BlockState result = target;
        for (Property targetProperty : target.getProperties()) {
            Property sourceProperty = source.getProperties().stream()
                    .filter(property -> property.getName().equals(targetProperty.getName()))
                    .findFirst()
                    .orElse(null);
            if (sourceProperty == null) {
                continue;
            }

            Comparable value = (Comparable) source.getValue(sourceProperty);
            if (targetProperty.getPossibleValues().contains(value)) {
                result = result.setValue(targetProperty, value);
            }
        }
        return result;
    }

    private static String describe(BlockState state) {
        if (state.getProperties().isEmpty()) {
            return state.getBlock().getName().getString();
        }
        String properties = state.getProperties().stream()
                .sorted(Comparator.comparing(Property::getName))
                .map(property -> property.getName() + "=" + valueName(state, property))
                .collect(Collectors.joining(", "));
        return state.getBlock().getName().getString() + " [" + properties + "]";
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static String valueName(BlockState state, Property property) {
        Comparable value = (Comparable) state.getValue(property);
        return property.getName(value);
    }
}
