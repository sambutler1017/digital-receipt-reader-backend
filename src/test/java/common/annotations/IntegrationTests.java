package common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

import common.configs.DatabaseTestExtension;

/**
 * Interface for adding all the annotations for a test class.
 * 
 * @author Sam Butler
 * @since July 31, 2021
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest
@ExtendWith(DatabaseTestExtension.class)
public @interface IntegrationTests {

}
