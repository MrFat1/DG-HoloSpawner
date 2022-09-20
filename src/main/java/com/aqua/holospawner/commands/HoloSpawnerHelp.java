package com.aqua.holospawner.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HoloSpawnerHelp implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        Player p = (Player) sender;

        if (p.hasPermission("dgholospawner.help")) {
            p.sendMessage(ChatColor.GOLD + "------------------------------------------");
            p.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "                 COMANDOS");
            p.sendMessage("§7");
            p.sendMessage(ChatColor.YELLOW + "/dghs: " + ChatColor.WHITE + "comando para crear hologramas. " + ChatColor.ITALIC.toString() + ChatColor.GRAY + "/dghs <spawner> <nombre del mob con colores>");
            p.sendMessage("§7");
            p.sendMessage(ChatColor.YELLOW + "/dghe: " + ChatColor.WHITE + "comando para editar hologramas. " + ChatColor.ITALIC.toString() + ChatColor.GRAY + "/dghe <tphere/delete> <nombre del holograma>");
            p.sendMessage("§7");
            p.sendMessage(ChatColor.YELLOW + "/dghe list: " + ChatColor.WHITE + "comando para la lista de hologramas.");
            p.sendMessage("§7");
            p.sendMessage(ChatColor.GOLD + "------------------------------------------");
        } else {
            p.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">> " + ChatColor.RED + "No tienes permiso para usar este comando!");
        }

        return false;
    }
}
