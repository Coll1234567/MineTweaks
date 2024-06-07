package me.jishuna.minetweaks.tweak;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import me.jishuna.jishlib.Plugin;
import me.jishuna.jishlib.util.ClassScanner;
import me.jishuna.jishlib.util.Registry;

public class TweakRegistry extends Registry<String, Tweak> {
    private final Set<TickingTweak> tickingTweaks = new HashSet<>();
    private final Set<ToggleableTweak> toggleableTweaks = new TreeSet<>(Comparator.comparing(Tweak.class::cast));

    public TweakRegistry() {
        new ClassScanner<>(this.getClass().getClassLoader(), Tweak.class, RegisterTweak.class)
                .forEach((Consumer<Tweak>) this::register);

        reload();
    }

    public void reload() {
        this.tickingTweaks.clear();
        this.toggleableTweaks.clear();
        Plugin.getInstance().getEventBus().cleanup();

        getValues().forEach(tweak -> {
            tweak.reload();

            if (!tweak.isEnabled()) {
                return;
            }

            if (tweak instanceof ToggleableTweak toggle) {
                this.toggleableTweaks.add(toggle);
            }

            if (tweak instanceof TickingTweak ticking) {
                this.tickingTweaks.add(ticking);
            }

            tweak.registerEvents(Plugin.getInstance().getEventBus());
        });
    }

    public void register(Tweak tweak) {
        register(tweak.getName(), tweak, true);
    }

    public void tick() {
        this.tickingTweaks.forEach(TickingTweak::tick);
    }

    public Set<ToggleableTweak> getToggleableTweaks() {
        return this.toggleableTweaks;
    }
}
