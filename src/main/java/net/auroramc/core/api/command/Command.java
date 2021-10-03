package net.auroramc.core.api.command;

import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Command {

    private final String mainCommand;
    private final List<String> aliases;
    protected final Map<String, Command> subcommands;
    private final List<Permission> permission;
    private final boolean showPermissionMessage;
    private final String customPermissionMessage;

    public Command(String mainCommand, List<String> alises, List<Permission> permission, boolean showPermissionMessage, String customPermissionMessage) {
        this.mainCommand = mainCommand.toLowerCase();
        this.aliases = alises;
        this.subcommands = new HashMap<>();
        this.permission = permission;
        this.showPermissionMessage = showPermissionMessage;
        this.customPermissionMessage = customPermissionMessage;
    }
    public abstract void execute(AuroraMCPlayer player, String aliasUsed, List<String> args);

    @NotNull
    public abstract List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments);

    protected void registerSubcommand(String subcommand, List<String> aliases, Command command) {
        subcommands.put(subcommand.toLowerCase(), command);
        for (String alias : aliases) {
            subcommands.put(alias.toLowerCase(), command);
        }
    }

    public String getMainCommand() {
        return mainCommand;
    }

    public Command getSubcommand(String subCommand) {
        return subcommands.get(subCommand);
    }

    public List<String> getAliases() {
        return aliases;
    }

    public List<Permission> getPermission() {
        return permission;
    }

    public boolean showPermissionMessage() {
        return showPermissionMessage;
    }

    public String getCustomPermissionMessage() {
        return customPermissionMessage;
    }

}
