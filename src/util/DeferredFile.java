package util;

import java.io.IOException;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.DeferredResource;


public abstract class DeferredFile implements DeferredResource {
	private final String filename;
	
	public DeferredFile(String filename) {
		this.filename = filename;
	}
	
	//to be overridden; use to load the resource as needed
	public abstract void loadFile(String filename) throws SlickException;
	
	
	@Override
	public final void load() throws IOException{
		try {	//handle errors here to avoid duplicating try/catch
			loadFile(filename);
		} catch (SlickException e) {
			throw new IOException("error loading:\t" +filename);
		}
	}

	@Override
	public final String getDescription() {
		return filename;
	}

}
