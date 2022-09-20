package com.aqua.holospawner.commands;

import com.aqua.holospawner.Main;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.spawning.spawners.MythicSpawner;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.translateAlternateColorCodes;


public class SpawnerCommand implements CommandExecutor {

    private Main main;

    public SpawnerCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;

        if (p.hasPermission("dgholospawner.create")) {

            if (args.length >= 2) {

                if (MythicBukkit.inst().getSpawnerManager().getSpawnerByName(args[0]) != null) {

                    StringBuilder sb = new StringBuilder();

                    for (int i = 1; i<args.length; i++) {
                        sb.append(args[i]).append(" ");
                    }

                    String nombremobbonito = translateAlternateColorCodes('&', sb.toString());

                    MythicSpawner ms = MythicBukkit.inst().getSpawnerManager().getSpawnerByName(args[0]);

                    Hologram holo = HologramsAPI.createHologram(Bukkit.getPluginManager().getPlugin("DG-HoloSpawner"), p.getLocation().add(0, 2, 0));
                    String nombreholo = args[0];

                    if (main.listholos.containsKey(nombreholo)) {
                        p.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">> " + ChatColor.RED + "Ya existe un holograma para ese spawner");

                    } else {

                        main.listholos.put(nombreholo, holo);
                        main.crearHolograma(holo, ms, nombremobbonito);

                        try {
                            main.editFile("hologramas.yml", nombreholo + ".nombre", nombremobbonito);
                            main.editFile("hologramas.yml", nombreholo + ".spawner", args[0]);
                            main.registerLoc("hologramas.yml", nombreholo + ".loc", p.getLocation().add(0, 2, 0));

                        } catch (Exception e) {
                            Bukkit.getConsoleSender().sendMessage("[DG-HoloSpawners] " + ChatColor.DARK_RED + "Error al editar hologramas.yml");
                        }

                        p.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + ">> " + ChatColor.WHITE + "Se ha generado un holograma para el spawner " + ChatColor.GREEN + args[0] + ChatColor.WHITE + " en tu localización.");
                    }


                } else {
                    p.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">> " + ChatColor.RED + "Este spawner no existe!");
                }

            } else {
                p.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">> " + ChatColor.RED + "Sintaxis incorrecta, utiliza /dghs <Spawner> <Nombre del mob con colores>");
            }
        } else {
            p.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">> " + ChatColor.RED + "No tienes permiso para usar este comando!");
        }

        return false;
    }
}
