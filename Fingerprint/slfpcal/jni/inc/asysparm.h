/*  ====================================================================================================
**
**  ---------------------------------------------------------------------------------------------------
**
**    File Name:      asysparm.h
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
#ifndef ___ASYSPARM_H___
#define ___ASYSPARM_H___

#ifdef __cplusplus
extern "C"
{
#endif

/***************************************
 * Global Typedefs
***************************************/
typedef void* ASYSPARMSubject_t;

/***************************************
 * Global Function Prototypes
***************************************/

/** ASysParm_Init
 * @brief  to init new system parameter module on share memory.
 * @exception NULL
 * @param [IN] _sz the number of files to store system parameters.
 * @return 0 means successful, otherwise failure.
 */
int ASysParm_Init(int _sz, ...);

/** ASysParm_Shutdown
 * @brief to destroy the system parameter module indicated by argument.
 * @param [IN] NULL
 * @return NULL
 */
void ASysParm_Shutdown();

/** ASysParm_GetSubject
 * @brief to retrieve the suject by section name and key.
 * @param [IN] _sectname the section name by which to find the right section.
 * @param [IN] _key the key by which you can retrieve the subject you want.
 * @param [OUT] psubject_ the subject you retrieved.
 * @return the size of the value array; <0 mean failure. 
 */
int  ASysParm_GetSubject(
        char* _sectname,
        char* _key, 
        ASYSPARMSubject_t* psubject_);

/** ASysParm_GetNumber
 * @brief to retrieve the NUM-th value referrd by subject and index.
 * @param [IN] _subject the subject retrieved by API(ASysParm_GetSubject).
 * @param [IN] _idx the index of the subject` values.
 * @return the _idx-th value of the subject.
 */
int ASysParm_GetNumber(ASYSPARMSubject_t _subject, int _idx);

/** ASysParm_GetString
 * @brief to retrieve the NUM-th value referred by subject and index.
 * @param [IN] _subject the subject retrieved by API(ASysParm_GetSubject).
 * @param [IN] _idx the index of the subject values.
 * @return the _idx-num value of the subject.
 */
char* ASysParm_GetString(ASYSPARMSubject_t _subject, int _idx);

#ifdef __cplusplus
}
#endif

#endif

