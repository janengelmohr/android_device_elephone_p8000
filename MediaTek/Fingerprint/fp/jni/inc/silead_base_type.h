#ifndef SILEAD_BASE_TYPE_H
#define SILEAD_BASE_TYPE_H

#include <linux/types.h>

#define ANDROID_LOG_INFO "fpcal" 
#define __android_log_print(i, m, f,...)	printf("\n%s:%s",i,m); printf(f,##__VA_ARGS__);

#define SILEAD_TRUE			1
#define SILEAD_FALSE		0

//
#define ARRAY_SIZE(a) (sizeof(a) / sizeof((a)[0]))


#endif
