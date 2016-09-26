extern "C" {
    void _ZN7android8String165setToEPKDsj(char16_t const*, unsigned int);

    void _ZN7android8String165setToEPKtj(unsigned short const* str, unsigned int len){
        _ZN7android8String165setToEPKDsj((char16_t const*)str, len);
    }

    void _ZN7android8String16C1EPKDsj(char16_t const*, unsigned int);

    void _ZN7android8String16C1EPKtj(unsigned short const* o, unsigned int len){
        _ZN7android8String16C1EPKDsj((char16_t const*)o, len);
    }
}