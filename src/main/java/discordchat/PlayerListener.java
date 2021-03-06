package discordchat;

import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.lang.TextContainer;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;

public class PlayerListener implements Listener {

    private String lastName;
    private String lastMessage;
    private String lastMessage2;
    private String lastMessage3;
    private String lastMessage4;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (Main.config.getBoolean("joinMessages")) API.sendMessage("**[" + Server.getInstance().getOnlinePlayers().size() + '/' + Server.getInstance().getMaxPlayers() + "]** " + Main.config.getString("info_player_joined").replace("%player%", e.getPlayer().getName()));

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (Main.config.getBoolean("quitMessages") && e.getPlayer().spawned) {
            API.sendMessage("**[" + (Server.getInstance().getOnlinePlayers().size() - 1) + '/' + Server.getInstance().getMaxPlayers() + "]** " + Main.config.getString("info_player_left").replace("%player%", e.getPlayer().getName()));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent e) {
        if (Main.config.getBoolean("deathMessages")) API.sendMessage(("**[" + Server.getInstance().getOnlinePlayers().size() + '/' + Server.getInstance().getMaxPlayers() + "]** " + Main.config.getString("info_player_death").replace("%death_message%", TextFormat.clean(textFromContainer(e.getDeathMessage()))));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(PlayerChatEvent e) {
        if (!e.isCancelled()) {
            if (!Main.config.getBoolean("enableMinecraftToDiscord")) return;
            String msg = e.getMessage();
            String name = e.getPlayer().getName();
            if (Main.config.getBoolean("spamFilter")) {
                if (msg.startsWith("Horion - the best minecraft bedrock utility mod - ")) return;
                if (msg.equals(lastMessage) && name.equals(lastName)) return;
                if (msg.equals(lastMessage2) && name.equals(lastName)) return;
                if (msg.equals(lastMessage3) && name.equals(lastName)) return;
                if (msg.equals(lastMessage4) && name.equals(lastName)) return;
                lastMessage4 = lastMessage3;
                lastMessage3 = lastMessage2;
                lastMessage2 = lastMessage;
                lastMessage = msg;
                lastName = name;
                msg = msg.replace("@", "");
            }
            API.sendMessage("**[" + Server.getInstance().getOnlinePlayers().size() + '/' + Server.getInstance().getMaxPlayers() + "]** " + name + " \u00BB " + msg));
        }
    }

    private static String textFromContainer(TextContainer container) {
        if (container instanceof TranslationContainer) {
            return Server.getInstance().getLanguage().translateString(container.getText(), ((TranslationContainer) container).getParameters());
        }
        return container.getText();
    }
}
