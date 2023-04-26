/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.backend.info.Info;
import net.auroramc.api.cosmetics.Cosmetic;
import net.auroramc.api.cosmetics.PlusSymbol;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.player.PlayerReport;
import net.auroramc.api.punishments.Punishment;
import net.auroramc.api.utils.Pronoun;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.events.player.PlayerObjectCreationEvent;
import net.auroramc.core.api.player.scoreboard.PlayerScoreboard;
import net.auroramc.core.api.utils.holograms.Hologram;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.*;
import org.bukkit.map.MapView;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import us.myles.ViaVersion.api.Via;

import java.net.InetSocketAddress;
import java.util.*;

public class AuroraMCServerPlayer extends AuroraMCPlayer {

    private Player player;
    private Map<String, BukkitTask> expiryTasks;
    private final PlayerScoreboard scoreboard;
    private boolean vanished;
    private UUID lastAdminMessaged;

    private BukkitTask activeReportTask;
    private BukkitTask subscriptionTask;
    private HashMap<Cosmetic, BukkitTask> runningCosmeticTasks;

    protected boolean dead;
    protected boolean hidden;
    protected boolean moved;

    private Map<String, Hologram> holograms;

    public AuroraMCServerPlayer(Player player) {
        super(player.getUniqueId(), player.getName(), player);
        this.player = player;
        dead = false;
        hidden = false;
        moved = false;
        holograms = new HashMap<>();
        scoreboard = new PlayerScoreboard(this, Bukkit.getScoreboardManager().getNewScoreboard());
        this.player = player;
    }

    public AuroraMCServerPlayer(AuroraMCServerPlayer player) {
        super(player);
        this.player = player.player;
        this.dead = player.dead;
        this.hidden = player.hidden;
        this.moved = player.moved;
        this.holograms = player.holograms;
        this.scoreboard = player.scoreboard;
        this.subscriptionTask = player.subscriptionTask;
        this.lastAdminMessaged = player.lastAdminMessaged;
        this.activeReportTask = player.activeReportTask;
        this.runningCosmeticTasks = player.runningCosmeticTasks;
        this.expiryTasks = player.expiryTasks;
        this.vanished = player.vanished;
    }


    @Override
    public void loadBefore(Object playerObject) {
        Player player = (Player) playerObject;
        this.player = player;
        this.vanished = AuroraMCAPI.getDbManager().isVanished(getUuid());
        for (Player player1 : Bukkit.getOnlinePlayers()) {
            player1.hidePlayer(player);
            player.hidePlayer(player1);
        }
    }

    @Override
    public void loadExtra() {
        if (getActiveSubscription() != null) {
            if (getActiveSubscription().getEndTimestamp() != -1 && getActiveSubscription().getEndTimestamp() > System.currentTimeMillis() && (getActiveSubscription().getEndTimestamp() - System.currentTimeMillis()) < 2592000000L) {
                subscriptionTask = new BukkitRunnable(){
                    @Override
                    public void run() {
                        AuroraMCServerPlayer.this.expireSubscription();
                    }
                }.runTaskLater(ServerAPI.getCore(), (getActiveSubscription().getEndTimestamp() - System.currentTimeMillis())/50);
            }
        }

        expiryTasks = new HashMap<>();

        for (Punishment punishment : getHistory().getPunishments()) {
            if (punishment.getStatus() == 1 || punishment.getStatus() == 2 || punishment.getStatus() == 3) {
                //This is an active punishment.
                if (punishment.getExpire() > System.currentTimeMillis()) {
                    //Active mute. Add to the scheduler.
                    BukkitTask task = new BukkitRunnable() {
                        @Override
                        public void run() {
                            AuroraMCAPI.getDbManager().expirePunishment(punishment.getPunishmentCode(), punishment.getStatus());
                            removeMute(punishment);
                            expiryTasks.remove(punishment.getPunishmentCode());
                        }
                    }.runTaskLaterAsynchronously(ServerAPI.getCore(), (punishment.getExpire() - System.currentTimeMillis()) / 50);
                    expiryTasks.put(punishment.getPunishmentCode(), task);
                }
            }
        }

        if (getActiveReport() != null) {
            activeReportTask = new BukkitRunnable(){
                @Override
                public void run() {
                    if (getActiveReport() != null) {
                        player.spigot().sendMessage(TextFormatter.formatReportMessage(getActiveReport()));
                    }
                }
            }.runTaskTimerAsynchronously(ServerAPI.getCore(), 0, 600);
        }

        int offlineReports = AuroraMCAPI.getDbManager().getOfflineReports(getId());
        if (getPreferences().isReportNotificationsEnabled()) {
            if (offlineReports > 0) {
                player.spigot().sendMessage(TextFormatter.pluginMessage("Reports", String.format("While you were offline, **%s** of your reports were handled by our Staff Team. Use /viewreports to see the individual outcomes of each report.", offlineReports)));
            }
        }

        if (AuroraMCAPI.getInfo().getNetwork() == Info.Network.TEST) {

            TextComponent component = new TextComponent("");

            TextComponent lines = new TextComponent("▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆");
            lines.setBold(true);
            lines.setColor(net.md_5.bungee.api.ChatColor.DARK_RED);

            component.addExtra(lines);
            component.addExtra("\n                     ");

            TextComponent cmp = new TextComponent("«MISSION CONTROL»\n \n");
            cmp.setColor(net.md_5.bungee.api.ChatColor.RED);
            cmp.setBold(true);
            component.addExtra(cmp);

            cmp = new TextComponent("You are currently connected to the AuroraMC Test\n" +
                    "Network!\n" +
                    "\n" +
                    "All servers in this network will not save data, and are\n" +
                    "all on test versions of our plugins.\n \n");
            cmp.setColor(ChatColor.WHITE);
            cmp.setBold(false);
            component.addExtra(cmp);
            component.addExtra(lines);
            sendMessage(component);
        } else if (AuroraMCAPI.isTestServer()) {
            sendMessage(TextFormatter.pluginMessage("Server Manager", "&c&lThis server is in test mode. While test mode is enabled, stats and other core features will not be saved."));
        }

        //Get the bungee to send all of the friend data to the server

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("UpdateFriendsList");
        out.writeUTF(getName());
        sendPluginMessage(out.toByteArray());


        new BukkitRunnable(){
            @Override
            public void run() {
                for (AuroraMCServerPlayer player1 : ServerAPI.getPlayers()) {
                    player1.updateNametag(AuroraMCServerPlayer.this);
                    updateNametag(player1);

                    if (player1 != AuroraMCServerPlayer.this) {

                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                if (!player1.isVanished() || player1.getRank().getId() <= AuroraMCServerPlayer.this.getRank().getId()) {
                                    AuroraMCServerPlayer.this.hidePlayer(player1);
                                    AuroraMCServerPlayer.this.showPlayer(player1);
                                }
                                if (!isVanished() || player1.getRank().getId() >= AuroraMCServerPlayer.this.getRank().getId()) {
                                    player1.hidePlayer(AuroraMCServerPlayer.this);
                                    player1.showPlayer(AuroraMCServerPlayer.this);
                                }
                            }
                        }.runTaskLater(ServerAPI.getCore(), 1);
                    }
                }

                updateNametag(AuroraMCServerPlayer.this);

                //To ensure that this is being called after everything has been retrived, it is called here and then replaces the object already in the cache.
                if (!player.isOnline()) {
                    return;
                }
                PlayerObjectCreationEvent creationEvent = new PlayerObjectCreationEvent(AuroraMCServerPlayer.this);
                Bukkit.getPluginManager().callEvent(creationEvent);
                if (!player.isOnline()) {
                    return;
                }
                ServerAPI.newPlayer(player, creationEvent.getPlayer());
                creationEvent.getPlayer().setLoaded(true);

                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("UpdateParty");
                out.writeUTF(getName());
                sendPluginMessage(out.toByteArray());
            }
        }.runTask(ServerAPI.getCore());

    }

    @Override
    public boolean undisguise(boolean save) {
        boolean last = super.undisguise(save);
        for (AuroraMCServerPlayer player1 : ServerAPI.getPlayers()) {
            player1.updateNametag(this);
        }
        return last;
    }

    @Override
    public void applyMute(Punishment punishment) {
        super.applyMute(punishment);
        if (punishment.getStatus() == 1 || punishment.getStatus() == 2 || punishment.getStatus() == 3) {
            //This is an active punishment.
            if (punishment.getExpire() > System.currentTimeMillis()) {
                //Active mute. Add to the scheduler.
                BukkitTask task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        AuroraMCAPI.getDbManager().expirePunishment(punishment.getPunishmentCode(), punishment.getStatus());
                        removeMute(punishment);
                        expiryTasks.remove(punishment.getPunishmentCode());
                    }
                }.runTaskLaterAsynchronously(ServerAPI.getCore(), (punishment.getExpire() - System.currentTimeMillis()) / 50);
                expiryTasks.put(punishment.getPunishmentCode(), task);
            }
        }
    }

    @Override
    public void expireSubscription() {
        if (getActiveSubscription() != null && this.subscriptionTask != null) {
            this.subscriptionTask.cancel();
        }
        super.expireSubscription();
    }

    public void extendSubscription() {
        if (subscriptionTask != null) {
            subscriptionTask.cancel();
            subscriptionTask = new BukkitRunnable() {
                @Override
                public void run() {
                    AuroraMCServerPlayer.this.expireSubscription();
                }
            }.runTaskLater(ServerAPI.getCore(), (getActiveSubscription().getEndTimestamp() - System.currentTimeMillis()) / 50);
        }
    }

    @Override
    public void setActiveReport(PlayerReport activeReport) {
        super.setActiveReport(activeReport);
        if (activeReport == null) {
            if (this.activeReportTask != null) {
                this.activeReportTask.cancel();
                this.activeReportTask = null;
            }
        } else {
            this.activeReportTask = createReportTask();
        }
    }

    @Override
    public boolean isOnline() {
        return player.isOnline();
    }

    public PlayerScoreboard getScoreboard() {
        return scoreboard;
    }

    public void setScoreboard() {
        player.setScoreboard(scoreboard.getScoreboard());
    }

    public void clearScoreboard() {
        for (AuroraMCServerPlayer bukkitPlayer : ServerAPI.getPlayers()) {
            org.bukkit.scoreboard.Team team = bukkitPlayer.getScoreboard().getScoreboard().getTeam(getByDisguiseName());
            if (team != null) {
                team.unregister();
            }
        }
    }

    @Override
    public void sendPluginMessage(byte[] byteArray) {
        player.sendPluginMessage(ServerAPI.getCore(), "BungeeCord", byteArray);
    }

    @Override
    public void sendTitle(BaseComponent title, BaseComponent subtitle, int fadeInTime, int showTime, int fadeOutTime) {
        IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(title));
        IChatBaseComponent chatSubtitle = IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(subtitle));

        if (subtitle != null) {
            PacketPlayOutTitle subTitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubtitle, fadeInTime, showTime, fadeOutTime);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subTitlePacket);
        }

        PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle, fadeInTime, showTime, fadeOutTime);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(titlePacket);
    }

    @Override
    public void sendHotBar(BaseComponent message) {
        IChatBaseComponent component = IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(message));

        PacketPlayOutChat packet = new PacketPlayOutChat(component, (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    @Override
    public void sendMessage(BaseComponent message) {
        player.spigot().sendMessage(message);
    }

    @Override
    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    public int getTicksLived() {
        return player.getTicksLived();
    }

    public void setTicksLived(int i) {
        player.setTicksLived(i);
    }

    public void playEffect(EntityEffect entityEffect) {
        player.playEffect(entityEffect);
    }

    public boolean isInsideVehicle() {
        return player.isInsideVehicle();
    }

    public boolean leaveVehicle() {
        return player.leaveVehicle();
    }

    public Entity getVehicle() {
        return player.getVehicle();
    }

    public boolean isVanished() {
        return vanished;
    }

    public void vanish() {
        this.vanished = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                AuroraMCAPI.getDbManager().vanish(AuroraMCServerPlayer.this);
            }
        }.runTaskAsynchronously(ServerAPI.getCore());
        for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
            if (player==this) {
                continue;
            }
            if (!player.isLoaded() || player.getRank().getId() < this.getRank().getId()) {
                player.hidePlayer(this);
                if (player.getScoreboard().getScoreboard().getTeam(this.player.getName()) != null) {
                    player.getScoreboard().getScoreboard().getTeam(this.player.getName()).unregister();
                }
            }
        }
    }

    public void unvanish() {
        this.vanished = false;
        AuroraMCPlayer pl = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                AuroraMCAPI.getDbManager().unvanish(pl);
            }
        }.runTaskAsynchronously(ServerAPI.getCore());
        for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
            if (player==this) {
                continue;
            }
            player.showPlayer(this);
            player.updateNametag(this);
        }
    }

    private BukkitTask createReportTask() {
        return new BukkitRunnable(){
            @Override
            public void run() {
                if (getActiveReport() != null) {
                    player.spigot().sendMessage(TextFormatter.formatReportMessage(getActiveReport()));
                }
            }
        }.runTaskTimerAsynchronously(ServerAPI.getCore(), 0, 600);
    }

    public boolean hasMoved() {
        return moved;
    }

    public void moved() {
        moved = true;
    }

    public void setLastAdminMessage(UUID lastMessaged) {
        this.lastAdminMessaged = lastMessaged;
    }

    public UUID getLastAdminMessaged() {
        return lastAdminMessaged;
    }

    public boolean isDead() {
        return dead;
    }

    public boolean isValid() {
        return player.isValid();
    }


    public Entity getPassenger() {
        return player.getPassenger();
    }

    public boolean setPassenger(Entity entity) {
        return player.setPassenger(entity);
    }

    public boolean isEmpty() {
        return player.isEmpty();
    }

    public boolean eject() {
        return player.eject();
    }

    public float getFallDistance() {
        return player.getFallDistance();
    }

    public void setFallDistance(float v) {
        player.setFallDistance(v);
    }

    public void setLastDamageCause(EntityDamageEvent entityDamageEvent) {
        player.setLastDamageCause(entityDamageEvent);
    }

    public EntityDamageEvent getLastDamageCause() {
        return player.getLastDamageCause();
    }

    public HashMap<Cosmetic, BukkitTask> getRunningCosmeticTasks() {
        return runningCosmeticTasks;
    }

    public Map<String, Hologram> getHolograms() {
        return holograms;
    }

    public void updateNametag(AuroraMCPlayer player) {
        String name = player.getByDisguiseName();
        Rank rank = player.getByDisguiserank();

        StringBuilder s;
        org.bukkit.scoreboard.Team team;

        if (player.isDisguised()) {
            if (player != this || !getPreferences().isHideDisguiseNameEnabled()) {
                if (scoreboard.getScoreboard().getTeam(player.getName()) != null) {
                    scoreboard.getScoreboard().getTeam(player.getName()).unregister();
                }
            } else {
                name = player.getName();
                rank = player.getRank();
            }
        }

        //Prefix.
        s = new StringBuilder(TextFormatter.rankFormat(rank, player.getActiveSubscription()));
        if (scoreboard.getScoreboard().getTeam(name) == null) {
            scoreboard.getScoreboard().registerNewTeam(name);
        }
        team = scoreboard.getScoreboard().getTeam(name);
        if (rank != Rank.PLAYER || player.getActiveSubscription() != null) {
            s.append(" ");
        }
        if (player.getTeam() == null) {
            s.append("§r");
            if (Via.getAPI().getPlayerVersion(this.player.getUniqueId()) >= 393) {
                s.append("§f");
            }
        } else {
            if (Via.getAPI().getPlayerVersion(this.player.getUniqueId()) >= 393) {
                s.append("§r");
            }
            s.append(getTeam().getTeamColor());
        }
        team.setPrefix(s.toString());


        s = new StringBuilder();

        if (getPreferences().getPreferredPronouns() != Pronoun.NONE) {
            s.append(" §7" + getPreferences().getPreferredPronouns().getDisplay());
        }
        if (player.getActiveSubscription() != null && player.getActiveCosmetics().get(Cosmetic.CosmeticType.PLUS_SYMBOL) != null) {
            PlusSymbol symbol = (PlusSymbol) player.getActiveCosmetics().get(Cosmetic.CosmeticType.PLUS_SYMBOL);
            s.append(String.format(" %s%s", player.getActiveSubscription().getSuffixColor(), symbol.getSymbol()));
        }

        team.setSuffix(s.toString());

        if ((player.getActiveDisguise() != null && player != this) || (player == this && !player.getPreferences().isHideDisguiseNameEnabled())) {
            if (!team.hasEntry(player.getByDisguiseName())) {
                for (String old : team.getEntries()) {
                    team.removeEntry(old);
                }
                team.addEntry(player.getByDisguiseName());
            }
        } else {
            if (!team.hasEntry(player.getName())) {
                for (String old : team.getEntries()) {
                    team.removeEntry(old);
                }
                team.addEntry(player.getName());
            }
        }

    }

    public void setCompassTarget(Location location) {
        player.setCompassTarget(location);
    }

    public Location getCompassTarget() {
        return player.getCompassTarget();
    }

    public InetSocketAddress getAddress() {
        return player.getAddress();
    }

    public void kickPlayer(String s) {
        player.kickPlayer(s);
    }

    public boolean isSneaking() {
        return player.isSneaking();
    }

    public void setSneaking(boolean b) {
        player.setSneaking(b);
    }

    public boolean isSprinting() {
        return player.isSprinting();
    }

    public void setSprinting(boolean b) {
        player.setSprinting(b);
    }

    public void saveData() {
        player.saveData();
    }

    public void loadData() {
        player.loadData();
    }

    public void setSleepingIgnored(boolean b) {
        player.setSleepingIgnored(b);
    }

    public boolean isSleepingIgnored() {
        return player.isSleepingIgnored();
    }

    public void playNote(Location location, Instrument instrument, Note note) {
        player.playNote(location, instrument, note);
    }

    public void playSound(Location location, Sound sound, float v, float v1) {
        player.playSound(location, sound, v, v1);
    }

    public void playSound(Location location, String s, float v, float v1) {
        player.playSound(location, s, v, v1);
    }

    public <T> void playEffect(Location location, Effect effect, T t) {
        player.playEffect(location, effect, t);
    }

    public void sendMap(MapView mapView) {
        player.sendMap(mapView);
    }

    public void updateInventory() {
        player.updateInventory();
    }

    public void setPlayerTime(long l, boolean b) {
        player.setPlayerTime(l, b);
    }

    public long getPlayerTime() {
        return player.getPlayerTime();
    }

    public long getPlayerTimeOffset() {
        return player.getPlayerTimeOffset();
    }

    public boolean isPlayerTimeRelative() {
        return player.isPlayerTimeRelative();
    }

    public void resetPlayerTime() {
        player.resetPlayerTime();
    }

    public void setPlayerWeather(WeatherType weatherType) {
        player.setPlayerWeather(weatherType);
    }

    public WeatherType getPlayerWeather() {
        return player.getPlayerWeather();
    }

    public void resetPlayerWeather() {
        player.resetPlayerWeather();
    }

    public void giveExp(int i) {
        player.giveExp(i);
    }

    public void giveExpLevels(int i) {
        player.giveExpLevels(i);
    }

    public float getExp() {
        return player.getExp();
    }

    public void setExp(float v) {
        player.setExp(v);
    }

    public int getLevel() {
        return player.getLevel();
    }

    public void setLevel(int i) {
        player.setLevel(i);
    }

    public int getTotalExperience() {
        return player.getTotalExperience();
    }

    public void setTotalExperience(int i) {
        player.setTotalExperience(i);
    }

    public float getExhaustion() {
        return player.getExhaustion();
    }

    public void setExhaustion(float v) {
        player.setExhaustion(v);
    }

    public float getSaturation() {
        return player.getSaturation();
    }

    public void setSaturation(float v) {
        player.setSaturation(v);
    }

    public int getFoodLevel() {
        return player.getFoodLevel();
    }

    public void setFoodLevel(int i) {
        player.setFoodLevel(i);
    }

    public Location getBedSpawnLocation() {
        return player.getBedSpawnLocation();
    }

    public void setBedSpawnLocation(Location location) {
        player.setBedSpawnLocation(location);
    }

    public boolean getAllowFlight() {
        return player.getAllowFlight();
    }

    public void setAllowFlight(boolean b) {
        player.setAllowFlight(true);
    }

    public void hidePlayer(AuroraMCServerPlayer player) {
        this.player.hidePlayer(player.player);
    }

    public void showPlayer(AuroraMCServerPlayer player) {
        this.player.showPlayer(player.player);
    }

    public boolean canSee(AuroraMCServerPlayer player) {
        return this.player.canSee(player.player);
    }

    public Location getLocation() {
        return player.getLocation();
    }

    public Location getLocation(Location location) {
        return player.getLocation(location);
    }

    public void setVelocity(Vector vector) {
        player.setVelocity(vector);
    }

    public Vector getVelocity() {
        return player.getVelocity();
    }

    public boolean isOnGround() {
        return player.isOnGround();
    }

    public World getWorld() {
        return player.getWorld();
    }

    public boolean teleport(Location location) {
        return player.teleport(location);
    }

    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause teleportCause) {
        return player.teleport(location, teleportCause);
    }

    public boolean teleport(Entity entity) {
        return player.teleport(entity);
    }

    public boolean teleport(Entity entity, PlayerTeleportEvent.TeleportCause teleportCause) {
        return player.teleport(entity, teleportCause);
    }

    public List<Entity> getNearbyEntities(double v, double v1, double v2) {
        return player.getNearbyEntities(v, v1, v2);
    }

    public int getFireTicks() {
        return player.getFireTicks();
    }

    public int getMaxFireTicks() {
        return player.getMaxFireTicks();
    }

    public void setFireTicks(int i) {
        player.setFireTicks(i);
    }

    public boolean isFlying() {
        return player.isFlying();
    }

    public void setFlying(boolean b) {
        player.setFlying(b);
    }

    public void setFlySpeed(float v) throws IllegalArgumentException {
        player.setFlySpeed(v);
    }

    public void setWalkSpeed(float v) throws IllegalArgumentException {
        player.setWalkSpeed(v);
    }

    public float getFlySpeed() {
        return player.getFlySpeed();
    }

    public float getWalkSpeed() {
        return player.getWalkSpeed();
    }

    public void setResourcePack(String s) {
        player.setResourcePack(s);
    }

    public boolean isHealthScaled() {
        return player.isHealthScaled();
    }

    public void setHealthScaled(boolean b) {
        player.setHealthScaled(b);
    }

    public void setHealthScale(double v) throws IllegalArgumentException {
        player.setHealthScale(v);
    }

    public double getHealthScale() {
        return player.getHealthScale();
    }

    public Entity getSpectatorTarget() {
        return player.getSpectatorTarget();
    }

    public void setSpectatorTarget(Entity entity) {
        player.setSpectatorTarget(entity);
    }

    public PlayerInventory getInventory() {
        return player.getInventory();
    }

    public Inventory getEnderChest() {
        return player.getEnderChest();
    }

    public boolean setWindowProperty(InventoryView.Property property, int i) {
        return player.setWindowProperty(property, i);
    }

    public InventoryView getOpenInventory() {
        return player.getOpenInventory();
    }

    public InventoryView openInventory(Inventory inventory) {
        return player.openInventory(inventory);
    }

    public InventoryView openWorkbench(Location location, boolean b) {
        return player.openWorkbench(location, b);
    }

    public InventoryView openEnchanting(Location location, boolean b) {
        return player.openEnchanting(location, b);
    }

    public void openInventory(InventoryView inventoryView) {
        player.openInventory(inventoryView);
    }

    public void closeInventory() {
        player.closeInventory();
    }

    public ItemStack getItemInHand() {
        return player.getItemInHand();
    }

    public void setItemInHand(ItemStack itemStack) {
        player.setItemInHand(itemStack);
    }

    public ItemStack getItemOnCursor() {
        return player.getItemOnCursor();
    }

    public void setItemOnCursor(ItemStack itemStack) {
        player.setItemOnCursor(itemStack);
    }

    public boolean isSleeping() {
        return player.isSleeping();
    }

    public int getSleepTicks() {
        return player.getSleepTicks();
    }

    public GameMode getGameMode() {
        return player.getGameMode();
    }

    public void setGameMode(GameMode gameMode) {
        player.setGameMode(gameMode);
    }

    public boolean isBlocking() {
        return player.isBlocking();
    }

    public double getEyeHeight() {
        return player.getEyeHeight();
    }

    public double getEyeHeight(boolean b) {
        return player.getEyeHeight(b);
    }

    public Location getEyeLocation() {
        return player.getEyeLocation();
    }

    public List<Block> getLineOfSight(Set<Material> set, int i) {
        return player.getLineOfSight(set, i);
    }

    public Block getTargetBlock(Set<Material> set, int i) {
        return player.getTargetBlock(set, i);
    }

    public List<Block> getLastTwoTargetBlocks(Set<Material> set, int i) {
        return player.getLastTwoTargetBlocks(set, i);
    }

    public int getRemainingAir() {
        return player.getRemainingAir();
    }

    public void setRemainingAir(int i) {
        player.setRemainingAir(i);
    }

    public int getMaximumAir() {
        return player.getMaximumAir();
    }

    public void setMaximumAir(int i) {
        player.setMaximumAir(i);
    }

    public int getMaximumNoDamageTicks() {
        return player.getMaximumNoDamageTicks();
    }

    public void setMaximumNoDamageTicks(int i) {
        player.setMaximumNoDamageTicks(i);
    }

    public double getLastDamage() {
        return player.getLastDamage();
    }

    public int getNoDamageTicks() {
        return player.getNoDamageTicks();
    }

    public void setNoDamageTicks(int i) {
        player.setNoDamageTicks(i);
    }

    public boolean addPotionEffect(PotionEffect potionEffect) {
        return player.addPotionEffect(potionEffect);
    }

    public boolean addPotionEffect(PotionEffect potionEffect, boolean b) {
        return player.addPotionEffect(potionEffect, b);
    }

    public boolean addPotionEffects(Collection<PotionEffect> collection) {
        return player.addPotionEffects(collection);
    }

    public boolean hasPotionEffect(PotionEffectType potionEffectType) {
        return player.hasPotionEffect(potionEffectType);
    }

    public void removePotionEffect(PotionEffectType potionEffectType) {
        player.removePotionEffect(potionEffectType);
    }

    public Collection<PotionEffect> getActivePotionEffects() {
        return player.getActivePotionEffects();
    }

    public boolean hasLineOfSight(Entity entity) {
        return player.hasLineOfSight(entity);
    }

    public EntityEquipment getEquipment() {
        return player.getEquipment();
    }

    public void damage(double v) {
        player.damage(v);
    }

    public void damage(double v, Entity entity) {
        player.damage(v, entity);
    }

    public double getHealth() {
        return player.getHealth();
    }

    public void setHealth(double v) {
        player.setHealth(v);
    }

    public double getMaxHealth() {
        return player.getMaxHealth();
    }

    public void setMaxHealth(double v) {
        player.setMaxHealth(v);
    }

    public void resetMaxHealth() {
        player.resetMaxHealth();
    }

    public <T extends Projectile> T launchProjectile(Class<? extends T> aClass) {
        return player.launchProjectile(aClass);
    }

    public <T extends Projectile> T launchProjectile(Class<? extends T> aClass, Vector vector) {
        return player.launchProjectile(aClass, vector);
    }

    public CraftPlayer getCraft() {
        return (CraftPlayer) player;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuroraMCServerPlayer that = (AuroraMCServerPlayer) o;
        return Objects.equals(player, that.player);
    }

    public boolean equals(Player o) {
        return Objects.equals(player, o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player);
    }

    public BukkitTask getActiveReportTask() {
        return activeReportTask;
    }
}
