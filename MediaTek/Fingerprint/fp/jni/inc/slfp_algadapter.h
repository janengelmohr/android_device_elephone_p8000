#ifndef __SLFP_ALG_ADAPTER_HEADER__
#define __SLFP_ALG_ADAPTER_HEADER__

#include "FingerDetect.h"

#include "ForDefine.h"
#include "paser_config.h"

typedef unsigned int UINT;

void SLFP_AlgInit(int iWidth, int iHeight, int* Algparam);

int MatchData(
	unsigned char*    	img, 
	int 				iMatchTuneOn, 
	unsigned int* 		iTuneDAC, 
	unsigned int* 		iTuneAG20, 
	unsigned int* 		iTuneAG2C, 
	int 				iHeight, 
	int 				iWidth,
	int*				pmatch_tune_finger_detect_ret
	);

int FuncInitial(
	unsigned char*  IMGBUF, 
	int             iMTOn, 
	UINT*           iCurrentVal,
	_Param*		para,
	int 		iHeight, 
	int 		iWidth
	);

	
int InitIsFirst();

void AlgGetSleepInfo(
		unsigned int* 	iTuneDAC, 
		unsigned int* 	iTuneAG20, 
		unsigned int* 	iTuneAG2C, 
		unsigned int* 	iThreshold
		);


#endif

