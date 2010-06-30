/**
 *
 */
package com.atlassian.labs.bamboo.git;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates, deletes, cleans test directories.
 * @author David Matějček
 */
public class DirectoryController {

    private static final Logger LOG = LoggerFactory.getLogger(DirectoryController.class);

    private final File mainDirectory;
    private final File checkoutDirectory;

    public DirectoryController(String mainDirectory) {
        LOG.debug("DirectoryController(mainDirectory={})", mainDirectory);
        this.mainDirectory = new File(mainDirectory);
        this.checkoutDirectory = new File(this.mainDirectory, "checkout");
    }

    /**
     * @return the main directory name
     */
    public String getName() {
        return this.mainDirectory.getName();
    }

    /**
     * @return main directory descriptor
     */
    public File getMainDirectory() {
        return this.mainDirectory;
    }

    /**
     * @return checkout directory descriptor; it is always a subdirectory 'checkout' of the main directory
     */
    public File getCheckoutDirectory() {
        return this.checkoutDirectory;
    }

    /**
     * Deletes the main directory and all it's subdirectories
     */
    public void delete() {
        LOG.info("{}: delete()", getName());
        deleteDirectory(this.mainDirectory);
    }

    /**
     * Recreates the main directory and also recreates it's subdirectory checkout
     */
    public void clean() {
        LOG.info("{}: clean()", getName());
        deleteDirectory(this.mainDirectory);
        this.checkoutDirectory.mkdirs();
    }

    private static boolean deleteDirectory(File dir) {
        LOG.trace("deleteDirectory(dir={})", dir);

        if (!dir.exists()) {
            return true;
        }

        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDirectory(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

}
