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
    String displayName() default "";
    String author() default "Unknown";
    String command() default "";
    String[] authors() default {};
    String[] commands() default {};
    Material icon() default Material.BEDROCK;
    String[] description() default {};
    ScenarioType[] incompatibleWith() default {};
    boolean inDevelopment() default false;
    int priority() default 1;
    boolean enablesNetherInMeetup() default false;
    boolean disablesOverworld() default false;
    boolean enablesMiningInMeetup() default false;
}
