# android_device_alps_k05ts_a
Device repository for Elephone P8000 CM12.1 (WIP)

currently working:
 * it boots
 * it is hardware accelerated
 * WiFi 
 * Camera works for taking pictures if FlashlightTile is nulled
 * Vibration
 * Rotation
 * BT
 * Audio
 * PS

TODO:
 * RIL
 * Videorecording
 * GPS
 * ALS
 * MTP
 * Enforcing SELinux

IF you are trying to build TWRP against cm-12.1 you will need this commit:
* http://review.cyanogenmod.org/#/c/89474/1/recovery.te
* DO NOT INCLUDE THIS COMMIT FOR NORMAL BUILDS!
