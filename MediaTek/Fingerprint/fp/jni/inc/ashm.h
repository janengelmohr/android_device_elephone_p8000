/******************************************************************************/
/** @file
 *  @brief   This file contains the implementation of shared memory.
 *  @author  Zang Xiaohua
 *  @date    20090508
 *  @version 1.0.0
 *  @details 
 */
 /*****************************************************************************/
#ifndef ___VSHM_H___
#define ___VSHM_H___

#include "aostypes.h"

#ifdef __cplusplus
extern "C"
{
#endif

//******************************************************************************
// Global Macros
//******************************************************************************

//******************************************************************************
// Global Typedefs
//******************************************************************************

//******************************************************************************
// Global Function Prototypes
//******************************************************************************
/** ashm_create
 * @brief  to create a shared memory.
 * @exception NULL
 * @param     [IN] _key is the key to create shared memory 
 * @param     [IN] _sz  shared memory size
 * @param     [IN/OUT] _flag Judge if the share memory is newly constructed.
 * @return    the newly-created shared memory.
 */
ashmid_t ashm_create(akey_t _key, int _sz, int* _flag);

/** ashm_exist
 * @brief  to unregister an access assigned to interrupt no in virtual soft interrupt vectors table.
 * @exception NULL
 * @param [IN] _id is the assigned interrupt no.
 * @return true or false
 */
Boolean ashm_exist(akey_t _key);

/** ashm_destroy
 * @brief  to Destroys all resources associated with the passed share memory.
 * @exception NULL
 * @param [IN] _h is the shared memory. 
 * @return NULL
 */
void ashm_destroy(ashmid_t _h);

/** ashm_attach
 * @brief  to get the address to share memory you created or got.
 * @exception NULL
 * @param [IN] _h is the shared memory. 
 * @return the addr.
 */
void* ashm_attach(ashmid_t _h);

int ashm_deattach(void* _p);

#ifdef __cplusplus
}
#endif

#endif /// ___VSHM_H___
