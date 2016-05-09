/*  ====================================================================================================
**
**
**  ---------------------------------------------------------------------------------------------------
**
**	File Name:  	acbuf.h
**	
**	Description:	This file contains the interface for the Data Base.
**
**					this is kernal code of SW framework.
**					It contributes one of functionalities of SW Platform. 
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
#ifndef _ACBUF_H_
#define	_ACBUF_H_

#ifdef __cplusplus
extern "C"
{
#endif

//******************************************************************************
//		Definition
//******************************************************************************

#define	USE_ENTIRE_BUFFER	0xFFFF	// use entire buffer to lock and unlock

//******************************************************************************
//		Typedefs
//******************************************************************************

typedef UInt8 BufferDatum_t;		// Datum type

typedef struct
{
	Semaphore_t		rd_lock;
	Semaphore_t		wrt_lock;
	BufferDatum_t	*buf;
	Int16			wrt_idx;
	Int16			rd_idx;
	Int16			prev_wrt_idx;
	Int16			wrt_reserved_sz;
	Int16			rd_reserved_sz;
	Int16			buf_sz;					// total size of entire buffer
	Int16			locked_sz;
	Int16			in_use_cnt;
	UInt16			overrun_cnt;
	UInt8			id;
} CBuf_t;						// Circular Buffer

typedef enum
{
	BUFFERSTATUS_SUCCESS,			// Operation Successful
	BUFFERSTATUS_FULL,				// Buffer is full
	BUFFERSTATUS_EMPTY,				// Buffer is empty
	BUFFERSTATUS_BUSY,				// Buffer is locked and busy
	BUFFERSTATUS_FAILURE			// Operation Failed
} BufferStatus_t;

typedef enum
{
	BUFFERTHRESHOLD_NORMAL,			// Buffer threshold normal
	BUFFERTHRESHOLD_BELOW_LOW,		// Buffer threshold is below low
	BUFFERTHRESHOLD_ABOVE_HIGH		// Buffer threshold is above high
} BufferThreshold_t;

	
//******************************************************************************
//		Prototypes
//******************************************************************************
CBuf_t *ACBUF_Create(
	Int16			buf_sz,	 			// Size of CBuffer
	Int16			locked_sz			// Locked size
	);

BufferStatus_t ACBUF_Destroy(
	CBuf_t		*cbuf				// CBuffer to destroy
	);

BufferStatus_t ACBUF_Reset(
	CBuf_t		*cbuf				// CBuffer in question
	);

Int16 ACBUF_GetUsedSize(
	CBuf_t		*cbuf				// CBuffer in question
	);

Int16 ACBUF_GetFreeSize(
	CBuf_t		*cbuf				// CBuffer in question
	);

Int16 ACBUF_GetBufferSize(
	CBuf_t		*cbuf				// CBuffer in question
	);

Int16 ACBUF_GetBlock(
	CBuf_t		*cbuf,				// CBuffer in question
	BufferDatum_t	*d_ptr,				// Ptr to destination buffer
	Int16			buf_sz				// size of buffer
	);

BufferStatus_t ACBUF_LockGetBlock(
	CBuf_t		*cbuf,				// CBuffer in question
	BufferDatum_t	**d_ptr,			// Ptr to destination buffer
	Int16			*buf_sz				// size of buffer locked buffer
	);

void ACBUF_UnlockGetBlock(
	CBuf_t		*cbuf,				// CBuffer in question
	Int16			buf_sz				// size of buffer used
	);

BufferStatus_t ACBUF_GetDatum(
	CBuf_t		*cbuf,				// CBuffer in question
	BufferDatum_t	*d_ptr				// Ptr to destination buffer
	);

Int16 ACBUF_PutBlock(
	CBuf_t		*cbuf,				// CBuffer in question
	BufferDatum_t	*s_ptr,				// Ptr to source buffer
	Int16			buf_sz				// size of buffer
	);

BufferStatus_t ACBUF_LockPutBlock(
	CBuf_t		*cbuf,				// CBuffer in question
	BufferDatum_t	**d_ptr,			// Ptr to destination buffer
	Int16			*buf_sz				// size of buffer locked buffer
	);

void ACBUF_UnlockPutBlock(
	CBuf_t		*cbuf,				// CBuffer in question
	Int16			buf_sz				// size of buffer used
	);

BufferStatus_t ACBUF_PutDatum(
	CBuf_t		*cbuf,				// CBuffer in question
	BufferDatum_t	*s_ptr				// Ptr to source buffer
	);

BufferStatus_t ACBUF_UnputDatum(
	CBuf_t		*cbuf				// CBuffer in question
	);

Int16 ACBUF_FlushBlock(
	CBuf_t		*cbuf,				// CBuffer in question
	Int16 cnt							// nbr datum to flush
	);

void ACBUF_Dump( 
	CBuf_t		*cbuf				// CBuffer in question
	);

#ifdef __cplusplus
}
#endif

#endif
