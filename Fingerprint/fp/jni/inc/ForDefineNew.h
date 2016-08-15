#ifndef FOR_DEFINE_NEW_H
#define FOR_DEFINE_NEW_H

#define DC_MINUS	987654
#define DC_DIRECT	456789
#define FINGER_DETECT_NORMAL_THRESH	4
#define FINGER_DETECT_SLEEP_THRESH	2

#ifndef PI
#define		PI				3.1415926535
#endif
/*
typedef struct
{
	int iWidth;
	int iHeight;
	int iUp;
	int iDown;
	int iLeft;
	int iRight;
}sSizeInfo;

typedef struct
{
	int ifftMin;
	int icovMin;
	int bright;
	int dark;
	int mean;
	int var;
}sParam;

typedef struct
{
	int iLowBoundary;
	int iLowLevelCount;
	int iLow[10];
	int iLowDelta[10];
	int iHighBoundary;
	int iHighLevelCount;
	int iHigh[10];
	int iHighDelta[10];
}sNewMatchTuneInfo;
*/
typedef struct
{
	int iEffectiveLow;//Low Boundary for effective range
	int iEffectiveHigh;//High Boundary for effective range
	int iRecommendLow;//Low Boundary for recommended range
	int iRecommendHigh;//High Boundary for recommended range
	int iLowSectionNum;//Tune Section number for lower gray scale mean range 
	int iHighSectionNum;//Tune Section number for higher gray scale mean range 
	int iMaxStep;// The max correction value
}sFineTuneInfo;

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
