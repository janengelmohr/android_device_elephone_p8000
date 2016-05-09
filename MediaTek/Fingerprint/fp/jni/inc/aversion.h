//	Author:			Warren Zhao
#ifndef __SL_VERSION__
#define __SL_VERSION__

#ifdef __cplusplus
extern "C"
{
#endif

//-------------------------
// GetSVN(): return MS SVN
//-------------------------
const char *GetSVN(void);

const char *GetLinkSignature(void);

const char *GetPlatformRev(void);

const char *GetProjName(void);

#ifdef __cplusplus
}
#endif

#endif