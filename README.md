Device repository for Meizu M2 Mini (CyanogenMod)
===========================

Getting Started
---------------

Initialize a repository with CyanogenMode:

    repo init -u git://github.com/divis1969/android.git -b cm-14.1-meilan2

Optinally use a specific manifest (not a tip):

    repo init -u git://github.com/divis1969/android.git -b cm-14.1-meilan2 -m cm-14.1-meilan2-v0.1.xml

Build the code:

    source build/envsetup.sh
    breakfast meilan2
    make -j 4 bacon showcommands 2>&1 | tee build.log

Flash the phone:
https://github.com/divis1969/android_device_meizu_meilan2/wiki/%D0%9F%D1%80%D0%BE%D1%88%D0%B8%D0%B2%D0%BA%D0%B0-%D1%82%D0%B5%D0%BB%D0%B5%D1%84%D0%BE%D0%BD%D0%B0

Current state
-------------

- Cyanogen boots
- Touch, screen, keyboard, central key are working
- Wifi is working
- Audio is working
- Telephony is working (see Known Issues)
    - USIM (3G) supported
    - Incoming/outgoung call
    - SMS, USSD
    - Data connectivity
- GPS
- Bluetooth (pairing only testes so far)
- Sensors
- Camera

Known Issues
-------------
- Android Camera App is not stable (hangs) ex. with location enabled
- Meizu Camera App is crasing when switching to front camera
- Telephony crashes eventually on location request from camera. 
- Hardware OMX codecs are not working

All issues: https://github.com/divis1969/android_device_meizu_meilan2/issues

Change log
----------

### v0.1
- Initial port from cm-14.0 (v0.3) to cm-14.1

