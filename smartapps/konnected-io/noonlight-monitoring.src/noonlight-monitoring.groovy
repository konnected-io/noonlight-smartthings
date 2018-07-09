/**
 *  Noonlight Monitoring
 *
 *  Copyright 2018 Nate Clark, Konnected Inc
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
definition(
    name: "Noonlight Monitoring",
    namespace: "konnected-io",
    author: "Nate Clark, Konnected Inc",
    description: "Security, fire and medical emergency monitoring for your smart devices. Brought to you by Noonlight & Konnected",
    category: "Safety & Security",
    iconUrl: "https://files.readme.io/9336861-Noonlight_Favicon_16x16-01.ico",
    iconX2Url: "https://files.readme.io/9336861-Noonlight_Favicon_16x16-01.ico",
    iconX3Url: "https://files.readme.io/9336861-Noonlight_Favicon_16x16-01.ico")

mappings {
  path("/noonlight/token") { action: [ POST: "updateNoonlightToken"] }
}

preferences {
  page(name: "pageConfiguration", install: true, uninstall: true, content: "pageConfiguration")
}

def pageConfiguration() {
  if(!state.accessToken) { createAccessToken() }
    if(!validNoonlightToken()) {
        dynamicPage(name: "pageConfiguration") {
            section("Activate your Noonlight account") {
                href(
                    name:        "oauth_init",
                    image:       "https://files.readme.io/9336861-Noonlight_Favicon_16x16-01.ico",
                    title:       "Tap to connect to Noonlight",
                    description: "Start your free trial or sign in",
                    url:         "https://konnected-noonlight.herokuapp.com/st/auth/?app_id=${app.id}&api_host=${apiServerUrl}&access_token=${state.accessToken}",
                    style: "embedded", required: true
                )
            }
        }
    } else {
      dynamicPage(name: "pageConfiguration") {
            section {
              paragraph("Awesome. You are connected to Noonlight!",
                  image:       "https://files.readme.io/9336861-Noonlight_Favicon_16x16-01.ico")
            }
            section("Select the services that you want to monitor with Noonlight. Each enabled service will create a virtual switch in SmartThings that you can use to trigger an alarm manually or automatically." ) {        
                input(
                name: "police",
                type: "bool",
                title: "Police",
                required: false,
                defaultValue: true
            )
                input(
                name: "fire",
                type: "bool",
                title: "Fire",
                required: false,
                defaultValue: true
            )
                input(
                name: "medical",
                type: "bool",
                title: "Medical",
                required: false,
                defaultValue: true
            )
      }
            section {
              paragraph "v0.0.1"
            }
        }
    }
}

def installed() {
  log.debug "Installed with settings: ${settings}"

  initialize()
}

def updated() {
  log.debug "Updated with settings: ${settings}"

  unsubscribe()
  initialize()
}

def initialize() {
  runEvery5Minutes(validNoonlightToken)
    validNoonlightToken()
    childDeviceConfiguration()
}

def updateNoonlightToken() {
  log.debug "Noonlight Token: $request.JSON"
  state.noonlightToken = request.JSON.token
    state.noonlightTokenExpires = request.JSON.expires
    return
}

def createAlarm(dni) {
  def service = dni.split('-')[-1]
    def alarm_attributes = [
        uri: 'https://api-sandbox.safetrek.io/v1/alarms',
        body: [
            services: [ 
              police: (service == 'police'), 
                fire: (service == 'fire'), 
                medical: (service == 'medical') 
            ],
            'location.coordinates': [ lat: location.getLatitude(), lng: location.getLongitude(), accuracy: 5 ]
        ],
        headers: ['Authorization': "Bearer ${state.noonlightToken}"]
    ]
    
    try {
        httpPostJson(alarm_attributes) { response -> 
          log.debug response.data
            processNoonlightResponse(response.data)
        }
    } catch(e) {
      log.error "something went wrong: $e"
    }
}

def cancelAlarm(dni) {
  def service = dni.split('-')[-1]
    def alarm_id = state[service]
  def alarm_attributes = [
        uri: "https://api-sandbox.safetrek.io/v1/alarms/$alarm_id/status",
        body: [ status: "CANCELED" ],
        headers: ['Authorization': "Bearer ${state.noonlightToken}"]
    ]
  try {
        httpPutJson(alarm_attributes) { response -> 
          log.debug response.data
            if (response.data.status == 200) {
              state.currentAlarms[service] = null
              getChildDevice("noonlight-$service").switchOff()
            }
        }
    } catch(e) {
      log.error "$e"
    }
}

def processNoonlightResponse(data) {
  state.currentAlarms = {} 
    if (data.status == 'ACTIVE') {
        data.services.each { service, enabled ->
          log.debug "service $service is $enabled"
          if (enabled) {
              state[service] = data.id
              getChildDevice("noonlight-$service")?.switchOn()                
            } else {
              state[service] = null
                getChildDevice("noonlight-$service")?.switchOff()
            }
        }
    }
}

def validNoonlightToken() {
  if (state.noonlightToken) {
        def expire_date = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", state.noonlightTokenExpires)
        def expires_in = (expire_date.time - new Date().time)
        if (expires_in > 0) {
        log.debug "Noonlight token is valid for $expires_in milliseconds"
            return true
        } else {
            log.debug "Noonlight token has expired!"
            return false
        }
    } else {
      log.debug "Noonlight token is not set!"
        return false
    }
}

def childDeviceConfiguration() {
  ['police','fire','medical'].each {
      def existing_device = getChildDevice("noonlight-$it")
    if (settings."$it") {
          if (!existing_device) {
          addChildDevice("konnected-io", 'Noonlight Alarm', "noonlight-$it", null, [label: "Noonlight ${it.capitalize()}", completedSetup: true])
            }
      } else {
          if (existing_device) {
              deleteChildDevice("noonlight-$it")
          }        
        }
    }
}
