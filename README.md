Device repository for Meizu M2 Mini (CyanogenMod)
===========================

Getting Started
---------------

Initialize a repository with CyanogenMode:

    repo init -u git://github.com/divis1969/android.git -b meilan2

Optinally use a specific manifest (not a tip):

    repo init -u git://github.com/divis1969/android.git -b meilan2 -m cm-12.1-v0.3.xml

Note: 2 more Cyanogen repositories were forked for v0.3, so if you will encounter an error while syncing on top
of exiting tree, use the suggesstion from the error log (sync those repos with --force-sync) 

Build the code:

    source build/envsetup.sh
    breakfast meilan2
    mka bacon 2>&1 | tee build.log

Flash the phone:
https://github.com/divis1969/android_device_meizu_meilan2/wiki/%D0%9F%D1%80%D0%BE%D1%88%D0%B8%D0%B2%D0%BA%D0%B0-%D1%82%D0%B5%D0%BB%D0%B5%D1%84%D0%BE%D0%BD%D0%B0

Current state
-------------

- Recovery is fully functional
- Update package (OTA) generation
- Cyanogen boots
- Touch, screen, keyboard, central key are working
- Wifi is working
- Camera is working
- Audio is working
- Telephony is working
    - USIM (3G) supported
    - Incoming/outgoung call
    - Data connection (3G/2G)
- GPS is working
- BT is working (discovery and connect were tested only)

Known Issues
-------------
- SIM sometimes is not correctly identified (workaround: turn Airplane mode on/off)
- Google Play (opengapps) does not allow to install some apps (ex. Chrome, Telegram)
- Google search cannot use microphone

Change log
----------

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

