package com.aqua.holospawner;

import com.aqua.holospawner.commands.SpawnerCommand;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.spawning.spawners.MythicSpawner;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.HashMap;

public class Main extends JavaPlugin implements Listener {

    public HashMap<String, Hologram> listholos = new HashMap<>(); //key, value


    @Override
    public void onEnable() {

        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
            getLogger().severe("*** HolographicDisplays no esta instalado en el server o esta desactivado. ***");
            getLogger().severe("*** El plugin DG-HoloSpawners se deshabilitará. ***");
            this.setEnabled(false);
            return;
        } else {
            Bukkit.getConsoleSender().sendMessage("[DG-HoloSpawners] " + ChatColor.GREEN + "Se ha encontrado el plugin HolographicDisplays");
        }

        if (!Bukkit.getPluginManager().isPluginEnabled("MythicMobs")) {
            getLogger().severe("*** MythicMobs no esta instalado en el server o esta desactivado. ***");
            getLogger().severe("*** El plugin DG-HoloSpawners se deshabilitará. ***");
            this.setEnabled(false);
            return;
        } else {
            Bukkit.getConsoleSender().sendMessage("[DG-HoloSpawners] " + ChatColor.GREEN + "Se ha encontrado el plugin Mythicmobs");
        }

        getCommand("dgholospawner").setExecutor(new SpawnerCommand(this));

        Bukkit.getPluginManager().registerEvents(this, this);

        try {
            initiateFIle("hologramas.yml");
        } catch (Exception e) {
            e.printStackTrace();
        }

        generarHolos("hologramas.yml");

    }

    public void initiateFIle(String name) throws Exception{
        File file = new File(getDataFolder(), name);

        if (!file.exists()) {
            file.createNewFile();
        } else {
            System.out.println("[DG-HoloSpawners] Cargado el fichero hologramas.yml");
        }

    }

    public void editFile(String name, String arg1, String arg2) throws Exception {
        File file = new File(getDataFolder(), name);
        YamlConfiguration holos = YamlConfiguration.loadConfiguration(file);

        holos.set(arg1, arg2);
        holos.save(file);

    }

    public void registerLoc(String name, String arg1, Location arg2) throws Exception {
        File file = new File(getDataFolder(), name);
        YamlConfiguration holos = YamlConfiguration.loadConfiguration(file);

        holos.set(arg1, arg2);
        holos.save(file);

    }

    public void generarHolos(String name) {
        File file = new File(getDataFolder(), name);
        YamlConfiguration holos = YamlConfiguration.loadConfiguration(file);

        for (String key : holos.getKeys(false)) {

            Location loc = (Location) holos.get(key + ".loc");
            Hologram hl = HologramsAPI.createHologram(Bukkit.getPluginManager().getPlugin("DG-HoloSpawner"), loc);

            String nombre = holos.getString(key + ".nombre");

            crearHolograma(hl, holos.getString(key + ".spawner"), nombre);

            listholos.put(key, hl);

        }


        //config.set("path.to.location", yourLocation); and yourLocation = (Location) config.get("path.to.location");

    }

    public void crearHolograma(Hologram holo, String ms, String nombremob) {

        new BukkitRunnable() {

            public void run() {

                MythicSpawner spawner = MythicBukkit.inst().getSpawnerManager().getSpawnerByName(ms);

                if (spawner == null) {
                    holo.delete();
                    this.cancel();
                }
                if (!holo.isDeleted()) {



                    int s = spawner.getRemainingCooldownSeconds();
                    int sec = s % 60;
                    int min = (s / 60) % 60;
                    int hours = (s / 60) / 60;

                    String strSec = (sec < 10) ? "0" + Integer.toString(sec) : Integer.toString(sec);
                    String strmin = (min < 10) ? "0" + Integer.toString(min) : Integer.toString(min);
                    String strHours = (hours < 10) ? "0" + Integer.toString(hours) : Integer.toString(hours);

                    String cooldown = ChatColor.GRAY + "Cooldown: " + ChatColor.GREEN + strHours + ":" + strmin + ":" + strSec;

                    holo.clearLines();
                    holo.appendTextLine(nombremob);
                    holo.appendTextLine("§7");

                    String mobs;

                    /*if (spawner.getNumberOfMobs() > 0) {
                        mobs = ChatColor.GREEN + "Si";
                    } else {
                        mobs = ChatColor.RED + "No";
                    }*/

                    //holo.appendTextLine("§7Boss spawneado: §a" + mobs);
                    holo.appendTextLine(cooldown);
                }
                else {
                    this.cancel();
                }
            }
        }.runTaskTimer(this, 0L, 20L);
    }


}