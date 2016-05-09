#ifndef __FPPARM_HEADER_H__
#define __FPPARM_HEADER_H__

#include "metatypes.h"

#define SYSPARAM_PARAM_DEVICE	(SLFP_GetFPParmFile("PARM"))
#define SYSPARAM_DEVICE			(SLFP_GetFPParmFile("CFG"))

int SLFP_LoadSysParam(
	UInt32 		chip_id
	);

const char* SLFP_GetFPParmFile(
	char*		option
	);

#endif
