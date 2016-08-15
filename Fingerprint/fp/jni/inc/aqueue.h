/*  ====================================================================================================
**
**  ---------------------------------------------------------------------------------------------------
**
**	File Name:  	aqueue.h
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
#ifndef ___AQUEUE_H___
#define ___AQUEUE_H___

#include "aostypes.h"

#ifdef __cplusplus
extern "C"
{
#endif

//******************************************************************************
// Global Macros
//******************************************************************************
#define BLOCK_FOREVER 0xffffffff

#define AQUEUE_Create(___name,___entries,___msgsz,___mode) __aqueue_create(___name, ___entries, ___msgsz, ___mode, 0)
#define AQUEUE_Create_IPC(___name,___entries,___msgsz,___mode) __aqueue_create(___name, ___entries, ___msgsz, ___mode, 1)
#define AQUEUE_Destroy(___q)                    aqueue_destroy(___q)
#define AQUEUE_FlushQ(___q)                     aqueue_flush(___q)
#define AQUEUE_Pend(___q,___msg,___ms)          aqueue_get(___q, ___msg, ___ms)
#define AQUEUE_Post(___q,___msg,___ms)          aqueue_put(___q, ___msg, ___ms)
#define AQUEUE_PostToFront(___q,___msg,___ms)   aqueue_put_front(___q, ___msg, ___ms)
#define AQUEUE_IsEmpty(___q)                    aqueue_is_empty(___q)
#define AQUEUE_GetNUQueueName(___q,___name)     aqueue_get_name(___q, ___name)
#define AQUEUE_GetHandle_IPC(___name)           aqueue_get_handle_ipc(___name)
#define AQUEUE_DeleteHandle_IPC(___q)           aqueue_delete_handle_ipc(___q)

//******************************************************************************
// Global Typedefs
//******************************************************************************
typedef unsigned long   QEntries_t;
typedef unsigned long   QMsgSize_t;
typedef void*           QMsg_t;
typedef aqueue_t	    Queue_t;


//******************************************************************************
// Global Function Prototypes
//******************************************************************************

/** __aqueue_create
 * @brief  to create a queue.
 * @exception NULL
 * @param     [IN] _name is the name of the queue or NULL.
 * @param     [IN] _entries is the number of the entries.
 * @param     [IN] _size is the sixed-size of message.
 * @param     [IN] _mode is the pending type.
 * @return    the newly-create queue.
 */
aqueue_t __aqueue_create(        /// return newly-created queue
    char*           _name,      /// The name of the queue
    QEntries_t      _entries,   /// The number of the entries
    QMsgSize_t      _size,      /// fixed-size of message
    OSSuspend_t     _mode,       /// mode of pending
	int             _shared
    );
aqueue_t aqueue_get_handle_ipc(
	char*          _name
	);
void aqueue_delete_handle_ipc(
	aqueue_t       _q
	);

/** aqueue_destroy
 * @brief  to create a queue.
 * @exception NULL
 * @param     [IN] _q is the queue you want to delete. the times the queue referenced reduce 1 per launching 
                   until the count equal 0, then destroy the queue.
 * @return    NULL.
 */
void aqueue_destroy(            /// Destroys all resources associated
    aqueue_t    _q             /// with the passed queue
    );

/** aqueue_flush
 * @brief  to clear queue.
 * @exception NULL
 * @param     [IN] _q is the queue you want to clear.
 * @return    NULL
 */
void aqueue_flush(              /// clear queue
    aqueue_t _q
    );

/** aqueue_get
 * @brief  to get message from the queue.
 * @exception NULL
 * @param     [IN] _q is the queue from which you get message.
 * @param     [IN] _msg is the pointer to empty message.
 * @param     [IN] _ms  is timeout to get message.
 * @return    A return value of OSSTATUSFailure represents the operation failed, OSSTATUSSuccess sucessful.
 */
OSStatus_t aqueue_get(          /// get message from the queue
    aqueue_t      _q,           /// queue pointer
    QMsg_t        _msg,         /// pointer to message
    unsigned long _ms           /// timeout to pend failure
    );

/** aqueue_put
 * @brief  to put message to the queue.
 * @exception NULL
 * @param     [IN] _q is the queue to which you append message.
 * @param     [IN] _msg is the pointer to message.
 * @param     [IN] _ms  is timeout to get message.
 * @return    A return value of OSSTATUSFailure represents the operation failed, OSSTATUSSuccess sucessful.
 */
OSStatus_t aqueue_put(          /// put message to queue
    aqueue_t      _q,           /// queue pointer
    QMsg_t        _msg,         /// pointer to message
    unsigned long _ms           /// timeout to post failure
    );

/** aqueue_put_front
 * @brief  to put message to the queue.
 * @exception NULL
 * @param     [IN] _q is the queue to which you add message at head.
 * @param     [IN] _msg is the pointer to message.
 * @param     [IN] _ms  is timeout to get message.
 * @return    A return value of OSSTATUSFailure represents the operation failed, OSSTATUSSuccess sucessful.
 */
OSStatus_t aqueue_put_front(    /// put message to front of queue
    aqueue_t      _q,           /// queue pointer
    QMsg_t        _msg,         /// pointer to message
    unsigned long _ms           /// timeout to post failure
    );

/** aqueue_is_empty
 * @brief  to check if there are any messages in queue.
 * @exception NULL
 * @param     [IN] _q is the queue.
 * @return    true or false
 */
Boolean aqueue_is_empty(         /// Check if there are any messages in queue
    aqueue_t _q                /// queue pointer
    );

/** aqueue_is_full
 * @brief  to check if the queue is full.
 * @exception NULL
 * @param     [IN] _q is the queue.
 * @return    true or false
 */
Boolean aqueue_is_full(aqueue_t _q);

/** aqueue_get_name
 * @brief  to gets ASCII name of queue.
 * @exception NULL
 * @param     [IN] _q is the queue.
 * @param     [IN/OUT] _name location to storage the queue name.
 * @return    A return value of OSSTATUSFailure represents the operation failed, OSSTATUSSuccess sucessful.
 */
OSStatus_t aqueue_get_name(     /// gets ASCII name of NU queue
    aqueue_t    _q,            /// queue pointer
    char*        _name          /// location to storage the queue name
    );

#ifdef __cplusplus
}
#endif

#endif /// ___AQUEUE_H___
