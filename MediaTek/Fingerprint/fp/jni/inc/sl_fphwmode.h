//
// By Warren Zhao
//

#ifndef __SL_FPHW_MODE_H__
#define __SL_FPHW_MODE_H__

#ifdef __cplusplus
extern "C" {
#endif

int FPHWMODE_SetMode(int inmode);

int FPHWMODE_GetMode(int* outmode,int* outpid);

#ifdef __cplusplus
}
#endif

#endif
