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
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class ImageCollection {
    
    private final Map<Byte,Image> images;
    
    /**
     * @param repository
     * @throws URISyntaxException 
     */
    public ImageCollection(String repository) throws URISyntaxException{    //FIXME throw or try/catch?
        // don't modify
        Map<Byte, Image> images = new HashMap<>();
        File directory = new File(ImageCollection.class.getClassLoader()
                .getResource(repository).toURI());

        for (File f : directory.listFiles()) {
            try {
                Byte b = Byte.parseByte(f.getName().substring(0, 3));
                images.put(b, ImageIO.read(f));
            } catch (Exception e) {
                // do nothing
            }
        }
        this.images = Collections.unmodifiableMap(images);
        
    }
    
    /**
     * @param imageNumber
     * @return
     */
    public Image image(byte imageNumber){
        Image im=imageOrNull(imageNumber);
        if (im ==null)
            throw new NoSuchElementException();
        return im;
    }
    
    
    /**
     * @param imageNumber
     * @return
     */
    public Image imageOrNull(byte imageNumber){
        
       return images.get(imageNumber);
    }

}
