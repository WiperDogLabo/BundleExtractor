/*
 * This testcase is used for testing GET service from
 * bundle RESTApi
 * Download, extract bundle and return list of files in this bundle
 * */

import groovy.json.*

def host = this.args[0]
def port = this.args[1]
assert host != null && host != '', 'Host must not be null or empty'
assert port != null && port != '', 'Host must not be null or empty'
println '================================================='
println '==            Testcase1 begins                 =='
println "Getting data from ${host}:${port}/bundle/org.wiperdog.job.mysql/org.wiperdog.job.mysql/1.0"
def cmd = "curl -X GET ${host}:${port}/bundle/org.wiperdog.job.mysql/org.wiperdog.job.mysql/1.0"
def proc = cmd.execute()
proc.waitFor()
def dataTxt = proc.in.text
def slurper = new JsonSlurper()
def data = slurper.parseText(dataTxt)
println data
println "Checking data..."
assert data != null, 'Fail to get data'
println "Checking data type..."
assert (data instanceof Map), 'Data is not a Map'
println "Checking data\'s key..."
data.keySet().each{key->
	assert (key == 'status' || key == 'listFiles'), 'Set of keys is invalid'
}
println "Checking detail data..."
assert data.status == 'success', 'This request must success but it failed'
assert (data.listFiles instanceof List<String>), 'List of files is invalid'
println '================Testcase1 OK====================='
