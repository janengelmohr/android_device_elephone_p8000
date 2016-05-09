
#ifndef __SLCRYPTION_HEADER__
#define __SLCRYPTION_HEADER__

int SLCRYPT_DeCodingString(
	unsigned char*s, 
	unsigned char*d, 
	int* out_sz
	);

int SLCRYPT_EnCodingString(
	unsigned char *s, 
	unsigned char *d, 
	int in_sz
	); 
int SLCRYPT_EnCodingFile(
	char* src_fn, 
	char* des_fn
	);

#endif