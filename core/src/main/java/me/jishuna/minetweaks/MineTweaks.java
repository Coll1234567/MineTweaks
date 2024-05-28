package me.jishuna.minetweaks;

import java.io.File;
import me.jishuna.jishlib.Debug;
import me.jishuna.jishlib.Plugin;
import me.jishuna.jishlib.message.Messages;
import me.jishuna.jishlib.util.Tasks;

public class MineTweaks extends Plugin {

    @Override
    public void onEnable(boolean reload) {
        Messages.initialize(new File(getDataFolder(), "messages.yml"));
        Tasks.runTimer(Registries.TWEAK::tick, 5);

        Debug.writeDebugLog(new File(getDataFolder(), "debug.txt"));
    }
}
