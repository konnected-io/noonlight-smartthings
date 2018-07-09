/**
 *  Noonlight Alarm
 *
 *  Copyright 2018 konnected.io
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
  definition (name: "Noonlight Alarm", namespace: "konnected-io", author: "konnected.io") {
    capability "Alarm"
    capability "Switch"
    capability "Actuator"
  }

  preferences {  }

  tiles {
    multiAttributeTile(name:"main", type: "generic", width: 6, height: 4, canChangeIcon: true) {
      tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
        attributeState ("off",  label: "Off",    icon:"https://uploads-ssl.webflow.com/5b283a9ce1d84c649b724269/5b2bd47b7afad82c303a2868_fav1.png", action:"alarm.both", backgroundColor:"#ffffff", nextState: "turningOn")
        attributeState ("on", label: "Alarm!", icon:"st.security.alarm.alarm", action:"alarm.off",  backgroundColor:"#e86d13", nextState: "turningOff")
        attributeState ("turningOn", label:'Activating', icon:"st.security.alarm.alarm", action:"alarm.off", backgroundColor:"#e86d13", nextState: "turningOff")
        attributeState ("turningOff", label:'Turning off', icon:"st.security.alarm.clear", action:"alarm.on", backgroundColor:"#ffffff", nextState: "turningOn")
      }
    }
    main "main"
    details "main"
  }
}

def off() {
  parent.cancelAlarm(device.deviceNetworkId)
}

def on() {
  parent.createAlarm(device.deviceNetworkId)
}

def switchOn() {
  log.debug "alarm is active"
  sendEvent(name: "switch", value: "on")
}

def switchOff() {
  log.debug "alarm is cancelled"
  sendEvent(name: "switch", value: "off")
}

def both() { on() }

def strobe() { on() }

def siren() { on() }

