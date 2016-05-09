#ifndef _SILEADINC_DEV_H_
#define _SILEADINC_DEV_H_

int FPCALAPI_GetOneFrame(char* frame , int length);
unsigned int  FPCALAPI_read (unsigned int total_reg);
unsigned int FPCALAPI_Write(unsigned int reg,unsigned int addr);
int FPCALAPI_HwInit();
void FPCALAPI_DeInit();
void FPCAL_ClearIntFlag();
int FPCALAPI_AdjOk();
#endif
