package network.auroramc.core.api.command;

import network.auroramc.core.api.permissions.Permission;
import network.auroramc.core.api.players.AuroraMCPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Command {

    private final String mainCommand;
    private final ArrayList<String> aliases;
    private final Map<String, Command> subcommands;
    private final List<Permission> permission;
    private final boolean showPermissionMessage;
    private final String customPermissionMessage;

    public Command(String mainCommand, ArrayList<String> alises, List<Permission> permission, boolean showPermissionMessage, String customPermissionMessage) {
        this.mainCommand = mainCommand.toLowerCase();
        this.aliases = alises;
        this.subcommands = new HashMap<>();
        this.permission = permission;
        this.showPermissionMessage = showPermissionMessage;
        this.customPermissionMessage = customPermissionMessage;
    }
    public abstract void execute(AuroraMCPlayer player, String aliasUsed, List<String> args);

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

    public ArrayList<String> getAliases() {
        return new ArrayList<>(aliases);
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
