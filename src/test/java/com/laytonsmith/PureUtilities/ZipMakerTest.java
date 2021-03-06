package com.laytonsmith.PureUtilities;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author layton
 */
public class ZipMakerTest {
    File resourceDir;
    
    public ZipMakerTest() {
    }
    
    @Before
    public void setUp() throws URISyntaxException{
        URL url = ZipMakerTest.class.getResource("/test.txt");
        resourceDir = new File(new URI(url.toString())).getParentFile();        
    }
    
    @After
    public void tearDown(){
        File test = new File(resourceDir, "zippables.zip");
        if(test.exists()){
            test.deleteOnExit();
        }
    }
    
    @Test public void testMakingZip() throws IOException, URISyntaxException{
        ZipMaker.MakeZip(new File(resourceDir, "zippables"), "zippables.zip");
        File zippable = new File(resourceDir, "zippables.zip");
        assertTrue("The zip file doesn't exist!", zippable.exists());
        File inner = new File(resourceDir, "zippables.zip/test.txt");
        assertEquals("Hello World!", new ZipReader(inner).getFileContents().trim());
        File innerFile = new File(resourceDir, "zippables.zip/inner/test.txt");
        assertEquals("Hello World!", new ZipReader(innerFile).getFileContents().trim());
    }
}
