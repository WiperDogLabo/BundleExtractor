package org.wiperdog.bundleextractor.internal;

import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean processListResources() {
		// Read listResource file for list bundle to get and extract
		List<Map> listBundle = readResourceFile(listResources);
		
		// Process get and extract bundles
		for (Map bundle : listBundle) {
			String location = processResource(bundle);
			if(location != null && !"".equals(location)){
				File gotBundle = new File(location);
				System.out.println(gotBundle.getName() + ":" + gotBundle.exists());
				extractPackage(gotBundle);
			}
			bundle.put("getit", false);
		}
		
		// Rewrite listResource file for updating get status
		rewriteFile(listBundle);
		return true;
	}

	public String processResource(Map bundle) {
		boolean getit = (Boolean) bundle.get("getit");
		if(getit){
			String groupId = (String) bundle.get("groupId");
			String artifactId = (String) bundle.get("artifactId");
			String version = (String) bundle.get("version");
			String location = System.getProperty("felix.home") + (String) bundle.get("location");
			String repo = (String) bundle.get("repo");
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
	
	/**
	 * Rewrite to update get status 
	 * @param listBundle
	 */
	public void rewriteFile(List<Map> listBundle){
		try {
			FileWriter fw = new FileWriter(listResources.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			StringBuffer sb = new StringBuffer();
			for (Map bundle : listBundle) {
				for (Object key : bundle.keySet()) {
					sb.append(bundle.get(key) + ",");
				}
			}
			bw.write(sb.toString());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Read listResource file for list bundles to get and extract
	 * listResource file has format : groupID, artifactID, version, destination, repoUrl, get status(true/false)
	 * @param listResource
	 * @return list bundles
	 */
	@SuppressWarnings("unchecked")
	public List<Map> readResourceFile(File listResource){
		List<Map> listBundle = new ArrayList<Map>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(listResource));
			String line;
			while((line = br.readLine()) != null){
				String[] values = line.split(",");
				Map bundle = new LinkedHashMap();
				bundle.put("groupId", values[0]);
				bundle.put("artifactId", values[1]);
				bundle.put("version", values[2]);
				bundle.put("location", values[3]);
				bundle.put("repo", values[4]);
				bundle.put("getit", values[5] != null ? Boolean.valueOf(values[5]) : true);
				listBundle.add(bundle);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listBundle;
	}

	public void setListResources(File listResources) {
		this.listResources = listResources;
	}
}
