package org.wiperdog.bundleextractor;

import java.io.File;

public interface BundleExtractor {
	
	public File getListResources();
	
	public void setListResources(File listResources);
	
	public String executeCommand(String cmd);
	
	public boolean processListResources();
}
