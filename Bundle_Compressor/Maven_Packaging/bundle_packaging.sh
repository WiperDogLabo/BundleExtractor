if [ "$#" -ne 5 ]; then
	echo "------------ERROR-----------------------"
    echo "  Illegal number of parameters"
    echo "  Usage: ./bundle_packaging.sh [resource directory] [destination] [groupId] [artifactId] [version]"
else
	command -v mvn >/dev/null 2>&1 || { echo "Maven not installed. Aborting." >&2; exit 1;}	
	command -v groovy >/dev/null 2>&1 || { echo "Groovy not installed. Aborting." >&2; exit 1;}	
	groovy bundle_packaging.groovy $1 $2 $3 $4 $5
fi

