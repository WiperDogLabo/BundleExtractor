/*
 * This testcase is used for testing POST service from
 * bundle RESTApi
 * Download, extract bundle and return list of files in this bundle
 * Send POST request to install some files (not all)
 * Check cleaning up
 * */
 import groovy.json.*

def host = this.args[0]
def port = this.args[1]
assert host != null && host != '', 'Host must not be null or empty'
assert port != null && port != '', 'Host must not be null or empty'
println '================================================='
println '==            Testcase4 begins                 =='
// Get data
println "Getting data from ${host}:${port}/bundle/org.wiperdog.job.mysql/org.wiperdog.job.mysql/1.0"
def cmd = "curl -X GET ${host}:${port}/bundle/org.wiperdog.job.mysql/org.wiperdog.job.mysql/1.0"
def proc = cmd.execute()
proc.waitFor()
def dataTxt = proc.in.text
def slurper = new JsonSlurper()
def output = new JsonOutput()
def data = slurper.parseText(dataTxt)
println data

// Prepare data for POST request install files
// Install some files
def dataPost = []
for(def i = 0; i < data.listFiles.size(); i++){
	if(i%2 == 0){
		dataPost.add([index:i, path:data.listFiles[i]])
	}
}
println "Sending POST request to ${host}:${port}/bundle/install with data"
println output.toJson(dataPost)
cmd = "curl -X POST -d " + output.toJson(dataPost) + " ${host}:${port}/bundle/install"
proc = cmd.execute()
proc.waitFor()
dataTxt = proc.in.text
def result = slurper.parseText(dataTxt)
println "Response :\n" + result
println "Checking responsed data..."
result.each{res->
	assert res.index instanceof Number
	assert res.status instanceof Boolean
	assert (res.file != null && res.file != "")
}
println "Checking cleaning up function...(This check is only for localhost)"
dataPost.each{
	File jobFile = new File(it.path)
	assert (!jobFile.exists()), '$jobFile wasn\'t deleted.'
}
println '===============Testcase4 OK======================'

