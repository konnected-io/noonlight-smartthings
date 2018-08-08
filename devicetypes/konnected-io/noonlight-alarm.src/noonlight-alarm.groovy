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
  definition (name: "Noonlight Alarm", namespace: "konnected-io", author: "konnected.io", mnmn: "SmartThings", vid: "generic-siren") {
    capability "Alarm"
    capability "Switch"
  }

  preferences {  }

  tiles(scale: 2) {
  	valueTile("view", "device.alarm", decoration: "flat") {
      state ("off", icon: "https://s3.amazonaws.com/konnected-noonlight/noonlight-symbol-white2x.png", label: "Idle")
      state ("both", icon: "https://s3.amazonaws.com/konnected-noonlight/noonlight-symbol-white2x.png", label: "Alarm!", backgroundColor:"#166efb")
    }
    standardTile("switch", "device.alarm", decoration: "flat", width: 6, height: 4) {
      state "off",  label: "Idle", icon:"https://s3.amazonaws.com/konnected-noonlight/noonlight-symbol-white3x.png", action:"alarm.both", backgroundColor:"#ffffff", nextState: "turningOn"
      state "both", label: "Alarm in Progress", icon:"https://s3.amazonaws.com/konnected-noonlight/noonlight-symbol-white3x.png", backgroundColor:"#344351", nextState: "turningOff"
      state "turningOn", label:'Activating', icon:"https://s3.amazonaws.com/konnected-noonlight/noonlight-symbol-white3x.png", backgroundColor:"#166efb", nextState: "turningOff"
    }
    main "view"
    details "switch"
  }
}

def off() {
  // Canceling the alarm from SmartThings is currently disabled for your safety.
  // A Noonlight agent will contact the account owner who can cancel the alarm with his/her PIN.
  //
  // parent.cancelAlarm()
}

def on() {
  parent.createAlarm()
}

def switchOn() {
  log.debug "alarm is active"
  sendEvent(name: "alarm", value: "both")
}

def switchOff() {
  log.debug "alarm is cancelled"
  sendEvent(name: "alarm", value: "off")
}

def both() { on() }

def strobe() { on() }

def siren() { on() }
