/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.api.nova;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.abstraction.ScheduleFactory;
import net.auroramc.api.player.AuroraMCPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckLog {

    private final TextComponent base;
    private final AuroraMCPlayer player;
    private final NovaCheck check;
    private final Map<Violation, Object> violations;

    public CheckLog(AuroraMCPlayer player, NovaCheck check) {
        this.player = player;
        this.check = check;
        this.violations = new HashMap();

        base = new TextComponent("");
        TextComponent prefix = new TextComponent("«NOVA AC»");
        prefix.setBold(true);
        prefix.setColor(ChatColor.DARK_RED);
        base.addExtra(prefix);
        base.addExtra(" ");

        TextComponent name = new TextComponent(player.getByDisguiseName());
        name.setColor(ChatColor.RED);
        name.setBold(false);
        base.addExtra(name);

        TextComponent suspected = new TextComponent(" is suspected of ");
        suspected.setColor(ChatColor.RESET);
        suspected.setBold(false);
        base.addExtra(suspected);
    }

    public synchronized void logViolation(Violation violation) {
        this.violations.put(violation, ScheduleFactory.scheduleAsyncLater(() -> {
            expire(violation);
        }, check.getExpiryTicks()));

        if (violations.size() == this.check.getLight()) {
            TextComponent component = new TextComponent(base);
            TextComponent check = new TextComponent(this.check.getName());
            check.setColor(ChatColor.GREEN);
            check.setBold(false);
            component.addExtra(check);
            component.addExtra(".");

            AuroraMCAPI.getAbstractedMethods().broadcastNovaMessage(component);
        } else if (violations.size() == this.check.getMedium()) {
            TextComponent component = new TextComponent(base);
            TextComponent check = new TextComponent(this.check.getName());
            check.setColor(ChatColor.YELLOW);
            check.setBold(false);
            component.addExtra(check);
            component.addExtra(".");

            AuroraMCAPI.getAbstractedMethods().broadcastNovaMessage(component);
        } else if (violations.size() == this.check.getSevere() || (violations.size() > this.check.getSevere() && (violations.size() - this.check.getSevere()) % this.check.getNotificationFrequency() == 0)) {
            TextComponent component = new TextComponent(base);
            TextComponent check = new TextComponent(this.check.getName());
            check.setColor(ChatColor.RED);
            check.setBold(false);
            component.addExtra(check);
            component.addExtra(".");

            AuroraMCAPI.getAbstractedMethods().broadcastNovaMessage(component);
        }
    }

    public synchronized void expire(Violation violation) {
        violations.remove(violation);
    }

    public AuroraMCPlayer getPlayer() {
        return player;
    }

    public NovaCheck getCheck() {
        return check;
    }

    public Map<Violation, Object> getViolations() {
        return violations;
    }
}
