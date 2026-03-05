package com.mosesuzozie.kitmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.Collection;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class KitGiveCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("kitgive")
            .then(argument("targets", EntityArgumentType.players())
            .then(argument("kit", StringArgumentType.word())
            .executes(context -> {
                Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "targets");
                String kitName = StringArgumentType.getString(context, "kit");

                for (ServerPlayerEntity player : players) {
                    giveKit(player, kitName);
                }

                context.getSource().sendFeedback(Text.of("Kit " + kitName + " given!"), false);
                return 1;
            })))));
    }

    private static void giveKit(ServerPlayerEntity player, String kitName) {
        switch (kitName.toLowerCase()) {
            case "pvp":
                // Armor
                player.getInventory().armor.set(3, new ItemStack(Items.NETHERITE_HELMET).enchant(Enchantments.PROTECTION,4).enchant(Enchantments.UNBREAKING,3).enchant(Enchantments.MENDING,1));
                player.getInventory().armor.set(2, new ItemStack(Items.NETHERITE_CHESTPLATE).enchant(Enchantments.PROTECTION,4).enchant(Enchantments.UNBREAKING,3).enchant(Enchantments.MENDING,1));
                player.getInventory().armor.set(1, new ItemStack(Items.NETHERITE_LEGGINGS).enchant(Enchantments.PROTECTION,4).enchant(Enchantments.UNBREAKING,3).enchant(Enchantments.MENDING,1));
                player.getInventory().armor.set(0, new ItemStack(Items.NETHERITE_BOOTS).enchant(Enchantments.PROTECTION,4).enchant(Enchantments.UNBREAKING,3).enchant(Enchantments.MENDING,1));

                // Sword
                ItemStack sword = new ItemStack(Items.NETHERITE_SWORD);
                sword.enchant(Enchantments.SHARPNESS,5);
                sword.enchant(Enchantments.MENDING,1);
                sword.enchant(Enchantments.UNBREAKING,3);
                sword.enchant(Enchantments.LOOTING,3);
                sword.enchant(Enchantments.FIRE_ASPECT,2);
                sword.enchant(Enchantments.KNOCKBACK,2);
                player.getInventory().insertStack(sword);

                // Elytra
                ItemStack elytra = new ItemStack(Items.ELYTRA);
                elytra.enchant(Enchantments.MENDING,1);
                elytra.enchant(Enchantments.UNBREAKING,3);
                player.getInventory().insertStack(elytra);

                // Consumables
                player.getInventory().insertStack(new ItemStack(Items.GOLDEN_APPLE,64));
                player.getInventory().insertStack(new ItemStack(Items.ENDER_PEARL,16));
                player.getInventory().insertStack(new ItemStack(Items.TOTEM_OF_UNDYING,1));

                // TODO: Add custom items (Maces, Wind Charge, Breach, Density) here
                break;

            default:
                player.sendMessage(Text.of("Unknown kit: " + kitName), false);
                break;
        }
    }
}