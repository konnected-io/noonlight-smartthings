# Noonlight for SmartThings
24/7 professional central station monitoring for your SmartThings smart home powered by [Noonlight](https://noonlight.com/)

## Smart, is now safe.
Konnected has partnered with [Noonlight](https://noonlight.com/) to offer access to a 24/7 professionally monitored emergency central station via your SmartThings smart home. When used with the SmartThings Smart Home Monitor app, Noonlight can respond in case of an intrusion, smoke or carbon monoxide alarm. You can also configure any automation or rule in SmartThings to trigger an alarm with Noonlight just like turning on a switch. For example set up a panic routine that turns on Noonlight and help will be on the way in minutes.

**This integration is provided free and open-source by Konnected and is not endorsed or approved by SmartThings in any way. SmartThings support cannot answer questions about un-official integrations.**

Noonlight is currently only available in the United States.

## How Noonlight Works in SmartThings

![](https://s3.amazonaws.com/cdn.freshdesk.com/data/helpdesk/attachments/production/32004244709/original/VLJAFAMdPZkN_Uo2NZUeqOiQ5JsE2rlSiA.png?1548185227)

Noonlight shows up as a Switch and/or Siren in SmartThings which you can control manually or automate. If you're using SmartThings Smart Home Monitor to alert for security or smoke/CO, you can set it up to turn on Noonlight like a siren/alarm. This initiates an API call over your internet connection to Noonlight's 24/7 facility in the United States.

Within seconds you will receive a **text message** from Noonlight confirming the alarm. You can respond via text reply.

You will soon receive a **phone call** from a Noonlight dispatcher. If it's a false alarm, tell them your 4-digit PIN and that's it.

If Noonlight cannot get in contact with you, they will dispatch your local emergency services to your home. This uses your hub's location setting to determine your location.

### Works Best With SmartThings Classic App
The new SmartThings app is not fully compatible with this integration. Please use the SmartThings Classic app.

### Requires an Internet Connection
**The SmartThings hub must have an active internet connection for this to work!**

### NO GUARANTEE
**This integration is provided as-is without warranties of any kind. Using Noonlight with SmartThings involves multiple service providers and potential points of failure, including (but not limited to) your internet service provider, 3rd party hosting services such as Amazon Web Services, and the SmartThings software platform.** Please read and understand the [Noonlight terms of use](https://noonlight.com/terms), [Konnected terms of use](https://konnected.io/terms) and [SmartThings terms of service](https://www.smartthings.com/terms), each of which include important limitations of liability and indemnification provisions.

## Why Noonlight is Better
* Noonlight allows the DIY smart home user to have the same peace of mind from 24/7 protection by a professionally managed central station at a fraction of the cost of traditional monitoring services by using their smart home eqipment and internet connection.
* When an alarm is triggered, Noonlight dispatchers see detailed information from the smart home devices that you authorize, including door/window sensors, motion sensors, smoke/CO detectors, presence sensors and temperature sensors. This allows the Noonlight dispatcher to potentially determine the nature of the emergency if you are unable to respond.
* Noonlight is backed by a UL certified central station, and can provide a certificate for insurance discounts
* Also includes the Noonlight App that you can use on the go to get help any time anywhere in the United States
* No long-term contracts or commitments. Cancel any time.
* **Only $10/month** (first month free)

## How this Integration Works
This integration connects SmartThings to Noonlight via two parts:
1. the open-source Groovy app and driver in this repo to be loaded into your SmartThings instance
1. an _authentication broker_ cloud service maintained by Konnected

When you initially set up the Noonlight app in SmartThings, you will be directed to _Connect Noonlight_ via a simple web based authentication flow. This directs you to an _authentication broker_ service maintained by Konnected that facilitates the secure exchange of credentials between SmartThings and Noonlight.

Periodically, the Konnected authentication broker service will call back to your SmartThings cloud instance with a refreshed Noonlight API token. This ensures that you always have a valid Noonlight API token in case of an emergency.

In the event of an alarm, the Noonlight app running on SmartThings will use the Noonlight API token to trigger an alarm directly through Noonlight's API.

## [Install Noonlight for SmartThings -->](https://help.konnected.io/support/solutions/articles/32000025600-noonlight-for-smartthings)
Click for installation and usage instructions.
