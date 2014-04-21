BundleExtractor  
===============  
This is a demo and test bundle-extractor on OSGI environment.  
1. extract org.apache.felix.main.distribution-4.4.0.tar.gz for basic OSGI environment.  
2. Use maven to build bundle-extractor -> Place bundle-extractor into ${OSGI environment}/bundle  
3. copy listResources into ${OSGI environment}  
  - listResources has format : groupID, artifactID, version, destination, repoUrl  
4. Now start OSGI environment with below command:  
  
  $> java -DbundleExtractor.config=listResources -Dfelix.home=. -jar bin/felix.jar
