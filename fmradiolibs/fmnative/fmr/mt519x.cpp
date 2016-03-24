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
 * MediaTek Inc. (C) 2010. All rights reserved.
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

#include "fmr.h"
#include <ATVCtrl.h>

#ifdef LOG_TAG
#undef LOG_TAG
#endif
#define LOG_TAG "FMLIB_519x"

extern "C" {
	using namespace android;

	int ATVC_fm_powerup(void* parm){return ATVCtrl::ATVC_fm_powerup(parm);}
	int ATVC_fm_powerdown(void){return ATVCtrl::ATVC_fm_powerdown();}
	int ATVC_fm_getrssi(int* val){return ATVCtrl::ATVC_fm_getrssi(val);}
	int ATVC_fm_tune(void* parm){return ATVCtrl::ATVC_fm_tune(parm);}
	int ATVC_fm_seek(void* parm){return ATVCtrl::ATVC_fm_seek(parm);}
	int ATVC_fm_scan(void* parm){return ATVCtrl::ATVC_fm_scan(parm);}
	int ATVC_fm_mute(int val){return ATVCtrl::ATVC_fm_mute(val);}
	int ATVC_fm_getchipid(unsigned short int* val){return ATVCtrl::ATVC_fm_getchipid(val);}
	int ATVC_fm_isRDSsupport(int* val){return ATVCtrl::ATVC_fm_isRDSsupport(val);}
	int ATVC_fm_isFMPowerUp(int* val){return ATVCtrl::ATVC_fm_isFMPowerUp(val);}
}

#ifdef __cplusplus
extern "C" {
#endif

#define MT519X_VFD_MAX 10
#define MT519X_VFD_BASE 20

static int g_stopscan = 0;
static int vfd[MT519X_VFD_MAX] = {0};

int MT519X_open_dev(const char *pname, int *fd);
int MT519X_close_dev(int fd);
int MT519X_pwr_up(int fd, int band, int freq);
int MT519X_pwr_down(int fd, int type);
int MT519X_seek(int fd, int *freq, int band, int dir, int lev);
int MT519X_sw_scan(int fd, uint16_t *scan_tbl, int *max_num, int band, int sort);
int MT519X_scan(int fd, uint16_t *tbl, int *num);
int MT519X_stop_scan(int fd);
int MT519X_tune(int fd, int freq, int band);
int MT519X_set_mute(int fd, int mute);
int MT519X_is_fm_pwrup(int fd, int *pwrup);
int MT519X_is_rdsrx_support(int fd, int *supt);
int MT519X_is_rdstx_support(int fd, int *supt);
int MT519X_turn_on_off_rds(int fd, int onoff);
int MT519X_get_chip_id(int fd, int *chipid);


void MT519X_init(struct fm_cbk_tbl *cbk_tbl)
{
	//Basic functions.
	cbk_tbl->open_dev = MT519X_open_dev;
	cbk_tbl->close_dev = MT519X_close_dev;
	cbk_tbl->pwr_up = MT519X_pwr_up;
	cbk_tbl->pwr_down = MT519X_pwr_down;
	cbk_tbl->seek = MT519X_seek;
	cbk_tbl->scan = MT519X_sw_scan;	//hw_scan or sw_scan.
	cbk_tbl->stop_scan = MT519X_stop_scan;
	cbk_tbl->tune = MT519X_tune;
	cbk_tbl->set_mute = MT519X_set_mute;
	cbk_tbl->is_fm_pwrup = MT519X_is_fm_pwrup;
	cbk_tbl->is_rdsrx_support = MT519X_is_rdsrx_support;
	cbk_tbl->is_rdstx_support = MT519X_is_rdstx_support;
	cbk_tbl->turn_on_off_rds = MT519X_turn_on_off_rds;
	cbk_tbl->get_chip_id = MT519X_get_chip_id;

	return;
}

int MT519X_open_dev(const char *pname, int *fd)
{
	int idx;
	int ret = 0;

	for (idx=0;idx<MT519X_VFD_MAX;idx++) {
		if (vfd[idx] == 0) {
			vfd[idx] = 1;
			*fd = idx+MT519X_VFD_BASE;
			return ret;
		}
	}

	return -ERR_INVALID_FD;
}

int MT519X_close_dev(int fd)
{
	if (vfd[fd-MT519X_VFD_BASE] == 1) {
		vfd[fd-MT519X_VFD_BASE] = 0;
		return 0;
	} else {
		return -1;
	}
}

int MT519X_pwr_up(int fd, int band, int freq)
{
	int ret = 0;
	struct fm_tune_parm parm;

	bzero(&parm, sizeof(struct fm_tune_parm));

	parm.band = band;
	parm.freq = freq;
	parm.hilo = FM_AUTO_HILO_OFF;
	parm.space = FM_SEEK_SPACE;

	if(ATVC_fm_powerup(&parm))
	{
		ret = 0;
	}
	else
	{
		ret = ERR_FW_NORES;
	}
	return ret;
}

int MT519X_pwr_down(int fd, int type)
{
	int ret = 0;

	if(ATVC_fm_powerdown())
	{
		ret = 0;
	}
	else
	{
		ret = ERR_FW_NORES;
	}
	return ret;
}

int MT519X_get_chip_id(int fd, int *chipid)
{
	int ret = 0;

	ret = ATVC_fm_getchipid((short unsigned int *)chipid)?0:1;
	return ret;
}

int MT519X_get_rssi(int fd, int *rssi)
{
	int ret = 0;

	ret = ATVC_fm_getrssi(rssi)?0:1;
	return ret;
}

int MT519X_tune(int fd, int freq, int band)
{
	int ret = 0;
	struct fm_tune_parm parm;

	bzero(&parm, sizeof(struct fm_tune_parm));

	parm.band = band;
	parm.freq = freq;
	parm.hilo = FM_AUTO_HILO_OFF;
	parm.space = FM_SEEK_SPACE;

	ret = ATVC_fm_tune(&parm);
	return ret?0:1;
}

int MT519X_seek(int fd, int *freq, int band, int dir, int lev)
{
	int ret = 0;
	struct fm_seek_parm parm;

	bzero(&parm, sizeof(struct fm_tune_parm));

	parm.band = band;
	parm.freq = *freq;
	parm.hilo = FM_AUTO_HILO_OFF;
	parm.space = FM_SEEK_SPACE;
	if (dir == 1) {
		parm.seekdir = FM_SEEK_UP;
	} else if (dir == 0) {
		parm.seekdir = FM_SEEK_DOWN;
	}
	parm.seekth = lev;

	ret = ATVC_fm_seek(&parm)?0:1;
	if (ret == 0) {
		*freq = parm.freq;
	}

	return ret;
}

int MT519X_set_mute(int fd, int mute)
{
	int ret = 0;

	ret = ATVC_fm_mute(mute)?0:1;
	
	return ret;
}

int MT519X_is_fm_pwrup(int fd, int *pwrup)
{
	int ret = 0;

	ret = ATVC_fm_isFMPowerUp(pwrup);
	return ret;
}

int MT519X_is_rdsrx_support(int fd, int *supt)
{
	int ret = 0;

	ret = ATVC_fm_isRDSsupport(supt)?0:1;
	return ret;
}

int MT519X_is_rdstx_support(int fd, int *supt)
{
	int ret = 0;
	
	*supt = 0;
	return ret;
}

int MT519X_sw_scan(int fd, uint16_t *scan_tbl, int *max_num, int band, int sort)
{
    int ret = 0;
    int chl_cnt = 0;
    struct fm_seek_parm parm;
    int start_freq = FM_FREQ_MIN;
    
    g_stopscan = false;

    do 
    {
        bzero(&parm, sizeof(struct fm_tune_parm));
        parm.band = band;
        parm.freq = start_freq;
        parm.hilo = FM_AUTO_HILO_OFF;
        parm.space = FM_SEEK_SPACE;
        parm.seekdir = FM_SEEK_UP;
        parm.seekth = FM_SEEKTH_LEVEL_DEFAULT;

        ret = ATVC_fm_seek(&parm)?0:1;
        
        if (ret)
        {
            LOGE("FM scan faild:%d:%d\n", ret, parm.err);
            return NULL;
        }
            
        if((parm.err == FM_SUCCESS) 
            && (chl_cnt < *max_num) 
            && (parm.freq > start_freq))
        {
            scan_tbl[chl_cnt] = parm.freq;
            chl_cnt++;            
            LOGI("FM scan:%d:%d\n", chl_cnt, parm.freq);
        }
        else
        {
            break;
        }                
        start_freq = parm.freq;
    }    while (!g_stopscan);

    if(g_stopscan)
    {
        LOGI("FM force stop scan\n");
        g_stopscan = false;   
    }

    *max_num = chl_cnt;
    
    if(g_stopscan)
    {
        LOGI("FM force stop scan\n");
        g_stopscan = false;   
    }

    LOGI("FM sw scan %d station found\n", chl_cnt);
    return ret;
}


int MT519X_stop_scan(int fd)
{
	int ret = 0;
	
	g_stopscan = 1;
	return ret;
}

int MT519X_turn_on_off_rds(int fd, int onoff)
{
    return 0;
}

#ifdef __cplusplus
}
#endif


