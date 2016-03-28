//	Author:			Warren Zhao



#ifndef _FARRAY_H_
#define _FARRAY_H_

#include "metaconsts.h"
#include "metatypes.h"

enum
{
	FARRAY_FIRST_INDEX		= 0,
	FARRAY_LAST_INDEX		= 0x7fffffff,
	FARRAY_INVALID_INDEX	= -1
};

class AArray
{

public:
	AArray();
	virtual ~AArray();
	
	Int32 GetItemCount() const;
	
	virtual Int32 GetItemSize() const;

	Int32 GetIndex(
		const void* inItem
		);

	virtual Int32 Append(
		const void* inItem
		);
	virtual Int32 InsertAt(
		Int32 inIndex, 
		const void* inItem
		);
	
	virtual Int32 ReplaceAt(
		Int32 inIndex, 
		const void* inItem
		);

	virtual	Boolean GetItemAt(
		Int32 inIndex, 
		void* ioItem
		);
	
	virtual Int32 Remove(
		const void* inItem
		);
	virtual Int32 RemoveAt(
		Int32 inIndex
		);
	virtual void RemoveAll();
	

protected:
	Boolean Increase(
		Int32	inDelta
		);
	void* ItemAt(
		Int32 inIndex
		) const;
	
private:
	Boolean IsValidIndex(
		Int32 inIndex
		);
	Int32 CheckIndex(
		Int32 inIndex
		);

	Int32 mItemCount;
	
	void* mItems;

};

inline	Int32	AArray::GetItemCount() const		{ return mItemCount;		}

inline	Boolean	AArray::IsValidIndex(Int32 inIndex)	{ return ( (inIndex >= FARRAY_FIRST_INDEX) && (inIndex < mItemCount) );	}

inline	void*	AArray::ItemAt(Int32 inIndex) const	{ return ( ((UInt8*)(mItems)) + (inIndex * GetItemSize()) );			}

#endif
