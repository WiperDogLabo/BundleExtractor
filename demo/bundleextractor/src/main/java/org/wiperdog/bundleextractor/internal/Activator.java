package org.wiperdog.bundleextractor.internal;

import java.io.File;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.wiperdog.bundleextractor.BundleExtractor;

public class Activator implements BundleActivator{

	public void start(BundleContext context) throws Exception {
		System.out.println("Bundle extractor to the rescue!");
		String configFilePath = System.getProperty("bundleExtractor.config");
		File listResources = new File(configFilePath);
		System.out.println("File config is " + listResources.getName());
		if(listResources.exists()){
			BundleExtractor instance = new BundleExtractorImpl(listResources);
			context.registerService(BundleExtractor.class.getName(), instance, null);
			instance.processListResources();
		}
	}

	public void stop(BundleContext context) throws Exception {
		System.out.println("Bye :(");
	}

}
