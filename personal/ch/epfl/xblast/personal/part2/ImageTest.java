package ch.epfl.xblast.personal.part2;

import static org.junit.Assert.*;

import java.awt.Image;
import java.util.NoSuchElementException;

import org.junit.Test;

import ch.epfl.xblast.client.ImageCollection;

public class ImageTest {

    @Test
    public void chargedTest() {
        ImageCollection images= new ImageCollection("player");
        Image c= images.image((byte) 0);
    }
    
    @Test(expected=NoSuchElementException.class)
    public void ExceptionTest() {
        ImageCollection images= new ImageCollection("player");
        Image c= images.image((byte) 74);
    }
    
    @Test
    public void nullTest() {
        ImageCollection images= new ImageCollection("player");
        Image c= images.imageOrNull((byte) 74);
        assertEquals(null,c);
    }
    
    @Test
    public void image() {
        ImageCollection images= new ImageCollection("player");
        Image c= images.imageOrNull((byte) 0);
        assertEquals(images.imageOrNull((byte) 0),c);
    }
    

}
