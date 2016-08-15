
#ifndef __FPSVC_TYPES_H__
#define __FPSVC_TYPES_H__

#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>
/*
 * Define the base type
 */
typedef unsigned char			SL_UINT8;
typedef	unsigned short			SL_UINT16;
typedef unsigned int			SL_UINT32;
typedef long long unsigned int	SL_UINT64;
typedef signed char				SL_INT8;
typedef signed short			SL_INT16;
typedef signed int				SL_INT32;
typedef int						SL_BOOL;
typedef char					SL_CHAR;
typedef void					SL_VOID;

#define SL_TRUE					1
#define SL_FALSE				0
#define SL_NULL					0
#define ARRAY_SIZE(a)			(sizeof(a) / sizeof( (a)[0] ) )

#define  SL_TEMPLATE_FILE		"/data/data/com.android.FpTest"

/*
 * Define the error code
 */
#define         SL_SUCCESS                      0
#define 		SL_FAIL							-1

#define         SL_WRONG_PARAM                  -1001
#define         SL_NOT_MATCH                    -1003
#define         SL_TOUCH_ERROR                  -1004

#define			SL_CHECK_IMG_FAIL				-1

#define 		MAX_ENROLL_NUM					10
#define 		MIN_ENROLL_NUM 					7

#define         SL_ENROLL_CANCELED              -2
#define         SL_NOT_SUPPORT                  -3
#define         SL_ENROLL_ERROR					-4
#define         SL_ENROLL_FAIL					-5


#define 		SL_IDENTIFY_TMEOUT				-1
#define         SL_IDENTIFY_CANCELED            -2
#define         SL_IDENTIFY_ERR_MATCH           -3
#define         SL_IDENTIFY_ERROR               -4
#define         SL_IDENTIFY_FAIL               	-5

#define         SL_FINGER_NOT_EXIST             -1008
#define         SL_DEVICE_NOT_READY             -1009
#define         SL_MEMORY_ERROR                 -1010
#define         SL_ALGORITHM_INIT_ERROR         -1011
#define			SL_LOAD_API_ERROR				-1013

#define         SL_UNKNOWN_ERROR                -1100

/*----------------------------------------------------------
 * Define the command for enrollment or identification
 */
#define SL_ABS(x) (((x) >= 0) ? (x) : -(x))
#define SL_SQR(x) ((x) * (x))

typedef enum {
	SL_OP_NONE,
	SL_OP_CANCEL,
	SL_OP_ENROLL,
	SL_OP_IDENTIFY,
	SL_OP_SAVE_FINGER,
	SL_OP_REMOVE_FINGER,
	SL_OP_ENABLE_FINGER,
	SL_OP_DISABLE_FINGER,
	SL_OP_UNKNOWN_OPERATION
} SL_FP_OPERATION, *PSL_FP_OPERATION;

/*---------------------------------------------------------*
 * Fingerprint Algorithm Context
 *---------------------------------------------------------*/
typedef void  SL_CONTEXT;

/*---------------------------------------------------------*
 * Fingerprint Sensor Parameters
 *---------------------------------------------------------*/

typedef enum {
	SL_SENSOR_WIDTH,
	SL_SENSOR_HEIGHT,
	SL_SENSOR_DPI,
	SL_UNKNOWN_PARAM
} SL_DEVICE_PARAM, *PSL_DEVICE_PARAM;

/*---------------------------------------------------------*
 * Slot index for fingerprint template enroll/identify
 *---------------------------------------------------------*/
typedef enum {
	SL_SLOT_0,
	SL_SLOT_1,
	SL_SLOT_2,
	SL_SLOT_3,
	SL_SLOT_4,
	SL_SLOT_ALL = 5,
	SL_UNKNOW_SLOT
} FP_SLOT_INDEX;

#define  EXPORT
#define  IMPORT

//#define SL_MAX_ENROLL_COUNT				8
//#define SL_MIN_ENROLL_COUNT				3
/***********sysparm default*****************/
#define SL_CHIP_TYPE						6162
#define	SL_IDENTIFY_THRESHOLD_DEFAULT		40
#define SL_MAX_FINGER_FRAME_WIDTH_DEFAULT	110
#define SL_MAX_FINGER_FRAME_HIGH_DEFAULT	118
typedef  SL_UINT8   SL_FRAME_ROW[SL_MAX_FINGER_FRAME_WIDTH_DEFAULT];

typedef struct {
	SL_FRAME_ROW		rows[SL_MAX_FINGER_FRAME_HIGH_DEFAULT];
} SL_FINGER_FRAME, *PSL_FINGER_FRAME;


typedef SL_BOOL         (*FingerDetectFunc)(void* frame);


#define FINGERNAMELEN 32
typedef struct {
	int index;
	int slot;
	int enable;
	char fingername[FINGERNAMELEN*2+1];
} SLFpsvcFPInfo_t;
typedef struct {
	int total;
 	int max;
	int wenable;
	int frame_w;
	int frame_h;
	SLFpsvcFPInfo_t fpinfo[SL_SLOT_ALL];
} SLFpsvcIndex_t;

#endif
