Device repository for Meizu M2 Mini (CyanogenMod)
===========================

Getting Started
---------------

Initialize a repository with CyanogenMode:

    repo init -u git://github.com/divis1969/android.git -b meilan2

Optinally use a specific manifest (not a tip):

    repo init -u git://github.com/divis1969/android.git -b meilan2 -m meilan2-cm-12.1-v0.6.xml

Note: 4 more Cyanogen repositories were forked since v0.2, so if you will encounter an error while syncing on top
of exiting tree, use the suggestion from the error log (sync those repos with --force-sync) 

Build the code:

    source build/envsetup.sh
    breakfast meilan2
    mka bacon 2>&1 | tee build.log

Flash the phone:
https://github.com/divis1969/android_device_meizu_meilan2/wiki/%D0%9F%D1%80%D0%BE%D1%88%D0%B8%D0%B2%D0%BA%D0%B0-%D1%82%D0%B5%D0%BB%D0%B5%D1%84%D0%BE%D0%BD%D0%B0

Current state
-------------

- Recovery is fully functional
- Update package generation
- Cyanogen boots
- Touch, screen, keyboard, central key are working
- Wifi is working
- Camera is working
    - Preview
    - Still image capturing
    - Video Recording
- Audio is working
- Telephony is working
    - USIM (3G) supported
    - Incoming/outgoung call
    - Data connection (3G/2G)
- GPS is working
- BT is working (discovery and connect were tested only)

Known Issues
-------------
All issues: https://github.com/divis1969/android_device_meizu_meilan2/issues

Change log
----------

### v0.6
- Enabled Tethering support
- Enabled A2DP support (fixed BT audio)
- Enabled power profile (battery statistics)
- Fixed MTK thermald and thermal utility launch
- Fixed microphone issue in some apps

### v0.5
- Fixed YouTube playback crash
- Fixed SIM card handling (enabled fakeiccid feature)
- Fixed sensors (enabled orientation and magnetic sensors)

### v0.4
- Fixed Google Play issue "... is not supported by your device"
- Fixed microphone issues (ex. Google Search)
- HW media codec support (Fixes issues with video recording and media playback)
- Auto-brightness support (modeled similar to stock 4.5.4)
- Fixed USSD

### v0.3
- GPS bring up
- BT bring up
- Fixed a battery status issue
- Removed China default timezone

### v0.2
- Telephony bring up complete

### v0.1
- Fixed an issue with USB/ADB
- Fixed brightness control and wakeup (screen on) after sleep (with USB detached)
- Fixed headphone is not recognized issue
- Fixed camera freeze when no USB is connected

