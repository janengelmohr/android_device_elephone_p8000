/*  ====================================================================================================
**
**  ---------------------------------------------------------------------------------------------------
**
**    File Name:      atask.h
**    
**    Description:    This file contains the implementation of OS wrapper.
**
**                    this is kernal code of SW framework.
**                    It contributes one of functionalities of SW Platform. 
**                    If the checkin is CR not PR, to add change History to this file head part 
**                    will be appreciated.
**
**  ---------------------------------------------------------------------------------------------------
**
**  Author:			Warren Zhao
**
** -------------------------------------------------------------------------
**
**    Change History:
**    
**    Initial revision
**
**====================================================================================================*/

#ifndef _ATASK_H_
#define _ATASK_H_

#ifdef __cplusplus
extern "C"
{
#endif

#include "aostypes.h"

//******************************************************************************
// Global Macros
//******************************************************************************
#define ATASKNAME_LEN 127

//******************************************************************************
// Global Typedefs
//******************************************************************************
#ifdef WIN32
#include <process.h>
typedef enum { 
    IDLE            = THREAD_PRIORITY_IDLE,
    LOWEST            = THREAD_PRIORITY_LOWEST,
    BELOW_NORMAL    = THREAD_PRIORITY_BELOW_NORMAL,
    NORMAL            = THREAD_PRIORITY_NORMAL,
    ABOVE_NORMAL    = THREAD_PRIORITY_ABOVE_NORMAL,
    HIGHEST            = THREAD_PRIORITY_HIGHEST
} TPriority_t;

#else
/** @brief This is the vthread detach status attribute. */
typedef enum
{
    ATASK_CREATE_JOINABLE,
    ATASK_CREATE_DETACHED,
} ATASK_DETACH_Status;

/** @brief This is the vthread contention scope attribute. */
typedef enum
{
    ATASK_SCOPE_PROCESS,
    ATASK_SCOPE_SYSTEM,
} ATASK_Scope;

/** @brief This is the vthread scheduling policy attribute. */
typedef enum
{
    ATASK_SCHED_RR,
    ATASK_SCHED_FIFO,
    ATASK_SCHED_OTHER,
} ATASK_SCHED_Policy;

/** @brief This is the vthread priority attribute. */
typedef enum
{ 
	IDLE            = 0,
	LOWEST          = 20,
	BELOW_NORMAL    = 40,
	NORMAL          = 60,
	ABOVE_NORMAL    = 80,
	HIGHEST         = 100
} ATASK_Priority;
typedef ATASK_Priority TPriority_t;
#endif

typedef void    *Task_t;
typedef char    *TName_t;
typedef unsigned long    TArgc_t;
typedef void    *TArgv_t;
typedef void*    (*TEntry_t)( void* );
typedef void    (*TEntryWArg_t)( TArgc_t, TArgv_t );
typedef unsigned long    TStackSize_t;

//******************************************************************************
// Global Function Prototypes
//******************************************************************************
Task_t ATASK_Create(                    // returns the newly-created task
    TEntry_t        entry,                        // task function entry point
    TArgv_t            argv,
    TName_t            task_name,                    // task name
    TPriority_t        priority,                // task priority, bigger is higher
    TStackSize_t    stack_size                // task stack size (in UInt8)
    );

void ATASK_Destroy(                    // Terminate and destroy all the
    Task_t            t                            // resource associated with the passed task
    );
                                        // returns previous task priority
TPriority_t ATASK_ChangePriority(        // change task priority
    Task_t            t,                            // task pointer
    TPriority_t        new_priority            // new task priority
    );

void ATASK_Suspend(                    // suspend task until resumed;
    Task_t            t                            // task pointer
    );

void ATASK_Resume(                        // resume suspended task;
    Task_t            t                            // task pointer
    );

void ATASK_Sleep(						// suspend task, until timeout
	Ticks_t			timeout						// task sleep timeout
	);

OSStatus_t ATASK_GetThreadName(        // get ASCII name of thread
    Task_t            t,                            // task or HISR pointer
    TName_t           p_name                        // location to store the ASCII name
    );

OSStatus_t ATASK_GetTaskName(            // get ASCII name of task
    Task_t            t,                            // task pointer
    TName_t         p_name                        // location to store the ASCII name
    );

OSStatus_t ATASK_GetHISRName(            // get ASCII name of HISR
    Task_t            t,                            // HISR pointer
    TName_t         p_name                        // location to store the ASCII name
    );

#ifdef __cplusplus
}
#endif

#endif

