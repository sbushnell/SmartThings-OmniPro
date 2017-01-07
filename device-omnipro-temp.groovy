/**
 *  OmniPro Temp
 *
 *  Copyright 2016 Ryan Wagoner
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
	definition (name: "OmniPro Temp", namespace: "Scott Bushnell", author: "Scott Bushnell") {
		capability "Temperature Measurement"
		capability "Refresh"
	}

	simulator {
		// TODO: define status and reply messages here
	}

	tiles {
		valueTile("temperature", "device.temperature", width: 2, height: 2) {
			state("temperature", label:'${currentValue}°', unit:'C',
				backgroundColors:[
					[value: 10, color: "#153591"],
					[value: 15, color: "#1e9cbb"],
					[value: 20, color: "#90d2a7"],
					[value: 25, color: "#44b621"],
					[value: 30, color: "#f1d801"],
					[value: 35, color: "#d04e00"],
					[value: 40, color: "#bc2323"]
				]
			)
		}
		standardTile("refresh", "device.switch", inactiveLabel: false, decoration: "flat") {
			state "default", label:'', action:"refresh.refresh", icon:"st.secondary.refresh"
		} 

		main(["temperature"])
		details(["temperature","refresh"])
    }
}

import groovy.json.JsonBuilder

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
	// TODO: handle 'temperature' attribute
}

def parseFromParent(data) {
	log.debug "Parsing from parent '${data}'"
    
    int celtemp = (data.temp).toInteger()
    celtemp = ((celtemp - 32)*5)/9
  
  log.debug "Cel Temp '${celtemp}'"
  data.temp = celtemp.toString()
  log.debug "data.temp '${data.temp}'"
	sendEvent(name: "temperature", value: data.temp, displayed: false)
}

// handle commands
def refresh() {
	log.debug "Executing 'refresh'"
	def unitid = device.deviceNetworkId.split(":")[2]
	return parent.buildAction("GET", "/GetZone?id=${unitid}", null)
}
