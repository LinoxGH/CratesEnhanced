package me.linoxgh.cratesenhanced.data.rewards;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemGroupReward implements Reward<ItemStack[]> {
    private ItemStack[] reward;
    private int weight;

    public ItemGroupReward(@NotNull ItemStack[] reward, int weight) {
        this.reward = reward;
        this.weight = weight;
    }

    public ItemGroupReward(@NotNull Map<String, Object> data) {
        this.reward = (ItemStack[]) data.get("reward");
        this.weight = (int) data.get("weight");
    }

    @Override
    public int getWeight() {
        return weight;
    }
    @Override
    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public boolean giveReward(@NotNull Player p, @NotNull Location crateLocation) {
        Location topLoc = crateLocation.set(crateLocation.getX(), crateLocation.getY() + 1D, crateLocation.getZ()).toCenterLocation();
        for (ItemStack reward : reward) {
            crateLocation.getWorld().spawnEntity(
                    topLoc,
                    EntityType.DROPPED_ITEM,
                    CreatureSpawnEvent.SpawnReason.CUSTOM,
                    (entity) -> {
                        Item item = (Item) entity;
                        item.setOwner(p.getUniqueId());
                        item.setCanMobPickup(false);
                        item.setCanPlayerPickup(true);
                        item.setWillAge(true);
                        item.setPickupDelay(20);
                        item.setItemStack(reward.clone());
                    }
            );
        }
        return true;
    }

    @Override
    public boolean giveReward(@NotNull Player p) {
        HashMap<Integer, ItemStack> unfits = p.getInventory().addItem(reward.clone());
        if (!(unfits.isEmpty())) {
            for (Map.Entry<Integer, ItemStack> entry : unfits.entrySet()) {
                p.getLocation().getWorld().dropItem(p.getLocation(), entry.getValue());
            }
        }
        return true;
    }

    @Override
    public ItemStack @NotNull [] getReward() {
        return reward;
    }

    @Override
    public void setReward(ItemStack @NotNull [] reward) {
        this.reward = reward;
    }

    @Override
    public @NotNull RewardType getRewardType() {
        return RewardType.ITEM_GROUP;
    }
}
