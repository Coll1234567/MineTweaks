package me.jishuna.minetweaks;

import java.io.File;
import me.jishuna.jishlib.Debug;
import me.jishuna.jishlib.Plugin;
import me.jishuna.jishlib.ServerMode;
import me.jishuna.jishlib.command.Commands;
import me.jishuna.jishlib.message.Messages;
import me.jishuna.jishlib.util.Tasks;
import me.jishuna.minetweaks.command.MineTweaksCommand;

public class MineTweaks extends Plugin {

    @Override
    public void onEnable(boolean reload) {
        Messages.initialize("messages.json");
        Commands.register(new MineTweaksCommand());

        Tasks.runTimer(Registries.TWEAK::tick, 5);

        Debug.writeDebugLog(new File(getDataFolder(), "debug.txt"));

        System.out.println(ServerMode.getServerMode());
    }

    @Override
    public void reloadConfig() {
        Messages.reload();
        Registries.TWEAK.reload();
    }
}
