package de.logilutions.orav.teamchest;

import de.logilutions.orav.database.DatabaseHandler;
import de.logilutions.orav.player.OravPlayer;
import de.logilutions.orav.player.OravPlayerManager;
import de.logilutions.orav.team.OravTeam;
import de.logilutions.orav.util.MessageManager;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

@RequiredArgsConstructor
public class TeamChestListener implements Listener {
    private final OravPlayerManager oravPlayerManager;
    private final TeamChestManager teamChestManager;
    private final MessageManager messageManager;
    private final DatabaseHandler databaseHandler;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        OravPlayer oravPlayer = oravPlayerManager.getPlayer(event.getPlayer().getUniqueId());
        if (oravPlayer == null) {
            return;
        }
        if (event.getHand() == EquipmentSlot.OFF_HAND || event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock.getType() != Material.CHEST) {
            return;
        }
        Long teamId = teamChestManager.getTeamOfChest(clickedBlock.getLocation());
        if (teamId != null && !oravPlayer.getOravTeam().getId().equals(teamId)) {
            event.setCancelled(true);
            OravTeam oravTeam = databaseHandler.readTeam(teamId);
            event.getPlayer().sendMessage(messageManager.getPrefix() + " " + "Das ist die TeamKiste von "
                    + oravTeam.getTeamColor().getChatColor().toString()  + oravTeam.getName());
        }
    }

    @EventHandler
    private void onSignChange(SignChangeEvent event) {
        OravPlayer oravPlayer = oravPlayerManager.getPlayer(event.getPlayer().getUniqueId());
        if (oravPlayer == null) {
            return;
        }
        String line0 = event.getLine(0).toLowerCase();
        if (!(line0.contains("team") && line0.contains("kiste"))) {
            return;
        }
        Sign sign = (Sign) event.getBlock().getState();
        if (!(sign.getBlockData() instanceof WallSign)) {
            return;
        }
        WallSign wallSign = (WallSign) sign.getBlockData();
        Block behind = event.getBlock().getRelative(wallSign.getFacing().getOppositeFace());
        if (behind.getType() != Material.CHEST) {
            return;
        }
        if (teamChestManager.setChest(oravPlayer.getOravTeam(), behind.getLocation())) {
            messageManager.sendMessage(event.getPlayer(), "Du hast die Teamkiste erfolgreich gesetzt!");
        } else {
            messageManager.sendMessage(event.getPlayer(), "Ihr habt bereits eine Teamkiste!");
        }
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getType() != Material.CHEST) {
            return;
        }
        teamChestManager.removeChest(block.getLocation());
    }

    @EventHandler
    private void onBlockBreak(BlockBurnEvent event) {
        Block block = event.getBlock();
        if (block.getType() != Material.CHEST) {
            return;
        }
        teamChestManager.removeChest(block.getLocation());
    }
}
