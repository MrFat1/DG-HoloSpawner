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

        if (args.length == 0) {
            p.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">> " + ChatColor.RED + "Sintaxis incorrecta, utiliza /dghs help");
            return false;
        } else {
            if (p.hasPermission("dgholospawner.*")) {

                switch (args[0]) {
                    case "create":
                        if (args.length >= 3) {

                            this.crearHolo(p,args);

                        } else {
                            p.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">> " + ChatColor.RED + "Sintaxis incorrecta, utiliza /dghs create <Spawner> <Nombre del mob con colores>");
                        }

                        break;

                    case "delete":

                        if (args.length >=2) {
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
                                p.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">> " + ChatColor.RED + "No se ha encontrado ningún holograma, utiliza /dghs list");
                            }
                        } else {
                            p.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">> " + ChatColor.RED + "Sintaxis incorrecta, utiliza /dghs delete <nombre del spawner>");
                        }


                    case "tphere":
                        if (args.length >=2) {
                            if (main.listholos.keySet().contains(args[1])) {
                                Hologram hologramaloc2 = main.listholos.get(args[1]);
                                p.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + ">> " + ChatColor.WHITE + "Tepeaste a tu posición el holograma: " + ChatColor.YELLOW + args[1]);
                                hologramaloc2.teleport(p.getLocation());

                                try {
                                    main.registerLoc("hologramas.yml", args[1] + ".loc", p.getLocation());

                                } catch (Exception e) {
                                    Bukkit.getConsoleSender().sendMessage("[DG-HoloSpawners] " + ChatColor.DARK_RED + "Error al editar hologramas.yml");
                                }
                            } else {
                                p.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">> " + ChatColor.RED + "No se ha encontrado ningún holograma, utiliza /dghs list");
                            }
                        } else {
                            p.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">> " + ChatColor.RED + "Sintaxis incorrecta, utiliza /dghs tphere <nombre del spawner>");
                        }
                        break;

                    case "list":
                        if (args.length == 1) {
                            if (main.listholos.isEmpty()) {
                                p.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + ">> " + ChatColor.WHITE + "Actualmente no hay hologramas creados");
                            } else {
                                p.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + ">> " + ChatColor.WHITE + "Lista de Hologramas creados: " + ChatColor.YELLOW + main.listholos.keySet());
                            }
                        } else {
                            p.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">> " + ChatColor.RED + "Sintaxis incorrecta, utiliza /dghs list");
                        }
                        break;

                    case "help":
                        p.sendMessage(ChatColor.GOLD + "------------------------------------------");
                        p.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "                 COMANDOS");
                        p.sendMessage("§7");
                        p.sendMessage(ChatColor.YELLOW + "/dghs create: " + ChatColor.WHITE + "comando para crear hologramas. " + ChatColor.ITALIC.toString() + ChatColor.GRAY + "/dghs create <spawner> <nombre del mob con colores>");
                        p.sendMessage("§7");
                        p.sendMessage(ChatColor.YELLOW + "/dghs delete: " + ChatColor.WHITE + "comando para borrar hologramas. " + ChatColor.ITALIC.toString() + ChatColor.GRAY + "/dghs delete <nombre del holograma>");
                        p.sendMessage("§7");
                        p.sendMessage(ChatColor.YELLOW + "/dghs tphere: " + ChatColor.WHITE + "comando para traerte hologramas. " + ChatColor.ITALIC.toString() + ChatColor.GRAY + "/dghs tphere <nombre del holograma>");
                        p.sendMessage("§7");
                        p.sendMessage(ChatColor.YELLOW + "/dghs list: " + ChatColor.WHITE + "comando para ver la lista de hologramas.");
                        p.sendMessage("§7");
                        p.sendMessage(ChatColor.YELLOW + "/dghs help: " + ChatColor.WHITE + "comando para ver esto.");
                        p.sendMessage("§7");
                        p.sendMessage(ChatColor.GOLD + "------------------------------------------");

                        break;

                    default:
                        p.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">> " + ChatColor.RED + "Ese comando no existe.");
                }

            } else {
                p.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">> " + ChatColor.RED + "No tienes permiso para usar este comando!");
            }
        }



        return false;
    }

    private void crearHolo(Player p, String[] args) {

        if (MythicBukkit.inst().getSpawnerManager().getSpawnerByName(args[1]) != null) {

            StringBuilder sb = new StringBuilder();

            for (int i = 2; i<args.length; i++) {
                sb.append(args[i]).append(" ");
            }

            String nombremobbonito = translateAlternateColorCodes('&', sb.toString());

            MythicSpawner ms = MythicBukkit.inst().getSpawnerManager().getSpawnerByName(args[1]);

            Hologram holo = HologramsAPI.createHologram(Bukkit.getPluginManager().getPlugin("DG-HoloSpawner"), p.getLocation().add(0, 2, 0));
            String nombreholo = args[1];

            if (main.listholos.containsKey(nombreholo)) {
                p.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">> " + ChatColor.RED + "Ya existe un holograma para ese spawner");

            } else {

                main.listholos.put(nombreholo, holo);
                main.crearHolograma(holo, ms, nombremobbonito);

                try {
                    main.editFile("hologramas.yml", nombreholo + ".nombre", nombremobbonito);
                    main.editFile("hologramas.yml", nombreholo + ".spawner", args[1]);
                    main.registerLoc("hologramas.yml", nombreholo + ".loc", p.getLocation().add(0, 2, 0));

                } catch (Exception e) {
                    Bukkit.getConsoleSender().sendMessage("[DG-HoloSpawners] " + ChatColor.DARK_RED + "Error al editar hologramas.yml");
                }

                p.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + ">> " + ChatColor.WHITE + "Se ha generado un holograma para el spawner " + ChatColor.GREEN + args[1] + ChatColor.WHITE + " en tu localización.");
            }


        } else {
            p.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">> " + ChatColor.RED + "Este spawner no existe!");
        }
    }
}
