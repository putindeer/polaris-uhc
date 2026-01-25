package us.polarismc.polarisuhc.config.customcrafts;

import org.bukkit.Material;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CraftInfo {
    String id();

    Material icon();

    String displayName();

    String[] lore() default {};

    String[] removedVanillaRecipes() default {};
}
