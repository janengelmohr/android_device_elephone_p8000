#ifndef _SILEADINC_DEV_H_
#define _SILEADINC_DEV_H_

#ifdef __cplusplus
extern "C" {
#endif

int FPCALAPI_GetOneFrame(unsigned char* frame , int length, int flag);
unsigned int  FPCALAPI_read (unsigned int total_reg);
unsigned int FPCALAPI_Write(unsigned int reg,unsigned int addr);
int FPCALAPI_HwInit();
void FPCALAPI_DeInit();
int FPCALAPI_GetOnePix();
void FPCAL_ClearIntFlag();
int FPCALAPI_AdjOk();
int FPCALAPI_GetSrcFrame(unsigned char* frame , int length, int skipeflag);
void FPCALAPI_GetTestParam(int* Frame_w, int* Frame_h);
int FPCALAPI_CheckSpi(int* id);

#ifdef __cplusplus
}
#endif

#endif
