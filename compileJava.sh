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

if [ "$1" != "" -a "$2" != "" -a "$3" != "" ]; then
	echo "here"
	cd "$1"
	if [ ! -d bin ]; then
		mkdir bin
	fi
	javac -verbose -d bin -sourcepath src/ "$2"
	jar cvfm "$3" manifest.md -C bin .
fi

#~ Usage
#~ Compile and deploy jar bundle of java
