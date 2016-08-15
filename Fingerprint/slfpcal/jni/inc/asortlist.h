/*  ====================================================================================================
**
**  ---------------------------------------------------------------------------------------------------
**
**    File Name:      asortlist.h
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
#ifndef ___ASORTLIST_H___
#define ___ASORTLIST_H___

#ifdef __cplusplus
extern "C"
{
#endif

struct _sortlist_t;
/* typedef struct _sortlist_t list_t;
*/
typedef struct _sortlist_t asortlist_t;

/*
* Description: Create sort list 
*
* Parameters:
*    _data_dup_fn: function to duplicate _data
*    _data_del_fn: function to delete _data.
*
* Note:
*    if _data_dup_fn is null, use _data as a new node directly, but user who use 
*    sortlist must manage the memory the _data uses.
*/
typedef void* (*data_dup)(void*);
typedef void  (*data_del)(void*);
typedef int   (*data_cmp)(void*, void*);

asortlist_t* sortlist_create(data_dup _data_dup_fn,
                           data_del _data_del_fn,
                           data_cmp _data_cmp_fn);

/*
* Description: free sort list 
*
*/
void sortlist_free(asortlist_t* _list);

/*
* Description: Append data to the end of sort list .
*
*/
int sortlist_append(void* _data, asortlist_t* _list);

/*
* Description: Insert data to the sort list.
*
*/
int sortlist_insert(void* _data, asortlist_t* _list);

/*
* Description: Find data.
*
*/
void* sortlist_find(void* _data, asortlist_t* _list);

/*
* Description: Remove Item from sort list 
*
*/
void sortlist_remove(void* _data, asortlist_t* _list);

/*
* Description: Invoke _cb callback function for each element.
*
*/
void sortlist_foreach_do(asortlist_t* _list, int (*_cb)(void* _data));

/*
* Description: Return the count of the sort list 
*
*/
unsigned long sortlist_count(asortlist_t* _list) ;

#ifdef __cplusplus
}
#endif

#endif /// ___ASORTLIST_H___
