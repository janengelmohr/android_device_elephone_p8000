/*  ====================================================================================================
**
**  ---------------------------------------------------------------------------------------------------
**
**	File Name:  	asemaphore.h
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
#ifndef ___ASEMAPHORE_H___
#define ___ASEMAPHORE_H___

#include "aostypes.h"

#ifdef __cplusplus
extern "C"
{
#endif

#define ASEM_Key       0x2000

//******************************************************************************
// Global Macros
//******************************************************************************
#define ASEMAPHORE_Create(___name,___maxval,___val,___mode,___mutex)      __asema_create(___name,___val,___maxval,0,___mutex)
#define ASEMAPHORE_Create_IPC(___name,___maxval,___val,___mode,___mutex)  __asema_create(___name, ___val, ___maxval, 1,___mutex)
#define ASEMAPHORE_Destroy(___s)                  asema_destroy(___s)
#define ASEMAPHORE_Obtain(___s,___ms)             asema_acquire_timed(___s, ___ms)
#define ASEMAPHORE_Obtain2(___s)                  asema_acquire(___s)
#define ASEMAPHORE_Release(___s)                  asema_release(___s,1)
#define ASEMAPHORE_GetHandle_IPC(___name)         asema_get_handle_ipc(___name)
#define ASEMAPHORE_Value(___s)                    asema_value(___s)
#define ASEMAPHORE_FindLocalSema(___name)         asema_find_local_sema(___name)

//******************************************************************************
// Global Typedefs
//******************************************************************************
typedef asema_t       Semaphore_t;
typedef unsigned long SCount_t;

//******************************************************************************
// Global Function Prototypes
//******************************************************************************
//internal use only. external module should not call this.
int asema_get_returnsema_parents(
	asema_t  _sema, 
	char*    _relayer_name,
	int      _max_relayer_name_sz
	);

asema_t asema_find_local_sema(
	char* _sema_name
	);

/** asema_create
 * @brief  Creates a new semaphore or obtains the semaphore identifier of an existing semaphore.
 * @param  [IN] _name is semaphoreset name.
 * @param  [IN] _maxval is the max number of available semaphores.
 * @param  [IN] _val  is the init val of the semaphore.
 * @exception NULL
 * @return the newly-created semaphore identifier.
 */
asema_t __asema_create(
	char*          _name,	
	unsigned short _val, 
	unsigned short _maxval, 
	int            _is_shared,
	int            _is_mutex
	);
asema_t asema_get_handle_ipc(
	char* _name
	);

/** asema_destroy
 * @brief  Delete the specified semaphore, the times the semaphore referenced reduce 1 per launching until the count equal 0, then destroy the semaphore set.
 * @patam  [IN] _s the specified semaphore.
 * @exception NULL
 * @return    NULL
 */
void asema_destroy(
	asema_t _s
	);

/** asemaset_acquire
 * @brief  wait to obtain a semaphore
 * @param  [IN] _s the specified semaphore.
 * @exception NULL
 * @return    A return value of OSSTATUSFailure represents the <asema_acquire> operation failed, OSSTATUSSuccess sucessful.
 */
aosstatus_t asema_acquire(            // wait to obtain a semaphore
    asema_t _s                        // semaphore pointer
    );

/** asemaset_acquire_timed
 * @brief  wait to obtain a semaphore
 * @param  [IN] _s the specified semaphore.
 * @param  [IN] _ms    timeout to acquire semaphore (millisecond).
 * @exception NULL
 * @return    A return value of OSSTATUSFailure represents the <asema_acquire> operation failed, OSSTATUSSuccess sucessful.
 */
aosstatus_t asema_acquire_timed(      // wait to obtain a semaphore
    asema_t       _s,                 // semaphore pointer
    unsigned long _ms                 // timeout to failed semaphore obtain
    );

/** asemaset_release
 * @brief  Release a semaphore
 * @param  [IN] _s the specified semaphore.
 * @param  [IN] _which which semaphore of the semaphore is operated.
 * @exception NULL
 * @return    A return value of OSSTATUSFailure represents the <asemaset_acquire> operation failed, OSSTATUSSuccess sucessful.
 */
aosstatus_t asema_release(            // wait to release a semaphore
    asema_t _s,                        // semaphore pointer
	int     _is_crash_handler
    );

int asema_value(
	asema_t _s
	);

#ifdef __cplusplus
}
#endif

#endif /// ___ASEMAPHORE_H___
