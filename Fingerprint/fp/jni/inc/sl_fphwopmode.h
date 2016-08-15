#ifndef __SL_HARDWARE_OPMODE_HEADER_H__
#define __SL_HARDWARE_OPMODE_HEADER_H__

#include "metatypes.h"

#ifdef __cplusplus
extern "C" {
#endif

int set_int_mode();
int set_poll_mode();
int set_stop_mode();
int set_start_frm_mode();
int set_rxadc_values(
	unsigned int*	inpvals,
	int				inlen
	);

#ifdef __cplusplus
}
#endif
#endif


