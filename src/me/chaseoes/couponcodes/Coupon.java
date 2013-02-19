package me.chaseoes.couponcodes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class Coupon {

    String code;

    public Coupon(String cod) {
        code = cod;
    }

    public boolean exists() {
        return CouponCodes.getInstance().getConfig().getString("coupons." + code + ".enabled") != null;
    }

    public boolean create() {
        if (exists()) {
            return false;
        }
        CouponCodes.getInstance().getConfig().set("coupons." + code + ".enabled", true);
        CouponCodes.getInstance().getConfig().set("coupons." + code + ".max-uses", 0);
        List<String> usedby = new ArrayList<String>();
        CouponCodes.getInstance().getConfig().set("coupons." + code + ".used-by", usedby);
        List<String> commands = new ArrayList<String>();
        commands.add("say %player reedemed a coupon!");
        CouponCodes.getInstance().getConfig().set("coupons." + code + ".run-commands", commands);
        CouponCodes.getInstance().saveConfig();
        return true;
    }
    
    public boolean isEnabled() {
        return CouponCodes.getInstance().getConfig().getBoolean("coupons." + code + ".enabled");
    }

    public int getMaxUses() {
        return CouponCodes.getInstance().getConfig().getInt("coupons." + code + ".max-uses");
    }

    public int getTimesUsed() {
        return CouponCodes.getInstance().getConfig().getStringList("coupons." + code + ".used-by").size();
    }

    public void setMaxUses(int i) {
        CouponCodes.getInstance().getConfig().set("coupons." + code + ".max-uses", i);
        CouponCodes.getInstance().saveConfig();
    }
    
    public List<String> getCommands() {
        return CouponCodes.getInstance().getConfig().getStringList("coupons." + code + ".run-commands");
    }
    
    public void setUsedBy(String player) {
        List<String> usedby = CouponCodes.getInstance().getConfig().getStringList("coupons." + code + ".used-by");
        usedby.add(player);
        CouponCodes.getInstance().getConfig().set("coupons." + code + ".used-by", usedby);
        CouponCodes.getInstance().saveConfig();
    }
    
    public boolean hasBeenUsedBy(String player) {
        List<String> usedby = CouponCodes.getInstance().getConfig().getStringList("coupons." + code + ".used-by");
        return usedby.contains(player);
    }
    
    public void reedem(Player player) {
        for (String com : getCommands()) {
            String command = com.replace("%player", player.getName());
            CouponCodes.getInstance().getServer().dispatchCommand(CouponCodes.getInstance().getServer().getConsoleSender(), command);
        }
        setUsedBy(player.getName());
    }

}
