package net.auroramc.core.gui.friends;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.friends.Friend;
import net.auroramc.core.api.players.friends.FriendStatus;
import net.auroramc.core.api.players.friends.FriendsList;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static net.auroramc.core.api.players.friends.FriendsList.VisibilityMode.*;

public class Friends extends GUI {

    public enum FriendsView {FAVOURITE, ALL, DELETE, REQUESTS, STATUS}

    private AuroraMCPlayer player;
    private FriendsList friendsList;
    private int currentPage;
    private final List<Friend> displayOrder;
    private final List<FriendStatus> statuses;
    private FriendsView view;

    public Friends(AuroraMCPlayer player) {
        super("&d&lFriends", 5, true);

        this.friendsList = player.getFriendsList();

        for (int i = 0; i <= 8; i++) {
            if (i < 6) {
                this.setItem(i, 0, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lYour Friends", 1, "", (short) 7));
                this.setItem(i, 8, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lYour Friends", 1, "", (short) 7));
            }
            this.setItem(0, i, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lYour Friends", 1, "", (short) 7));
            this.setItem(5, i, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lYour Friends", 1, "", (short) 7));
        }
        this.setItem(0, 1, new GUIItem(Material.NETHER_STAR, "&3&lFavourite Friends", 1, ";&rClick here to change the;&rview to **Favourite Friends**;&ronly.", (short) 0, true));
        this.setItem(0, 3, new GUIItem(Material.BOOK, "&3&lAll Friends", 1, ";&rClick here to change the;&rview to **All Friends**.", (short) 0, false));
        this.setItem(0, 5, new GUIItem(Material.PAPER, "&3&lFriend Requests", 1, ";&rClick here to change the;&rview to **Friend Requests**.", (short) 0, false));
        this.setItem(0, 7, new GUIItem(Material.BARRIER, "&3&lDelete Friends", 1, ";&c&lWARNING:&r Clicking a player head in;&rthis menu will &cunfriend&r a player.", (short) 0, false));

        this.setItem(5, 3, new GUIItem(Material.NAME_TAG, "&3&lChange Status", 1, String.format(";&rCurrent Status: &%s%s;;&r&eClick to open the status menu.", friendsList.getCurrentStatus().getColour(), friendsList.getCurrentStatus().getName())));

        this.setItem(5, 5, new GUIItem(Material.INK_SACK, "&3&lServer Visibility", 1, String.format(";&rCurrent Server Visibility: %s;;&rClick to change to: %s", ((friendsList.getVisibilityMode() == ALL) ? "&aAll" : ((friendsList.getVisibilityMode() == FAVOURITE_FRIENDS_ONLY) ? "&6Favourite Friends Only" : "&cNobody")), ((friendsList.getVisibilityMode() == ALL) ? "&6Favourite Friends Only" : ((friendsList.getVisibilityMode() == FAVOURITE_FRIENDS_ONLY) ? "&cNobody" : "&aAll"))), ((friendsList.getVisibilityMode() == ALL) ? (short) 10 : ((friendsList.getVisibilityMode() == FAVOURITE_FRIENDS_ONLY) ? (short) 14 : (short) 8))));

        this.displayOrder = new ArrayList<>();
        this.player = player;
        this.currentPage = 1;

        view = FriendsView.FAVOURITE;

        statuses = Arrays.stream(FriendStatus.values()).filter(status -> status.getPermission() == null || player.hasPermission(status.getPermission().getNode()) || status.showInGUI()).collect(Collectors.toList());

        refresh();

        if (displayOrder.size() > 28) {
            this.setItem(5, 7, new GUIItem(Material.ARROW, "&3&lNext Page"));
        }

        int column = 1;
        int row = 1;

        for (Friend friend : displayOrder) {
            this.setItem(row, column, new GUIItem(Material.SKULL_ITEM, String.format("&3&l%s&r&b%s", friend.getName(), ((friend.getType() == Friend.FriendType.FAVOURITE) ? " ✰" : "")), 1, String.format(";&rStatus: &%s%s%s;;&r&%sShift-Left-Click to %s them as a favourite friend.", friend.getStatus().getColour(), friend.getStatus().getName(), ((friend.getServer() != null) ? String.format(";&rServer: **%s**", friend.getServer()) : ""), ((friend.getType() == Friend.FriendType.FAVOURITE) ? 'c' : 'a'), ((friend.getType() == Friend.FriendType.FAVOURITE) ? "remove" : "add")), ((friend.getStatus() != FriendStatus.OFFLINE) ? (short) 3 : (short) 0), false, friend.getName()));
            column++;
            if (column == 8) {
                row++;
                column = 1;
                if (row == 5) {
                    break;
                }
            }
        }

    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.STAINED_GLASS_PANE) {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
        } else {
            if (row == 0 || row == 5) {
                switch (item.getType()) {
                    case NETHER_STAR:
                        view = FriendsView.FAVOURITE;
                        refresh();
                        currentPage = 1;
                        this.updateItem(5, 1, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lYour Friends", 1, "", (short) 7));
                        if (displayOrder.size() > 28) {
                            this.updateItem(5, 7, new GUIItem(Material.ARROW, "&3&lNext Page"));
                        } else {
                            this.updateItem(5, 1, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lYour Friends", 1, "", (short) 7));
                        }
                        break;
                    case BOOK:
                        view = FriendsView.ALL;
                        refresh();
                        currentPage = 1;
                        this.updateItem(5, 1, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lYour Friends", 1, "", (short) 7));
                        if (displayOrder.size() > 28) {
                            this.updateItem(5, 7, new GUIItem(Material.ARROW, "&3&lNext Page"));
                        } else {
                            this.updateItem(5, 1, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lYour Friends", 1, "", (short) 7));
                        }
                        break;
                    case PAPER:
                        view = FriendsView.REQUESTS;
                        refresh();
                        currentPage = 1;
                        this.updateItem(5, 1, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lYour Friends", 1, "", (short) 7));
                        if (displayOrder.size() > 28) {
                            this.updateItem(5, 7, new GUIItem(Material.ARROW, "&3&lNext Page"));
                        } else {
                            this.updateItem(5, 1, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lYour Friends", 1, "", (short) 7));
                        }
                        break;
                    case BARRIER:
                        view = FriendsView.DELETE;
                        refresh();
                        currentPage = 1;
                        this.updateItem(5, 1, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lYour Friends", 1, "", (short) 7));

                        if (displayOrder.size() > 28) {
                            this.updateItem(5, 7, new GUIItem(Material.ARROW, "&3&lNext Page"));
                        } else {
                            this.updateItem(5, 1, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lYour Friends", 1, "", (short) 7));
                        }
                        break;
                    case ARROW:
                        if (column == 7) {
                            currentPage++;
                            if (view == FriendsView.STATUS) {
                                if (statuses.size() < ((currentPage) * 28)) {
                                    this.updateItem(5, 7, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lYour Friends", 1, "", (short) 7));
                                }
                            } else {
                                if (displayOrder.size() < ((currentPage) * 28)) {
                                    this.updateItem(5, 7, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lYour Friends", 1, "", (short) 7));
                                }
                            }

                            this.updateItem(5, 1, new GUIItem(Material.ARROW, "&3&lPrevious Page"));
                        } else {
                            currentPage--;
                            this.updateItem(5, 7, new GUIItem(Material.ARROW, "&3&lNext Page"));

                            if (currentPage == 1) {
                                this.updateItem(5, 1, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lYour Friends", 1, "", (short) 7));
                            }
                        }
                        break;
                    case INK_SACK:
                        switch (item.getDurability()) {
                            case 8:
                                friendsList.setVisibilityMode(ALL, true);
                                break;
                            case 14:
                                friendsList.setVisibilityMode(NOBODY, true);
                                break;
                            case 10:
                                friendsList.setVisibilityMode(FAVOURITE_FRIENDS_ONLY, true);
                                break;
                        }
                        this.updateItem(5, 5, new GUIItem(Material.INK_SACK, "&3&lServer Visibility", 1, String.format(";&rCurrent Server Visibility: %s;;&rClick to change to: %s", ((friendsList.getVisibilityMode() == ALL) ? "&aAll" : ((friendsList.getVisibilityMode() == FAVOURITE_FRIENDS_ONLY) ? "&6Favourite Friends Only" : "&cNobody")), ((friendsList.getVisibilityMode() == ALL) ? "&6Favourite Friends Only" : ((friendsList.getVisibilityMode() == FAVOURITE_FRIENDS_ONLY) ? "&cNobody" : "&aAll"))), ((friendsList.getVisibilityMode() == ALL) ? (short) 10 : ((friendsList.getVisibilityMode() == FAVOURITE_FRIENDS_ONLY) ? (short) 14 : (short) 8))));
                        break;
                    case NAME_TAG:
                        view = FriendsView.STATUS;
                        currentPage = 1;
                        break;
                }


                row = 1;
                column = 1;
                for (int i = 0; i < 28; i++) {
                    //show the prev 28 items.
                    int pi = (((currentPage - 1) * 28) + i);
                    if (view == FriendsView.STATUS) {
                        if (statuses.size() <= pi) {
                            this.updateItem(row, column, null);
                            column++;
                            if (column == 8) {
                                row++;
                                column = 1;
                                if (row == 5) {
                                    break;
                                }
                            }
                            continue;
                        }
                        FriendStatus status = statuses.get(pi);
                        this.updateItem(row, column, new GUIItem(Material.PAPER, String.format("&%s&l%s", status.getColour(), status.getTitle()), 1, String.format(";&rClick here to display your;&rstatus as &%s%s&r.%s", status.getColour(), status.getName(), ((status.getPermission() != null) ? ((player.hasPermission(status.getPermission().getNode())) ? "" : ((status.getCustomPermissionText() != null) ? ";;&r&c" + status.getCustomPermissionText() : ";;&r&cYou do not have permission to set this status.")) : "")), (short) 0, (friendsList.getCurrentStatus() == status)));
                    } else {
                        if (displayOrder.size() <= pi) {
                            this.updateItem(row, column, null);
                            column++;
                            if (column == 8) {
                                row++;
                                column = 1;
                                if (row == 5) {
                                    break;
                                }
                            }
                            continue;
                        }
                        Friend friend = displayOrder.get(pi);
                        if (view == FriendsView.REQUESTS) {
                            this.updateItem(row, column, new GUIItem(((friend.getType() == Friend.FriendType.PENDING_INCOMING) ? Material.ENDER_PEARL : Material.EYE_OF_ENDER), String.format("&3&l%s", friend.getName()), 1, ((friend.getType() == Friend.FriendType.PENDING_INCOMING) ? String.format("&rIncoming request from **%s**.;;&r&aLeft-Click to accept.;&r&cRight-Click to deny.", friend.getName()) : String.format("&rOutgoing request to **%s**;;&r&cShift-Right-Click to revoke.", friend.getName()))));
                        } else {
                            if (view == FriendsView.DELETE) {
                                this.updateItem(row, column, new GUIItem(Material.SKULL_ITEM, String.format("&3&l%s&r&b%s", friend.getName(), ((friend.getType() == Friend.FriendType.FAVOURITE) ? " ✰" : "")), 1, String.format(";&rStatus: &%s%s%s;;&r&cLeft-Click to remove them as a friend.", friend.getStatus().getColour(), friend.getStatus().getName(), ((friend.getServer() != null) ? String.format(";&rServer: **%s**", friend.getServer()) : "")), ((friend.getStatus() != FriendStatus.OFFLINE) ? (short) 3 : (short) 0), false, friend.getName()));
                            } else {
                                this.updateItem(row, column, new GUIItem(Material.SKULL_ITEM, String.format("&3&l%s&r&b%s", friend.getName(), ((friend.getType() == Friend.FriendType.FAVOURITE) ? " ✰" : "")), 1, String.format(";&rStatus: &%s%s%s;;&r&%sShift-Left-Click to %s them as a favourite friend.", friend.getStatus().getColour(), friend.getStatus().getName(), ((friend.getServer() != null) ? String.format(";&rServer: **%s**", friend.getServer()) : ""), ((friend.getType() == Friend.FriendType.FAVOURITE) ? 'c' : 'a'), ((friend.getType() == Friend.FriendType.FAVOURITE) ? "remove" : "add")), ((friend.getStatus() != FriendStatus.OFFLINE) ? (short) 3 : (short) 0), false, friend.getName()));
                            }
                        }
                    }
                    column++;
                    if (column == 8) {
                        row++;
                        column = 1;
                        if (row == 5) {
                            break;
                        }
                    }
                }
            } else {
                if (view == FriendsView.STATUS) {
                    FriendStatus newStatus = statuses.get(((currentPage - 1) * 28) + ((row - 1) * 7) + (column - 1));
                    if (newStatus.getPermission() != null) {
                        if (!player.hasPermission(newStatus.getPermission().getNode())) {
                            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                            return;
                        }
                    }
                    friendsList.setCurrentStatus(newStatus, true);
                    row = 1;
                    column = 1;
                    for (int i = 0; i < 28; i++) {
                        int pi = (((currentPage - 1) * 28) + i);
                        if (statuses.size() <= pi) {
                            this.updateItem(row, column, null);
                            continue;
                        }
                        FriendStatus status = statuses.get(pi);
                        this.updateItem(row, column, new GUIItem(Material.PAPER, String.format("&%s&l%s", status.getColour(), status.getTitle()), 1, String.format(";&rClick here to display your;&rstatus as &%s%s&r.%s", status.getColour(), status.getName(), ((status.getPermission() != null) ? ((player.hasPermission(status.getPermission().getNode())) ? "" : ((status.getCustomPermissionText() != null) ? ";;&r&c" + status.getCustomPermissionText() : ";;&r&cYou do not have permission to set this status.")) : "")), (short) 0, (friendsList.getCurrentStatus() == status)));
                        column++;
                        if (column == 8) {
                            row++;
                            column = 1;
                            if (row == 5) {
                                break;
                            }
                        }
                    }
                    this.updateItem(5, 3, new GUIItem(Material.NAME_TAG, "&3&lChange Status", 1, String.format(";&rCurrent Status: &%s%s;;&r&eClick to open the status menu.", friendsList.getCurrentStatus().getColour(), friendsList.getCurrentStatus().getName())));

                } else {
                    Friend friend = displayOrder.get(((currentPage - 1) * 28) + ((row - 1) * 7) + (column - 1));
                    switch (view) {
                        case ALL:
                        case FAVOURITE:
                            if (clickType == ClickType.SHIFT_LEFT) {
                                if (friend.getType() == Friend.FriendType.FAVOURITE) {
                                    friend.unfavourited(true);
                                } else {
                                    friend.favourited(true);
                                }
                                player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 1, 100);
                                this.updateItem(row, column, new GUIItem(Material.SKULL_ITEM, String.format("&3&l%s&r&b%s", friend.getName(), ((friend.getType() == Friend.FriendType.FAVOURITE) ? " ✰" : "")), 1, String.format(";&rStatus: &%s%s%s;;&r&%sShift-Left-Click to %s them as a favourite friend.", friend.getStatus().getColour(), friend.getStatus().getName(), ((friend.getServer() != null) ? String.format(";&rServer: **%s**", friend.getServer()) : ""), ((friend.getType() == Friend.FriendType.FAVOURITE) ? 'c' : 'a'), ((friend.getType() == Friend.FriendType.FAVOURITE) ? "remove" : "add")), ((friend.getStatus() != FriendStatus.OFFLINE) ? (short) 3 : (short) 0), false, friend.getName()));
                            } else {
                                if (friend.getServer() != null) {
                                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                    out.writeUTF("FriendTeleport");
                                    out.writeUTF(player.getName());
                                    out.writeUTF(friend.getServer());
                                    player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                                }
                            }
                            break;
                        case DELETE:
                            friendsList.friendRemoved(friend.getUuid(), true);
                            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 1, 100);
                            this.updateItem(row, column, null);
                            displayOrder.remove(friend);
                            //Regenerate the page.

                            if (displayOrder.size() < ((currentPage) * 28)) {
                                this.updateItem(5, 7, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lYour Friends", 1, "", (short) 7));
                            }

                            row = 1;
                            column = 1;
                            for (int i = 0; i < 28; i++) {
                                //show the prev 28 items.
                                int pi = (((currentPage - 1) * 28) + i);

                                if (displayOrder.size() <= pi) {
                                    this.updateItem(row, column, null);
                                    column++;
                                    if (column == 8) {
                                        row++;
                                        column = 1;
                                        if (row == 5) {
                                            break;
                                        }
                                    }
                                    continue;
                                }
                                friend = displayOrder.get(pi);
                                this.updateItem(row, column, new GUIItem(Material.SKULL_ITEM, String.format("&3&l%s", friend.getName()), 1, String.format(";&rStatus: &%s%s%s;;&r&cLeft-Click to remove them as a friend.", friend.getStatus().getColour(), friend.getStatus().getName(), ((friend.getServer() != null) ? String.format(";&rServer: **%s**", friend.getServer()) : "")), ((friend.getStatus() != FriendStatus.OFFLINE) ? (short) 3 : (short) 0), false, friend.getName()));
                                column++;
                                if (column == 8) {
                                    row++;
                                    column = 1;
                                    if (row == 5) {
                                        break;
                                    }
                                }
                            }

                            break;
                        case REQUESTS:
                            if (friend.getType() == Friend.FriendType.PENDING_INCOMING) {
                                if (clickType.isLeftClick()) {
                                    //Accepted

                                    int limit = 250;
                                    if (player.hasPermission("elite")) {
                                        limit += 50;
                                    }
                                    if (player.hasPermission("master")) {
                                        limit += 50;
                                    }

                                    if (player.getFriendsList().getFriends().values().size() + (int) player.getFriendsList().getPendingFriendRequests().values().stream().filter((friend2) -> friend2.getType() == Friend.FriendType.PENDING_OUTGOING).count() >= limit) {
                                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Friends", "You have already reached your Friends limit! Cancel pending outgoing friend requests or delete friends in order to add more!"));
                                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                                        return;
                                    }
                                    friendsList.friendRequestAccepted(friend.getUuid(), false, null, FriendStatus.OFFLINE, true);
                                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 1, 100);
                                    displayOrder.remove(friend);

                                    row = 1;
                                    column = 1;
                                    if (displayOrder.size() < ((currentPage) * 28)) {
                                        this.updateItem(5, 7, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lYour Friends", 1, "", (short) 7));
                                    }

                                    for (int i = 0; i < 28; i++) {
                                        //show the prev 28 items.
                                        int pi = (((currentPage - 1) * 28) + i);
                                        if (displayOrder.size() <= pi) {
                                            this.updateItem(row, column, null);
                                            column++;
                                            if (column == 8) {
                                                row++;
                                                column = 1;
                                                if (row == 5) {
                                                    break;
                                                }
                                            }
                                            continue;
                                        }
                                        friend = displayOrder.get(pi);
                                        this.updateItem(row, column, new GUIItem(((friend.getType() == Friend.FriendType.PENDING_INCOMING) ? Material.ENDER_PEARL : Material.EYE_OF_ENDER), String.format("&3&l%s", friend.getName()), 1, ((friend.getType() == Friend.FriendType.PENDING_INCOMING) ? String.format("&rIncoming request from **%s**.;;&r&aLeft-Click to accept.;&r&cRight-Click to deny.", friend.getName()) : String.format("&rOutgoing request to **%s**;;&r&cShift-Right-Click to revoke.", friend.getName()))));
                                        column++;
                                        if (column == 8) {
                                            row++;
                                            column = 1;
                                            if (row == 5) {
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    //Denied
                                    friendsList.friendRequestRemoved(friend.getUuid(), true);
                                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 1, 100);
                                    displayOrder.remove(friend);

                                    row = 1;
                                    column = 1;
                                    if (displayOrder.size() < ((currentPage) * 28)) {
                                        this.updateItem(5, 7, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lYour Friends", 1, "", (short) 7));
                                    }

                                    for (int i = 0; i < 28; i++) {
                                        //show the prev 28 items.
                                        int pi = (((currentPage - 1) * 28) + i);
                                        if (displayOrder.size() <= pi) {
                                            this.updateItem(row, column, null);
                                            column++;
                                            if (column == 8) {
                                                row++;
                                                column = 1;
                                                if (row == 5) {
                                                    break;
                                                }
                                            }
                                            continue;
                                        }
                                        friend = displayOrder.get(pi);
                                        this.updateItem(row, column, new GUIItem(((friend.getType() == Friend.FriendType.PENDING_INCOMING) ? Material.ENDER_PEARL : Material.EYE_OF_ENDER), String.format("&3&l%s", friend.getName()), 1, ((friend.getType() == Friend.FriendType.PENDING_INCOMING) ? String.format("&rIncoming request from **%s**.;;&r&aLeft-Click to accept.;&r&cRight-Click to deny.", friend.getName()) : String.format("&rOutgoing request to **%s**;;&r&cShift-Right-Click to revoke.", friend.getName()))));
                                        column++;
                                        if (column == 8) {
                                            row++;
                                            column = 1;
                                            if (row == 5) {
                                                break;
                                            }
                                        }
                                    }

                                }

                            } else {
                                if (clickType == ClickType.SHIFT_RIGHT) {
                                    //Revoked
                                    friendsList.friendRequestRemoved(friend.getUuid(), true);
                                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 1, 100);
                                    displayOrder.remove(friend);

                                    row = 1;
                                    column = 1;
                                    if (displayOrder.size() < ((currentPage) * 28)) {
                                        this.updateItem(5, 7, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lYour Friends", 1, "", (short) 7));
                                    }

                                    for (int i = 0; i < 28; i++) {
                                        //show the prev 28 items.
                                        int pi = (((currentPage - 1) * 28) + i);
                                        if (displayOrder.size() <= pi) {
                                            this.updateItem(row, column, null);
                                            column++;
                                            if (column == 8) {
                                                row++;
                                                column = 1;
                                                if (row == 5) {
                                                    break;
                                                }
                                            }
                                            continue;
                                        }
                                        friend = displayOrder.get(pi);
                                        this.updateItem(row, column, new GUIItem(((friend.getType() == Friend.FriendType.PENDING_INCOMING) ? Material.ENDER_PEARL : Material.EYE_OF_ENDER), String.format("&3&l%s", friend.getName()), 1, ((friend.getType() == Friend.FriendType.PENDING_INCOMING) ? String.format("&rIncoming request from **%s**.;;&r&aLeft-Click to accept.;&r&cRight-Click to deny.", friend.getName()) : String.format("&rOutgoing request to **%s**;;&r&cShift-Right-Click to revoke.", friend.getName()))));
                                        column++;
                                        if (column == 8) {
                                            row++;
                                            column = 1;
                                            if (row == 5) {
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                                }
                            }
                            break;
                    }


                }
            }
        }
    }

    private void refresh() {
        switch (view) {
            case FAVOURITE:
                List<Friend> onlineFriends = friendsList.getFriends().values().stream().filter((friend) -> ((friend.getType() == Friend.FriendType.FAVOURITE) && friend.isOnline())).collect(Collectors.toList());
                List<Friend> offlineFriends = friendsList.getFriends().values().stream().filter((friend) -> ((friend.getType() == Friend.FriendType.FAVOURITE) && !friend.isOnline())).collect(Collectors.toList());
                onlineFriends.sort(Comparator.comparing(Friend::getName));
                offlineFriends.sort(Comparator.comparing(Friend::getName));
                displayOrder.clear();
                displayOrder.addAll(onlineFriends);
                displayOrder.addAll(offlineFriends);
                break;
            case ALL:
            case DELETE:
                List<Friend> favouriteOnlineFriends = friendsList.getFriends().values().stream().filter((friend) -> ((friend.getType() == Friend.FriendType.FAVOURITE) && friend.isOnline())).collect(Collectors.toList());
                List<Friend> favouriteOfflineFriends = friendsList.getFriends().values().stream().filter((friend) -> ((friend.getType() == Friend.FriendType.FAVOURITE) && !friend.isOnline())).collect(Collectors.toList());
                List<Friend> normalOnlineFriends = friendsList.getFriends().values().stream().filter((friend) -> ((friend.getType() == Friend.FriendType.NORMAL) && friend.isOnline())).collect(Collectors.toList());
                List<Friend> normalOfflineFriends = friendsList.getFriends().values().stream().filter((friend) -> ((friend.getType() == Friend.FriendType.NORMAL) && !friend.isOnline())).collect(Collectors.toList());

                favouriteOnlineFriends.sort(Comparator.comparing(Friend::getName));
                normalOnlineFriends.sort(Comparator.comparing(Friend::getName));
                favouriteOfflineFriends.sort(Comparator.comparing(Friend::getName));
                normalOfflineFriends.sort(Comparator.comparing(Friend::getName));
                displayOrder.clear();
                displayOrder.addAll(favouriteOnlineFriends);
                displayOrder.addAll(normalOnlineFriends);
                displayOrder.addAll(favouriteOfflineFriends);
                displayOrder.addAll(normalOfflineFriends);
                break;
            case REQUESTS:
                displayOrder.clear();
                displayOrder.addAll(friendsList.getPendingFriendRequests().values());
                displayOrder.sort(Comparator.comparing(Friend::getName));
                break;
        }
    }

    public void reload() {
        refresh();
        int row = 1;
        int column = 1;
        for (int i = 0; i < 28; i++) {
            //show the prev 28 items.
            int pi = (((currentPage - 1) * 28) + i);
            if (view == FriendsView.STATUS) {
                if (statuses.size() <= pi) {
                    this.updateItem(row, column, null);
                    column++;
                    if (column == 8) {
                        row++;
                        column = 1;
                        if (row == 5) {
                            break;
                        }
                    }
                    continue;
                }
                FriendStatus status = statuses.get(pi);
                this.updateItem(row, column, new GUIItem(Material.PAPER, String.format("&%s&l%s", status.getColour(), status.getTitle()), 1, String.format(";&rClick here to display your;&rstatus as &%s%s&r.%s", status.getColour(), status.getName(), ((status.getPermission() != null) ? ((player.hasPermission(status.getPermission().getNode())) ? "" : ((status.getCustomPermissionText() != null) ? ";;&r&c" + status.getCustomPermissionText() : ";;&r&cYou do not have permission to set this status.")) : "")), (short) 0, (friendsList.getCurrentStatus() == status)));
            } else {
                if (displayOrder.size() <= pi) {
                    this.updateItem(row, column, null);
                    column++;
                    if (column == 8) {
                        row++;
                        column = 1;
                        if (row == 5) {
                            break;
                        }
                    }
                    continue;
                }
                Friend friend = displayOrder.get(pi);
                if (view == FriendsView.REQUESTS) {
                    this.updateItem(row, column, new GUIItem(((friend.getType() == Friend.FriendType.PENDING_INCOMING) ? Material.ENDER_PEARL : Material.EYE_OF_ENDER), String.format("&3&l%s", friend.getName()), 1, ((friend.getType() == Friend.FriendType.PENDING_INCOMING) ? String.format("&rIncoming request from **%s**.;;&r&aLeft-Click to accept.;&r&cRight-Click to deny.", friend.getName()) : String.format("&rOutgoing request to **%s**;;&r&cShift-Right-Click to revoke.", friend.getName()))));
                } else {
                    if (view == FriendsView.DELETE) {
                        this.updateItem(row, column, new GUIItem(Material.SKULL_ITEM, String.format("&3&l%s&r&b%s", friend.getName(), ((friend.getType() == Friend.FriendType.FAVOURITE) ? " ✰" : "")), 1, String.format(";&rStatus: &%s%s%s;;&r&cLeft-Click to remove them as a friend.", friend.getStatus().getColour(), friend.getStatus().getName(), ((friend.getServer() != null) ? String.format(";&rServer: **%s**", friend.getServer()) : "")), ((friend.getStatus() != FriendStatus.OFFLINE) ? (short) 3 : (short) 0), false, friend.getName()));
                    } else {
                        this.updateItem(row, column, new GUIItem(Material.SKULL_ITEM, String.format("&3&l%s&r&b%s", friend.getName(), ((friend.getType() == Friend.FriendType.FAVOURITE) ? " ✰" : "")), 1, String.format(";&rStatus: &%s%s%s;;&r&%sShift-Left-Click to %s them as a favourite friend.", friend.getStatus().getColour(), friend.getStatus().getName(), ((friend.getServer() != null) ? String.format(";&rServer: **%s**", friend.getServer()) : ""), ((friend.getType() == Friend.FriendType.FAVOURITE) ? 'c' : 'a'), ((friend.getType() == Friend.FriendType.FAVOURITE) ? "remove" : "add")), ((friend.getStatus() != FriendStatus.OFFLINE) ? (short) 3 : (short) 0), false, friend.getName()));
                    }
                }
            }
            column++;
            if (column == 8) {
                row++;
                column = 1;
                if (row == 5) {
                    break;
                }
            }
        }
    }
}
