package me.chaseoes.couponcodes;

import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CouponCodes extends JavaPlugin {

    private static CouponCodes instance;

    public static CouponCodes getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public void loadCoupons() {

    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        if (cmnd.getName().equalsIgnoreCase("coupon")) {
            if (strings.length == 0) {
                cs.sendMessage(ChatColor.GREEN + "CouponCodes version " + getDescription().getVersion() + " by chaseoes.");
                cs.sendMessage(ChatColor.GOLD + "http://dev.bukkit.org/server-mods/couponcodes/");
                return true;
            }

            if (strings[0].equalsIgnoreCase("reload")) {
                if (cs.hasPermission("couponcodes.reload")) {
                    reloadConfig();
                    saveConfig();
                    cs.sendMessage(ChatColor.GREEN + "Successfully reloaded all coupons.");
                } else {
                    cs.sendMessage(ChatColor.RED + "You don't have permission to do that.");
                }
            }

            if (strings[0].equalsIgnoreCase("add")) {
                if (cs.hasPermission("couponcodes.add")) {
                    if (strings.length > 1) {
                        String code;
                        if (strings[1].equalsIgnoreCase("random")) {
                            code = RandomStringUtils.randomAlphanumeric(8);
                        } else {
                            code = strings[1];
                        }

                        Coupon c = new Coupon(code);
                        if (c.create()) {
                            if (strings.length > 2) {
                                c.setMaxUses(Integer.parseInt(strings[2]));
                            }
                            cs.sendMessage(ChatColor.GREEN + "Successfully created a new coupon with the code " + strings[1] + ".");
                        } else {
                            cs.sendMessage(ChatColor.RED + "A coupon with the code " + strings[1] + " already exists.");
                        }
                    } else {
                        cs.sendMessage(ChatColor.RED + "Usage: /coupon add <code> [max # of uses]");
                    }
                } else {
                    cs.sendMessage(ChatColor.RED + "You don't have permission to do that.");
                }
            }

            if (strings[0].equalsIgnoreCase("reedem")) {
                if (cs.hasPermission("couponcodes.reedem")) {
                    if (strings.length > 1) {
                        Coupon c = new Coupon(strings[1]);
                        if (c.exists()) {
                            if (!c.hasBeenUsedBy(cs.getName())) {
                                if (c.getTimesUsed() + 1 <= c.getMaxUses() || c.getMaxUses() == 0) {
                                    if (c.isEnabled()) {
                                        c.reedem((Player) cs);
                                        cs.sendMessage(ChatColor.GREEN + "Successfully reedemed that coupon!");
                                    } else {
                                        cs.sendMessage(ChatColor.RED + "That coupon can not be used at this time.");
                                    }
                                } else {
                                    cs.sendMessage(ChatColor.RED + "That coupon can no longer be used.");
                                }
                            } else {
                                cs.sendMessage(ChatColor.RED + "You have already used that coupon.");
                            }
                        } else {
                            cs.sendMessage(ChatColor.RED + "That coupon does not exist.");
                        }
                    } else {
                        cs.sendMessage(ChatColor.RED + "Usage: /coupon reedem <code>");
                    }
                } else {
                    cs.sendMessage(ChatColor.RED + "You don't have permission to do that.");
                }
            }
        }
        return true;
    }

}
