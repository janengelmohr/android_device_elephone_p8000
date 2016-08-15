/*  ====================================================================================================
**
**
**  ---------------------------------------------------------------------------------------------------
**
**	File Name:  	metatypes.h
**	
**	Description:	This file contains the meta types definitions.
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

#ifndef _META_TYPES_H_
#define _META_TYPES_H_

typedef unsigned char	UInt8;
typedef unsigned short	UInt16;
typedef signed   char	Int8;
typedef signed   short	Int16;
typedef unsigned char	Boolean;
typedef char			INT8;
typedef short			INT16;
#ifdef BUILD_CPU_BUSWIDTH64
typedef unsigned long	PTR_T;
typedef unsigned long	UInt64;
typedef signed   long	Int64;
typedef long			INT64;
typedef unsigned int	UInt32;
typedef signed   int	Int32;
typedef unsigned int	BitField;
typedef int				INT32;
#else
#define wrong_type		UInt64
#define wrong_type		Int64
#define wrong_type		INT64
typedef unsigned long	PTR_T;
typedef unsigned long	UInt32;
typedef signed   long	Int32;
typedef unsigned int	BitField;
typedef long			INT32;
#endif

#define ATrue  1
#define AFalse 0

#define TRUE  1
#define FALSE 0

#ifdef NULL
#undef					NULL
#endif
#define NULL 0
//unify the meta types......
#ifdef BOOL
#undef					BOOL
#endif

#ifdef CHAR
#undef					CHAR
#endif

#ifdef SHORT
#undef					SHORT
#endif

#ifdef LONG
#undef					LONG
#endif

#ifdef	VOID
#undef					VOID
#endif

#ifndef	INLINE
#define INLINE			__inline
#endif

typedef UInt16 wchar;

#endif

