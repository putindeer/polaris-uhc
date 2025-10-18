package us.polarismc.polarisuhc.managers.scenario;

import org.bukkit.Material;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Scenario {
    String name();
    String author() default "Unknown";
    Material icon() default Material.BEDROCK;
    String[] description() default {};
}
