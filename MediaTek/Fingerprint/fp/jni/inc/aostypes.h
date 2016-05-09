/*  ====================================================================================================
**
**  ---------------------------------------------------------------------------------------------------
**
**	File Name:  	aostypes.h
**	
**	Description:	This file contains the implementation of OS wrapper.
**
**					this is kernal code of SW framework.
**					It contributes one of functionalities of SW Platform. 
**					If the checkin is CR not PR, to add change History to this file head part 
**					will be appreciated.
**
**  ---------------------------------------------------------------------------------------------------
**
**  Author:			Warren Zhao
**
** -------------------------------------------------------------------------
**
**	Change History:
**	
**	Initial revision
**
**====================================================================================================*/

#ifndef _AOSTYPES_H_
#define _AOSTYPES_H_

#ifdef __cplusplus
extern "C"
{
#endif

#include "metatypes.h"

/***************************************
 Global Typedefs
***************************************/
#define OSMGR_Key       0x4219ceed

#define OSTAGName_Len      64
#define OSTAGArgs_Len      2048

typedef int    akey_t;
typedef int    aid_t;
typedef int    ashmid_t;
typedef int    asemasetid_t;
typedef void*  asema_t;
typedef void*  aqueue_t;
typedef int    OSResult_t;
typedef enum
{
    OSSUSPEND_Fifo,
    OSSUSPEND_Priority,
} OSSuspend_t;

typedef enum
{
    OSSTATUSSuccess = 0,
    OSSTATUSTimeout,
    OSSTATUSIntr,
    OSSTATUSBufffull,
    OSSTATUS_Threshold,
    OSSTATUSFailure = 255,
} OSStatus_t;
typedef OSStatus_t aosstatus_t;

typedef UInt32 Ticks_t;

#ifdef WIN32
#define TICKS_FOREVER					((Ticks_t)INFINITE)
#define TICKS_NO_WAIT					((Ticks_t)0)
#define TICKS_ONE_SECOND				((Ticks_t)100*10)	// ticks per second
#else
#define TICKS_FOREVER					((Ticks_t)0xffffffff)
#define TICKS_NO_WAIT					((Ticks_t)0)
#define TICKS_ONE_SECOND				((Ticks_t)100*10)	//ticks per second

#endif

#ifndef offsetof
#define offsetof(TYPE, MEMBER) ((size_t) &((TYPE *)0)->MEMBER)
#endif

/*
#define container_of(ptr, type, member) ({			\
	const typeof( ((type *)0)->member ) *__mptr = (ptr);	\
	(type *)( (char *)__mptr - offsetof(type,member) );})
*/

#define container_of(ptr, type, member)  ( (type *)( (char *)ptr - offsetof(type,member) ) )

#ifdef __cplusplus
}
#endif

#endif

