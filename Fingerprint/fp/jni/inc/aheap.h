/*  ====================================================================================================
**
**  ---------------------------------------------------------------------------------------------------
**
**	File Name:  	aheap.h
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

#ifndef _INC_AHEAP_H_
#define _INC_AHEAP_H_

#include <stddef.h>
#include "aostypes.h"

#ifdef __cplusplus
extern "C"
{
#endif

//******************************************************************************
// Global Macros
//******************************************************************************
#define AHEAP_Alloc(size)		dbAHEAP_Alloc((size),__FILE__,__LINE__)
#define AHEAP_Delete(ptr)		{dbAHEAP_Delete((ptr),__FILE__,__LINE__); (ptr)=NULL;}//heap did the if(!=NULL) already

#define MEMINFO                 amem_info
#define ALLOC(nbytes)           amem_alloc((nbytes), __FILE__, __LINE__)
#define CALLOC(count, nbytes)   amem_calloc((count), (nbytes), __FILE__, __LINE__)
#define FREE(ptr)               ((void)(amem_free((ptr), __FILE__, __LINE__), (ptr) = 0))
#define RESIZE(ptr, nbytes)     ((ptr) = amem_resize((ptr), (nbytes), __FILE__, __LINE__))

//******************************************************************************
// Global Function Prototypes
//******************************************************************************
void AHEAP_Init( 						// Creates and initializes the heap
	void   *HeapStart,					// Pointer to start of heap
	UInt32 HeapSize						// Number of bytes in heap
	);

void *dbAHEAP_Alloc(size_t size, const char* file, UInt16 line);
void dbAHEAP_Delete(void* ptr, const char* file,   UInt16 line);

extern void *amem_alloc (size_t nbytes, const char *file, UInt32 line);
extern void *amem_calloc(UInt32 count, UInt32 nbytes, const char *file, UInt32 line);
extern void  amem_free  (void *ptr, const char *file, UInt32 line);
extern void *amem_resize(void *ptr, UInt32 nbytes,const char *file, UInt32 line);
extern void  amem_info  ();

//******************************************************************************
// The following set of routines for used with Tracing (debugging)
// Do not use for other reason.
//******************************************************************************
void AHEAP_InitTraceMem( 				// Initialize trace heap memory
	void   *HeapStart,   				// Pointer to start of heap
	UInt32 HeapSize						// Number of bytes in heap
	);

void *AHEAP_AllocTraceMem(				// Allocates N bytes from the heap
	UInt32 size	 						// Number of bytes to allocate
	);

void AHEAP_DeleteTraceMem(				// Deallocates the block pointed to by ptr
	void *ptr							// Pointer to block to deallocate
	);

void AHEAP_TraceHeapUsage(				// Traces the Heap Usage
	UInt8 *str							//
	);

void AHEAP_EnableTrace( void );		// Enables tracing for Heap allocations/deallocations

UInt32 AHEAP_MediaZoneFree( void );   // Get Media Free Memory Usage status

#ifdef __cplusplus
}
#endif

#endif
