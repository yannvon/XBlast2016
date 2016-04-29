package ch.epfl.xblast.client;

import java.awt.Image;
import java.io.File;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;

/**
 * This immutable class represents a collection of images that are provided by
 * repository and indexed by an integer value.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class ImageCollection {
    
    private final Map<Integer,Image> images;

    /**
     * Only Constructor, taking the name of the repository that contains the
     * images as argument.
     * 
     * @param repository
     *            name of the repository where the images are saved
     */
    public ImageCollection(String repository) { // FIXME error handling!

        Map<Integer, Image> images = new HashMap<>();
        try {
            // don't modify the following line!
            File directory = new File(ImageCollection.class
                                        .getClassLoader()
                                        .getResource(repository)
                                        .toURI());

            for (File f : directory.listFiles()) {
                try {
                    Integer i = Integer.parseInt(f.getName().substring(0, 3));
                    images.put(i, ImageIO.read(f));
                } catch (Exception e) {
                    // do nothing, we don't want to abort the operation if one image fails to load.
                }
            }
        } catch (URISyntaxException e) {
            throw new Error(
                    "The directory named " + repository + " doesn't exist!");
        }
        // we treat the images like they were immutable
        this.images = Collections.unmodifiableMap(images);
    }

    /**
     * Method allowing to get an image for a specific int value.
     * 
     * @param imageNumber
     *            int value characterizing an image
     * @return the image that corresponds to the int value
     * @throws NoSuchElementException
     *             if no image corresponds to given index.
     */
    public Image image(int imageNumber) {
        Image i = imageOrNull(imageNumber);
        if (i == null)
            throw new NoSuchElementException();
        return i;
    }

    /**
     * Method allowing to get an image for a specific byte value.
     * 
     * @param imageNumber
     *            byte value characterizing an image
     * @return the image that corresponds to the byte value or null if no image
     *         corresponds to given index.
     */
    public Image imageOrNull(int imageNumber) {
        return images.get(imageNumber);
    }
}
