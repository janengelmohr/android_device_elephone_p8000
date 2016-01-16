Device repository for Meizu M2 Mini (CyanogenMod)
===========================

Getting Started
---------------

Initialize a repository with CyanogenMode:

    repo init -u git://github.com/CyanogenMod/android.git -b cm-12.1

Create local manifest for meizu meilan2 repositories:

    mkdir -p .repo/local_manifests/meizu_m2.xml

Populate the meizu_m2.xml with the following content:

    <xml

Build the code:

    source build/envsetup.sh
    breakfast meilan2
    mka 2>&1 | tee build.log

Current state
-------------
Note: the below is not yet confirmed, copied from original p8000 repo!!!

 * it boots
 * it is hardware accelerated
 * WiFi works

TODO:
 * RIL
 * BT
 * Audio
 * Camera
 * GPS
 * ALS
 * PS
 * MTP
 * Enforcing SELinux

IF you are trying to build TWRP against cm-12.1 you will need this commit:
* http://review.cyanogenmod.org/#/c/89474/1/recovery.te
* DO NOT INCLUDE THIS COMMIT FOR NORMAL BUILDS!
