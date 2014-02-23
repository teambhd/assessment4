package unitTests;

import static org.junit.Assert.*;

import java.io.IOException;

import logicClasses.Airport;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;

import util.DeferredFile;

public class DeferredFile_Test {
	private DeferredFile deferredFileInstance;

	@Before
	public void setUp() throws Exception {
		LoadingList.get().add(deferredFileInstance = new DeferredFile("res/graphics/new/airport.png"){
			public void loadFile(String filename) throws SlickException{
 
            }	
		});
	}


	@Test
	public void testGetDescription() {
		assertEquals("res/graphics/new/airport.png", deferredFileInstance.getDescription());
		
	}
	
	@Test
	public void testLoad() throws IOException {
		String filename = "dummy";
		boolean throwsException;
		try {	//handle errors here to avoid duplicating try/catch
			deferredFileInstance.loadFile(filename);
		} catch (SlickException e) {
			throwsException = true;
			assertEquals(true, throwsException);
			throw new IOException("error loading:\t" +filename);
			
		}
		
	}

}
