self="$0"
while [ -h "$self" ]; do
        res=`ls -ld "$self"`
        ref=`expr "$res" : '.*-> \(.*\)$'`
        if expr "$ref" : '/.*' > /dev/null; then
                self="$ref"
        else
                self="`dirname \"$self\"`/$ref"
        fi
done
dir=`dirname "$self"`
CUR_DIR=`cd "$dir/" && pwd`

function usage
(
	echo "===Bundle Extractor===="
	echo "Compress directories into bundle format for pushing maven repository"
	echo "	./bundle-extractor.sh -c <path> <bundle-name>"
	echo "Decompress bundle got from maven"
	echo "	./bundle-extractor.sh -d <bundle-name>"
	echo "If in <path> directory has manifest.mf then it will include manifest.mf(custom external manifest) file to META-INF/MANIFEST.MF"
	echo "======================="
)

if [ "$1" = "-c" ]; then
	if [ "$2" != "" -a "$3" != "" ]; then
		cd "$2"
		if [ ! -f manifest.mf ]; then
			jar cvf "$3" -C . .
			exit 0
		else
			jar cvfm "$3" manifest.mf -C . .
			exit 0
		fi
	else
		usage
		exit 0
	fi
fi

if [ "$1" = "-d" ]; then
	if [ "$2" != "" ]; then
		jar -xvf "$2"
		exit 0
	else 
		usage
		exit 0
	fi
else
	usage
fi
