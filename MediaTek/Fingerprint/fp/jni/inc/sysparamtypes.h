/*  ====================================================================================================
**
**
**  ---------------------------------------------------------------------------------------------------
**
**	File Name:  	sysparamtypes.h
**	
**	Description:	This file contains the sysparam types definitions.
**
**
**  ---------------------------------------------------------------------------------------------------
**
**  Author:			Warren Zhao
**
** -------------------------------------------------------------------------
**
**	Change History:
**	
**	Initial revision
**
**====================================================================================================*/

#ifndef _SYSPARAM_TYPES_H_
#define _SYSPARAM_TYPES_H_
// fp chipe id
#define  Sensor_Type    	    "device/SysParam/SLFpChip/SensorType"

// fp type
#define  SLFpApi_Sysparm_on   	"device/SysParam/SLFpApi/sysparm_on"
#define  SLFpApi_FpShreshold   	"device/SysParam/SLFpApi/fpShreshold"
#define  SLFpApi_Frame_h		"device/SysParam/SLFpApi/frame_h"
#define  SLFpApi_Frame_w    	"device/SysParam/SLFpApi/frame_w"
#define	 SLFpApi_Multi_User		"device/SysParam/SLFpApi/multi_user_enable"
#define	 SLFpApi_MultiProcs		"device/SysParam/SLFpApi/MultiProcs"

// hw test config
#define  Sysparm_on       		"device/SysParam/HardWare/sysparm_on"
#define  Wakeup           		"device/SysParam/HardWare/wakeup"
#define  GrayRevertOnOff		"device/SysParam/HardWare/grayrevert"
//#define  Stress           		"device/SysParam/HardWare/stress"
#define  Test_Sysparm_on      "device/SysParam/Test/sysparm_on"
#define  Stress           		"device/SysParam/Test/stress"
// spi config
#define  SpiPages        		"device/SysParam/HardWare/spi/spiPages"
#define  SpiOffset        		"device/SysParam/HardWare/spi/spiOffset"
#define  SpiSpeed         		"device/SysParam/HardWare/spi/spiSpeed"
#define  SpiResetGpio     		"device/SysParam/HardWare/spi/spiResetGpio"
#define  SpiReset         		"device/SysParam/HardWare/spi/spiReset"
#define  SpiMode          		"device/SysParam/HardWare/spi/spiMode"
#define  Iotrllength	  		"device/SysParam/HardWare/spi/spiiotrllength"

#define  FrameAverageOnOff	  	"device/SysParam/HardWare/frame_average"


typedef struct{
    int 			spiPages;
    int 			spiOffset;
    unsigned int 	spiSpeed;
    int 			spiReset_gpio;
    int 			spiReset;
    int 			spiMode;    /*useless*/
    int 			iotrllength;
} spi ;

typedef struct{
	int api_sysparm_on;
    int api_fp_shreshold;    
    int api_frame_h;
    int api_frame_w;
	int api_multi_user_enable;
	int api_multi_procs_enable;	
	int hw_sysparm_on;
	int test_sysparm_on;
    int test_stress;
	int grayrevert_on;
	int frameaverage_on;
    spi spi_config;	
	int uevt_sysparm_on;
	int uevt_timeout;
	int int_mode_on;
	unsigned int RegAddrClearTest_R;
 	unsigned int RegAddrClearTest_W;
 	unsigned int RegAddrCheckIntMode;
}silead_config, * psilead_config;


#endif

