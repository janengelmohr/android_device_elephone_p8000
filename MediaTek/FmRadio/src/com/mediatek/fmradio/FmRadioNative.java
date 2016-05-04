/* Copyright Statement:
 *
 * This software/firmware and related documentation ("MediaTek Software") are
 * protected under relevant copyright laws. The information contained herein is
 * confidential and proprietary to MediaTek Inc. and/or its licensors. Without
 * the prior written permission of MediaTek inc. and/or its licensors, any
 * reproduction, modification, use or disclosure of MediaTek Software, and
 * information contained herein, in whole or in part, shall be strictly
 * prohibited.
 *
 * MediaTek Inc. (C) 2011-2014. All rights reserved.
 *
 * BY OPENING THIS FILE, RECEIVER HEREBY UNEQUIVOCALLY ACKNOWLEDGES AND AGREES
 * THAT THE SOFTWARE/FIRMWARE AND ITS DOCUMENTATIONS ("MEDIATEK SOFTWARE")
 * RECEIVED FROM MEDIATEK AND/OR ITS REPRESENTATIVES ARE PROVIDED TO RECEIVER
 * ON AN "AS-IS" BASIS ONLY. MEDIATEK EXPRESSLY DISCLAIMS ANY AND ALL
 * WARRANTIES, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NONINFRINGEMENT. NEITHER DOES MEDIATEK PROVIDE ANY WARRANTY WHATSOEVER WITH
 * RESPECT TO THE SOFTWARE OF ANY THIRD PARTY WHICH MAY BE USED BY,
 * INCORPORATED IN, OR SUPPLIED WITH THE MEDIATEK SOFTWARE, AND RECEIVER AGREES
 * TO LOOK ONLY TO SUCH THIRD PARTY FOR ANY WARRANTY CLAIM RELATING THERETO.
 * RECEIVER EXPRESSLY ACKNOWLEDGES THAT IT IS RECEIVER'S SOLE RESPONSIBILITY TO
 * OBTAIN FROM ANY THIRD PARTY ALL PROPER LICENSES CONTAINED IN MEDIATEK
 * SOFTWARE. MEDIATEK SHALL ALSO NOT BE RESPONSIBLE FOR ANY MEDIATEK SOFTWARE
 * RELEASES MADE TO RECEIVER'S SPECIFICATION OR TO CONFORM TO A PARTICULAR
 * STANDARD OR OPEN FORUM. RECEIVER'S SOLE AND EXCLUSIVE REMEDY AND MEDIATEK'S
 * ENTIRE AND CUMULATIVE LIABILITY WITH RESPECT TO THE MEDIATEK SOFTWARE
 * RELEASED HEREUNDER WILL BE, AT MEDIATEK'S OPTION, TO REVISE OR REPLACE THE
 * MEDIATEK SOFTWARE AT ISSUE, OR REFUND ANY SOFTWARE LICENSE FEES OR SERVICE
 * CHARGE PAID BY RECEIVER TO MEDIATEK FOR SUCH MEDIATEK SOFTWARE AT ISSUE.
 *
 * The following software/firmware and/or related documentation ("MediaTek
 * Software") have been modified by MediaTek Inc. All revisions are subject to
 * any receiver's applicable license agreements with MediaTek Inc.
 */

package com.mediatek.fmradio;

/**
 * This class define FM native interface, will description FM native interface
 */
public class FmRadioNative {
    static {
        System.loadLibrary("fmjni");
    }

    /**
     * Open FM device, call before power up
     *
     * @return (true,success; false, failed)
     */
    static native boolean openDev();

    /**
     * Close FM device, call after power down
     *
     * @return (true, success; false, failed)
     */
    static native boolean closeDev();

    /**
     * power up FM with frequency use long antenna
     *
     * @param frequency frequency(50KHZ, 87.55; 100KHZ, 87.5)
     *
     * @return (true, success; false, failed)
     */
    static native boolean powerUp(float frequency);

    /**
     * Power down FM
     *
     * @param type (0, FMRadio; 1, FMTransimitter)
     *
     * @return (true, success; false, failed)
     */
    static native boolean powerDown(int type);

    /**
     * tune to frequency
     *
     * @param frequency frequency(50KHZ, 87.55; 100KHZ, 87.5)
     *
     * @return (true, success; false, failed)
     */
    static native boolean tune(float frequency);

    /**
     * seek with frequency in direction
     *
     * @param frequency frequency(50KHZ, 87.55; 100KHZ, 87.5)
     * @param isUp (true, next channel; false previous channel)
     *
     * @return frequency(float)
     */
    static native float seek(float frequency, boolean isUp);

    /**
     * Auto scan(from 87.50-108.00)
     *
     * @return The scan channel array(short)
     */
    static native short[] autoScan();

    /**
     * Stop scan, also can stop seek, other native when scan should call stop
     * scan first, else will execute wait auto scan finish
     *
     * @return (true, can stop scan process; false, can't stop scan process)
     */
    static native boolean stopScan();

    /**
     * Open or close rds fuction
     *
     * @param rdson The rdson (true, open; false, close)
     *
     * @return rdsset
     */
    static native int setRds(boolean rdson);

    /**
     * Read rds events
     *
     * @return rds event type
     */
    static native short readRds();

    /**
     * Get program service(program name)
     *
     * @return The program name
     */
    static native byte[] getPs();

    /**
     * Get radio text, RDS standard not support Chinese character
     *
     * @return The radio text(byte)
     */
    static native byte[] getLrText();

    /**
     *Active alternative frequencies
     *
     * @return The frequency(float)
     */
    static native short activeAf();

    /**
     * Mute or unmute FM voice
     *
     * @param mute (true, mute; false, unmute)
     *
     * @return (true, success; false, failed)
     */
    static native int setMute(boolean mute);

    /**
     * Inquiry if RDS is support in driver
     *
     * @return (1, support; 0, NOT support; -1, error)
     */
    static native int isRdsSupport();

    /**
     * Switch antenna
     *
     * @param antenna antenna (0, long antenna, 1 short antenna)
     *
     * @return (0, success; 1 failed; 2 not support)
     */
    static native int switchAntenna(int antenna);

    /**
     * Set Fm status to tell native RX or TX is on
     * @param which 0 is RX, 1 is TX
     * @param on true is on
     * @return
     */
    static native boolean setFmStatus(int which, boolean on);

    /**
     * Get the native Fm status
     * @param which 0 is RX ON, 1 is TX ON, 2 is TX Scaning
     * @return true if dothing
     */
    static native boolean getFmStatus(int which);

    // FM EM start
    /**
     * get rssi from hardware(use for engineer mode)
     * 
     * @return rssi value
     */
    static native int readRssi();

    /**
     * Inquiry if fm stereo mono(true, stereo; false mono)
     * 
     * @return (true, stereo; false, mono)
     */
    static native boolean stereoMono();

    /**
     * Force set to stero/mono mode
     * 
     * @param isMono
     *            (true, mono; false, stereo)
     * @return (true, success; false, failed)
     */
    static native boolean setStereoMono(boolean isMono);

    /**
     * Read cap array of short antenna
     * 
     * @return cap array value
     */
    static native short readCapArray();

    /**
     * read rds bler
     * 
     * @return rds bler value
     */
    static native short readRdsBler();

    /**
     * send variables to native, and get some variables return.
     * @param val send to native
     * @return get value from native
     */
    static native short[] emcmd(short[] val);

    /**
     * set RSSI, desense RSSI, mute gain soft
     * @param index flag which will execute
     * (0:rssi threshold,1:desense rssi threshold,2: SGM threshold)
     * @param value send to native
     * @return execute ok or not
     */
    static native boolean emsetth(int index, int value);

    /**
     * get hardware version
     * 
     * @return hardware version information array(0, ChipId; 1, EcoVersion; 2, PatchVersion; 3,
     *         DSPVersion)
     */
    static native int[] getHardwareVersion();
    // FM EM end
}
