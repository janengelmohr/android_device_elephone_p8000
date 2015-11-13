# android_device_alps_k05ts_a
Device repository for Elephone P8000 CM12.1 (WIP)

current state:
 * CR works except external sd mounting and signature checking

TODO:
 * fix binder access errors, looks like an SELinux problem

IF you are trying to build TWRP against cm-12.1 you will need this commit:
* http://review.cyanogenmod.org/#/c/89474/1/recovery.te
DO NOT INCLUDE THIS COMMIT FOR NORMAL BUILDS!
