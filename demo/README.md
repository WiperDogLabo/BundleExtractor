BundleExtractor  
===============  
This is a demo and test bundle-extractor on OSGI environment.  
Explaination:  
- lib folder is resource folder for testing compress, it contains resource files and a custom manifest file manifest.md  
- manifest.md contains information about destination in Main-class entry  
- Use bundle-extractor.sh to compress resource folder
    $> bundle-extractor.sh -c lib/groovy org.wiperdog.lib.groovy-1.1.jar  
    
- Deploy jar to repository 
- After that, do the following steps for testing get from repository and extract using information in MANIFEST.MF  

1. extract org.apache.felix.main.distribution-4.4.0.tar.gz for basic OSGI environment.  
2. Use maven to build bundle-extractor -> Place bundle-extractor into ${OSGI environment}/bundle  
3. copy listResources into ${OSGI environment}  
  - listResources has format : groupID, artifactID, version, destination, repoUrl  
  
4. Now start OSGI environment with below command:  
  
  $> java -DbundleExtractor.config=listResources -Dfelix.home=. -jar bin/felix.jar
