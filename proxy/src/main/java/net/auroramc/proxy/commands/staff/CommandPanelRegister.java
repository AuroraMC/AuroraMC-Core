/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.commands.staff;


import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.api.permissions.Permission;
import net.auroramc.proxy.api.backend.ProxyDatabaseManager;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import net.auroramc.proxy.api.utils.BCrypt;
import net.auroramc.proxy.api.utils.PanelAccountType;
import net.md_5.bungee.api.ProxyServer;
import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandPanelRegister extends ProxyCommand {


    public CommandPanelRegister() {
        super("panelregister", Arrays.asList("registerpanel", "panelreg", "regpanel"), Collections.singletonList(Permission.PANEL), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            if (checkString(args.get(0))) {
                ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                    String hashedPassword = hash(args.get(0));
                    if (hashedPassword == null) {
                        player.sendMessage(TextFormatter.pluginMessage("Panel Manager", "The server has failed to encrypt your password. Please try again or let Block2Block know."));
                        return;
                    }
                    if (!AuroraMCAPI.getDbManager().hasAccount(player.getUniqueId())) {
                        //Gen new account.

                        PanelAccountType panelAccountType = null;

                        //What account type is it.
                        for (PanelAccountType type : PanelAccountType.values()) {
                            if (type.getRank() != null) {
                                if (player.getRank() == type.getRank()) {
                                    panelAccountType = type;
                                    break;
                                }
                            }
                            if (type.getSubrank() != null) {
                                if (player.getSubranks().contains(type.getSubrank())) {
                                    panelAccountType = type;
                                    break;
                                }
                            }
                        }

                        ProxyDatabaseManager.createPanelAccount(player.getName(), hashedPassword, player.getUniqueId(), panelAccountType);

                        player.sendMessage(TextFormatter.pluginMessage("Panel Manager", "A panel account has been created! Details:\n" +
                                "Login at **https://admin.auroramc.block2block.me/**\n" +
                                "Your username is **" + player.getName() + "**\n" +
                                "You can generate a verification code using /panel."));
                    } else {
                        //Update password.
                        AuroraMCAPI.getDbManager().updatePanelPassword(player.getUniqueId(), hashedPassword);
                        player.sendMessage(TextFormatter.pluginMessage("Panel Manager", "Your password has been updated."));
                    }
                });
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Panel Manager", "Your specified password does not meet the password strength requirements. Your password must be at least 8 characters long and have at least 1 of each of the following:\n" +
                        " - **Lowercase letter.**\n" +
                        " - **Uppercase letter.**\n" +
                        " - **Number.**\n" +
                        " - **Special character.**"));
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Panel Manager", "In order to create an account on the admin panel or reset your password, you must specify a password."));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }

    private static boolean checkString(String str) {
        if (str.length() < 8) {
            return false;
        }
        char ch;
        boolean capitalFlag = false;
        boolean lowerCaseFlag = false;
        boolean numberFlag = false;
        boolean characterFlag = false;
        for(int i=0;i < str.length();i++) {
            ch = str.charAt(i);
            if( Character.isDigit(ch)) {
                numberFlag = true;
            } else if (Character.isAlphabetic(ch)) {
                if (Character.isUpperCase(ch)) {
                    capitalFlag = true;
                } else if (Character.isLowerCase(ch)) {
                    lowerCaseFlag = true;
                }
            } else {
                characterFlag = true;
            }
            if(numberFlag && capitalFlag && lowerCaseFlag && characterFlag)
                return true;
        }
        return false;
    }

    private static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }
}
