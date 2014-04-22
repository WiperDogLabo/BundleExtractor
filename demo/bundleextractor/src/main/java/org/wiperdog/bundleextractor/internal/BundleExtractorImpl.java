package org.wiperdog.bundleextractor.internal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.wiperdog.bundleextractor.BundleExtractor;

public class BundleExtractorImpl implements BundleExtractor {
	private File listResources;
	public String MANIFEST_ATTRIBUTE = "Destination";

	public BundleExtractorImpl() {
		this.listResources = null;
	}

	public BundleExtractorImpl(File listResources) {
		this.listResources = listResources;
	}

	public File getListResources() {
		return listResources;
	}

	public boolean processListResources() {
		try {
			String line;
			BufferedReader br = new BufferedReader(new FileReader(listResources));
			while((line = br.readLine()) != null){
				String location = processResource(line);
				File gotBundle = new File(location);
				System.out.println(gotBundle.getName() + ":" + gotBundle.exists());
				extractPackage(gotBundle);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public String processResource(String line) {
		String[] values = line.split(",");
		String groupId = values[0];
		String artifactId = values[1];
		String version = values[2];
		String location = System.getProperty("felix.home") + values[3];
		String repo = values[4];
		if(!"".equals(groupId) && !"".equals(artifactId) && !"".equals(version) && !"".equals(location)){
			//Write and execute maven command to get bundle from maven repository
			StringBuffer sb = new StringBuffer();
			sb.append("mvn org.apache.maven.plugins:maven-dependency-plugin:2.8:get ");
			sb.append("-Dartifact=" + groupId+ ":" + artifactId +":" + version + " ");
			sb.append("-Ddest=" + location);
			if(!"".equals(repo)){
				sb.append(" -DrepoUrl=" + repo);
			}	
			String result = executeCommand(sb.toString());
			if(!"".equals(result)){
				System.out.println(result);
//				return artifactId + "-" + version + ".jar";
				return location;
			}
		}
		return null;
	}
	
	public void extractPackage(File packageBundle){
		try {
			JarFile jar = new JarFile(packageBundle);
			String destination = jar.getManifest().getMainAttributes().getValue(MANIFEST_ATTRIBUTE);
			if(destination == null || "".equals(destination)){
				System.out.println("Destination in MANIFEST is empty.");
				return;
			}
			// Make sure the folder is made
			File dest = new File(System.getProperty("felix.home") + destination);
			dest.mkdirs();
			Enumeration entries = jar.entries();
			while(entries.hasMoreElements()){
				JarEntry entry = (JarEntry) entries.nextElement();
				System.out.println("-Extract " + entry.getName());
				File f = new File(System.getProperty("felix.home") + destination + File.separator + entry.getName());
				if(entry.isDirectory()){
					f.mkdir();
					continue;
				}
				InputStream is = jar.getInputStream(entry);
				FileOutputStream fos = new FileOutputStream(f);
				while(is.available() > 0){
					fos.write(is.read());
				}
				fos.close();
				is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public String executeCommand(String cmd) {
		StringBuffer output = new StringBuffer();
		Process p;
		try {
			p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output.toString();
	}

	public void setListResources(File listResources) {
		this.listResources = listResources;
	}
}
