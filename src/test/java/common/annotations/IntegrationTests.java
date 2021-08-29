package common.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import common.configs.DataSourceTestConfig;
import common.configs.DatabaseTestExtension;

/**
 * Interface for adding all the annotations for a test class.
 * 
 * @author Sam Butler
 * @since July 31, 2021
 */
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(DatabaseTestExtension.class)
@ContextConfiguration(classes = DataSourceTestConfig.class)
@ActiveProfiles("test")
public @interface IntegrationTests {

}
