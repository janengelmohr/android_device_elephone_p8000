#ifndef __FOR_DEFINE_H__
#define __FOR_DEFINE_H__
#include <stdlib.h>
#include <string.h>
#include <math.h>


#define		CUTSIZE			0
#define		GABOR_FREQ		8.0f
#define		AREANUM			8
#define		GABOR_PSI		0.0f
#define		GABOR_GAMMA		1.0f
#define		GABOR_SIGMA		4.0f
#define		GABOR_PATH		"../GBFFun.dat"
#define		PI				3.1415926535
#define 	DC_MINUS		987654
#define 	DC_DIRECT		456789
#define 	SHAPE_STRIP		13579
#define		SHAPE_SQUARE	24680

#define 	FINGER_DETECT_NORMAL_THRESH		4
#define 	FINGER_DETECT_SLEEP_THRESH		2

typedef struct{
	int iWidth;
	int iHeight;
	int iUp;
	int iDown;
	int iLeft;
	int iRight;
}sSizeInfo;

typedef struct{
	int ifftMin;
	int icovMin;
	int bright;
	int dark;
	int mean;
	int var;
}sParam;

typedef struct {
	int iPTOn;//0,?¨¢???1?a1?
	int iPTLow[3];
	unsigned int iPTLowStep[3];
	int iPTHigh[3];
	unsigned int iPTHighStep[3];
	int iFTTime;//1Tune¡ä?¨ºy
	unsigned int iFTStep;//5 Tune2?3¡è
	int iFTLowTh;//50 Tune????
	int iFTHighTh;//180 Tune¨¦???
}sMatchTuneInfo;

typedef struct {
	int iLowBoundary;
	int iLowLevelCount;
	int iLow[10];
	int iLowDelta[10];
	int iHighBoundary;
	int iHighLevelCount;
	int iHigh[10];
	int iHighDelta[10];
} sNewMatchTuneInfo;

#define		REG_STG1_2_ADDR		0xFF000020
#define		REG_STG3_4_ADDR		0xFF00002C
#define		REG_STGADC_ADDR		0xFF00006C

/*
const unsigned int iREG_RXDAC_ADDR[8] = 
{
	0xFF080100,
	0xFF080104,
	0xFF080108,
	0xFF08010C,
	0xFF080110,
	0xFF080114,
	0xFF080118,
	0xFF08011C
};
*/

#endif
