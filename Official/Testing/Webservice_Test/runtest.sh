if [ $# -lt 2 ]
  then
    echo "Please call with arguments"
    echo './runtest.sh ${host} ${port}'
	exit
fi

if [ ! -n "which groovy" ]
then
	echo "You need groovy to run test"
	echo "Installing groovy"
	sudo apt-get install groovy
fi
host=$1
port=$2
groovy testcase1.groovy $host $port
groovy testcase2.groovy $host $port
groovy testcase3.groovy $host $port
groovy testcase4.groovy $host $port
