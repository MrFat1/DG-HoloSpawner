package com.aqua.holospawner.commands;

import com.aqua.holospawner.Main;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnerEdit implements CommandExecutor {

    //dgspawneredit <tp/delete>

    private Main main;

    public SpawnerEdit(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        Player p = (Player) commandSender; //Downcasting

        if (p.hasPermission("dgholospawner.edit")) {

            if (args.length >= 2) { //Si tiene tp/delete + nombre

                if (args[0].equalsIgnoreCase("delete")) {

                    if (main.listholos.keySet().contains(args[1])) {
                        Hologram hologramaloc = main.listholos.get(args[1]);
                        p.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">> " + ChatColor.RED + "Borraste el holograma: " + ChatColor.YELLOW + args[1]);
                        hologramaloc.delete();
                        main.listholos.remove(args[1], hologramaloc);


                        try {
                            main.editFile("hologramas.yml", args[1], null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else {
                        p.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">> " + ChatColor.RED + "No se ha encontrado ningún holograma, utiliza /dghe list");
                    }

                } else if (args[0].equalsIgnoreCase("tphere")) {

                    if (main.listholos.keySet().contains(args[1])) {
                        Hologram hologramaloc2 = main.listholos.get(args[1]);
                        p.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + ">> " + ChatColor.WHITE + "Tepeaste a tu posición el holograma: " + ChatColor.YELLOW + args[1]);
                        hologramaloc2.teleport(p.getLocation());

                        try {
                            main.registerLoc("hologramas.yml", args[1] + ".loc", p.getLocation());

                        } catch (Exception e) {
                            Bukkit.getConsoleSender().sendMessage("[DG-HoloSpawners] " + ChatColor.DARK_RED + "Error al editar hologramas.yml");
                        }
                    }

                } else {
                    p.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">> " + ChatColor.RED + "Sintaxis incorrecta, utiliza /dghe <tphere/delete> <nombre del holograma>");
                }

            } else if (args.length == 1) {

                if (args[0].equalsIgnoreCase("list")) {

                    if (main.listholos.isEmpty()) {
                        p.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + ">> " + ChatColor.WHITE + "Actualmente no hay hologramas creados");
                    } else {
                        p.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + ">> " + ChatColor.WHITE + "Lista de Hologramas creados: " + ChatColor.YELLOW + main.listholos.keySet());
                    }

                } else {
                    p.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">> " + ChatColor.RED + "Sintaxis incorrecta, utiliza /dghe list");
                }

            } else {
                p.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">> " + ChatColor.RED + "Sintaxis incorrecta, utiliza /dghe <tphere/delete/list> <nombre del holograma>");
            }

        } else {
            p.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">> " + ChatColor.RED + "No tienes permiso para usar este comando!");
        }

        return false;
    }
}
