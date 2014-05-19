Bundle Compressor
=============================
We create 2 methods to packaging resources file into bundle : 

 - Java_Packaing/ : Using java command to packaging

 	Usage : ./bundle_extractor.sh -c [resources directory] [bundle name]

 	 * Note:  need to create an manifest.mf file with "Destination" attribute in resources directory (This attribute specifice location for bundleextrator extract files to that)

 - Maven_Packing/ : Using maven to packaging
 	Usage : ./bundle_packaging.sh [resources directory] [destination] [groupId] [artifactId] [version]