package ch.epfl.xblast.client;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;

/**
 * This immutable class represents a collection of images that are provided by a
 * repository and indexed by an integer value.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class ImageCollection {

    /*
     * Attributes
     */
    private final Map<Integer, Image> images;

    /**
     * Only Constructor, taking the name of the repository that contains the
     * images as argument.
     * 
     * @param dirName
     *            name of the repository where the images are saved
     */
    public ImageCollection(String dirName) {
        Map<Integer, Image> images = new HashMap<>();
        
        try {
            // don't modify the following line!
            File directory = new File(ImageCollection.class
                                      .getClassLoader()
                                      .getResource(dirName)
                                      .toURI());

            for (File f : directory.listFiles()) {
                try {
                    int i = Integer.parseInt(f.getName().substring(0, 3));
                    images.put(i, ImageIO.read(f));
                } catch (NumberFormatException | IOException e) {
                    /*
                     * We don't want to abort the operation if one image fails
                     * to load.
                     */
                    e.printStackTrace();
                }
            }
        } catch (URISyntaxException e) {
            throw new Error(
                    "The directory named " + dirName + " doesn't exist!");
        }
        // we treat the images like they were immutable (even if they are not
        // really)
        this.images = Collections.unmodifiableMap(images);
    }

    /**
     * Method allowing to get an image for a specific integer value.
     * This method throws an exception if the image was not found.
     * 
     * @param imageNumber
     *            integer value characterizing an image
     * @return the image that corresponds to the integer value
     * @throws NoSuchElementException
     *             if no image corresponds to given index.
     */
    public Image image(int imageNumber) {
        Image i = imageOrNull(imageNumber);
        if (i == null)
            throw new NoSuchElementException(
                    "No image corresponds to given integer.");
        return i;
    }

    /**
     * Method allowing to get an image for a specific integer value.
     * This method return null if the image was not found.
     * 
     * @param imageNumber
     *            integer value characterizing an image
     * @return the image that corresponds to the integer value or null if no
     *         image corresponds to given index.
     */
    public Image imageOrNull(int imageNumber) {
        return images.get(imageNumber);
    }
}
