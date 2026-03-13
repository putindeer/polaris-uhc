package us.polarismc.polarisuhc.config.toggle;

import org.bukkit.Material;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ToggleInfo {
    String id();
    Material icon();
    String displayName();
    String[] lore() default {};
    boolean listener() default true;
    boolean listenerWhenDisabled() default false;
}
