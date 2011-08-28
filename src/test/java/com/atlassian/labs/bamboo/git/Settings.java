/**
 *
 */
package com.atlassian.labs.bamboo.git;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author David Matějček
 */
public class Settings {

    private static final Logger LOG = LoggerFactory.getLogger(Settings.class);

    private static Properties settings;

    static {
        init();
    }


    private static void init() {
        InputStream is = null;
        try {
            ClassLoader loader = Settings.class.getClassLoader();
            is = loader.getResourceAsStream("test.properties");
            settings = new Properties();
            settings.load(is);
        } catch (IOException e) {
            LOG.error("Cannot load test.properties", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOG.warn("Cannot close test.properties inputstream", e);
                }
            }
        }
    }


    public static String getMasterRepositoryDir() {
        return settings.getProperty("test.repo.master.directory", "masterRepo");
    }


    public static String getCloneRepositoryDir() {
        return settings.getProperty("test.repo.clone.directory", "testRepo");
    }

}
