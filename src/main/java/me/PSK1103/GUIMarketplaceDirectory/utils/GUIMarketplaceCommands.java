package me.PSK1103.GUIMarketplaceDirectory.utils;

import me.PSK1103.GUIMarketplaceDirectory.GUIMarketplaceDirectory;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GUIMarketplaceCommands implements TabExecutor {

    final GUIMarketplaceDirectory plugin;

    public GUIMarketplaceCommands(GUIMarketplaceDirectory plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (label.equals("guimarketplacedirectory") || label.equals("gmd") || label.equals("guimd")) {
            if (args.length >= 3) {
                if (args[0].equals("search")) {
                    switch (args[1]) {
                        case "shop":
                        case "s":
                            StringBuilder key = new StringBuilder();
                            for (int i = 2; i < args.length - 1; i++) {
                                key.append(args[i]);
                                key.append(' ');
                            }
                            key.append(args[args.length - 1]);
                            plugin.gui.openRefinedShopPageByName(((Player) commandSender), key.toString());
                            return true;

                        case "item":
                        case "i":
                            StringBuilder key1 = new StringBuilder();
                            for (int i = 2; i < args.length - 1; i++) {
                                key1.append(args[i]);
                                key1.append(' ');
                            }
                            key1.append(args[args.length - 1]);
                            plugin.gui.openRefinedItemInventory(((Player) commandSender), key1.toString());
                            return true;

                        case "player":
                        case "p":
                            plugin.gui.openRefinedShopPageByPlayer(((Player) commandSender), args[2]);
                            return true;
                    }
                }
            }
            if (args.length == 2) {
                switch (args[0]) {
                    case "moderate":
                    case "m":
                        if (!commandSender.hasPermission("GUIMD.moderate")) {
                            commandSender.sendMessage(ChatColor.RED + "You do not have permissions to moderate the marketplace directory");
                            return true;
                        }
                        if (!plugin.getCustomConfig().getBoolean("moderate-directory", false)) {
                            commandSender.sendMessage(ChatColor.RED + "This feature is not enabled! Enable it from the config");
                            return true;
                        }
                        switch (args[1]) {
                            case "pending":
                            case "p":
                                plugin.gui.openShopDirectoryModerator(((Player) commandSender), 1);
                                return true;

                            case "review":
                                plugin.gui.openShopDirectoryModerator(((Player) commandSender), 2);
                                return true;

                            case "recover":
                                plugin.gui.openShopDirectoryModerator((Player) commandSender, 3);
                                return true;
                        }
                        break;
                }
            }
            if (args.length == 1) {
                switch (args[0]) {
                    case "help":
                        //TODO: add help message
                        commandSender.sendMessage(ChatColor.LIGHT_PURPLE + "=============GUIMarketplaceDirectory v" + plugin.getDescription().getVersion() + "=============");
                        commandSender.sendMessage(ChatColor.GOLD + "/guimd search [item/player/shop] (key): " + ChatColor.GREEN + "Search for items via item name or shops via shop name/player name");
                        if(commandSender.hasPermission("GUIMD.moderate")) {
                            commandSender.sendMessage(ChatColor.GOLD + "/guimd  moderate pending: " + ChatColor.GREEN + "Shows shops requiring approval");
                            commandSender.sendMessage(ChatColor.GOLD + "/guimd  moderate review: " + ChatColor.GREEN + "Shows active shops for removal if deemed objectionable");
                            commandSender.sendMessage(ChatColor.GOLD + "/guimd  moderate recover: " + ChatColor.GREEN + "Shows active shops for recovering a copy of the [shop init] book if the owner loses their copy");
                            commandSender.sendMessage(ChatColor.GOLD + "/guimd reload: " + ChatColor.GREEN + "Refreshes the plugin");
                        }
                        return true;

                    case "r":
                    case "reload":
                        if (!(commandSender instanceof ConsoleCommandSender) && !commandSender.hasPermission("GUIMD.moderate")) {
                            commandSender.sendMessage(ChatColor.RED + "You do not have permissions to reload the marketplace directory config");
                            return true;
                        }
                        plugin.reloadCustomConfig();
                        return true;
                }
            }
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (alias.equals("gmd") || alias.equals("guimd") || alias.equals("guimarketplacedirectory")) {
            List<String> hints = new ArrayList<>();

            if (args.length == 1) {
                if (args[0].length() == 0) {
                    hints.add("help");
                    if (commandSender.hasPermission("GUIMD.moderate")) {
                        hints.add("moderate");
                        hints.add("reload");
                    }
                    hints.add("search");
                } else {
                    if ("moderate".startsWith(args[0]) && commandSender.hasPermission("GUIMD.moderate")) {
                        if (args[0].equals("moderate")) {
                            hints.add("pending");
                            hints.add("recover");
                            hints.add("review");
                        } else
                            hints.add("moderate");
                    } else if ("reload".startsWith(args[0])) {
                        hints.add("reload");
                    } else if ("search".startsWith(args[0])) {
                        if (args[0].equals("search")) {
                            hints.add("item");
                            hints.add("player");
                            hints.add("shop");
                        } else
                            hints.add("search");
                    } else if ("help".startsWith(args[0]) && !args[0].equals("help"))
                        hints.add("help");
                }
            }

            if (args.length == 2) {
                if (args[0].equals("moderate") && commandSender.hasPermission("GUIMD.moderate")) {
                    if (args[1].length() == 0) {
                        hints.add("pending");
                        hints.add("recover");
                        hints.add("review");
                    } else {
                        if ("pending".startsWith(args[1])) {
                            if (!args[1].equals("pending")) {
                                hints.add("pending");
                            }
                        }
                        if ("recover".startsWith(args[1])) {
                            if (!args[1].equals("recover")) {
                                hints.add("recover");
                            }
                        }
                        if ("review".startsWith(args[1])) {
                            if (!args[1].equals("review")) {
                                hints.add("review");
                            }
                        }
                    }
                }

                if (args[0].equals("search")) {
                    if (args[1].length() == 0) {
                        hints.add("item");
                        hints.add("player");
                        hints.add("shop");
                    } else {
                        if ("item".startsWith(args[1])) {
                            if (!args[1].equals("item")) {
                                hints.add("item");
                            }
                        }
                        if ("player".startsWith(args[1])) {
                            if (!args[1].equals("player")) {
                                hints.add("player");
                            }
                        }
                        if ("shop".startsWith(args[1])) {
                            if (!args[1].equals("shop")) {
                                hints.add("shop");
                            }
                        }
                    }
                }
            }

            if (args.length == 0) {
                hints.add("help");
                if (commandSender.hasPermission("GUIMD.moderate")) {
                    hints.add("moderate");
                    hints.add("reload");
                }
                hints.add("search");
            }

            return hints;

        }
        return null;
    }
}
