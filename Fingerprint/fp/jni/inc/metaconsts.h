//	Author:			WenLi


#ifndef _MMI_MMICONSTS_H_
#define _MMI_MMICONSTS_H_

//******************************************************************************
//							Include block
//******************************************************************************
#include "metatypes.h"

//******************************************************************************
//							definition
//******************************************************************************

#ifndef TRUE
#define FALSE	0
#define TRUE	(!FALSE)
#endif

#ifndef NULL
#define NULL ((void *)0)
#endif

#ifndef MAX
#define MAX( a, b )				( (a)>(b) ? (a) : (b) )
#endif

#ifndef MIN
#define MIN( a, b )				( (a)<(b) ? (a) : (b) )
#endif 

#ifndef ABS
#define ABS( a )				( (a)>0 ? (a) : -(a) )
#endif

#ifndef MIN_MAX
#define MIN_MAX( a, x, b )		( (a)>(x) ? (a) : ( (b)>(x) ? (x) : (b) ) )
#endif

#ifdef PADDING_WORD
#error	PADDING_WORD already defined!
#endif

#define PADDING_WORD(n)						\
	(((n) % sizeof(UInt32)) ?					\
	sizeof(UInt32) - ((n) % sizeof(UInt32)) :	\
	sizeof(UInt32))

#ifdef CHECK_STRUCT_SIZE
#error	CHECK_STRUCT_SIZE already defined!
#endif
#define CHECK_STRUCT_SIZE(T)			\
	typedef struct {					\
		UInt8 padding1[1 - sizeof(T) %	\
		sizeof(UInt32)];	\
		UInt8 padding2[2 - sizeof(T) %	\
		sizeof(UInt32)];	\
		UInt8 padding3[3 - sizeof(T) %	\
		sizeof(UInt32)];	\
	} Check_ ## T

#define UINT32_ALIGN(x)		((((x)+3)>>2)<<2)
#define UINT16_ALIGN(x)		((((x)+1)>>1)<<1)

#endif
