package common.configs;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.digital.receipt.service.activeProfile.ActiveProfile;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

/**
 * Base Test configuration class.
 * 
 * @author Sam Butler
 * @since August 29, 2021
 */
@Service
@ComponentScan(basePackages = "com.digital.receipt")
public class DatabaseTestProfile {
    protected final String DB_URL = "jdbc:mysql://databasePI.ddnsfree.com/%s?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    protected Properties prop;
    protected ActiveProfile activeProfile;

    /**
     * Default constructor for initializing properties file.
     */
    public DatabaseTestProfile() {
        this.activeProfile = new ActiveProfile();
        this.prop = new Properties();
        initPropertiesFile();
    }

    /**
     * Gets the username for the database based on the environment.
     * 
     * @return {@link String} of the username to use.
     */
    protected String getUsername() {
        return System.getProperty("APP_ENVIRONMENT") != null ? System.getProperty("MYSQL_USERNAME")
                : prop.getProperty("spring.datasource.username");
    }

    /**
     * Gets the password for the database based on the environment.
     * 
     * @return {@link String} of the password to use.
     */
    protected String getPassword() {
        return System.getProperty("APP_ENVIRONMENT") != null ? System.getProperty("MYSQL_PASSWORD")
                : prop.getProperty("spring.datasource.password");
    }

    /**
     * Init the properties test file.
     */
    protected void initPropertiesFile() {
        try (InputStream input = new FileInputStream(activeProfile.getLocalTestPropertyFilePath())) {
            prop.load(input);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
