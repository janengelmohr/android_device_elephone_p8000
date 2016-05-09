#ifndef _SILEAD_PERF_H_
#define _SILEAD_PERF_H_

#ifdef __cplusplus
extern "C" {
#endif
int FPPERFAPI_SaveImagesOne(void);
int FPPERFAPI_Status(void);
//unsigned int  FPPERFAPI_read (unsigned int total_reg);
//unsigned int FPPERFAPI_Write(unsigned int reg,unsigned int addr);
int FPPERFAPI_HwInit();
void FPPERFAPI_HwDeInit();

#ifdef __cplusplus
}
#endif

#endif
