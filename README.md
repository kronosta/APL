# New features
kronosta/APL is a fork of dzaima/APL. I added the following:
- Inner Product now fully works, in vectors, matrices, and higher dimensions. (dzaima/APL only supports vectors as arguments.)
  - Inner Product now also works slightly differently than standard. While normally the right argument applies to two whole vectors, they now operate on scalars as if the Each operator was applied. This does not affect scalar functions, so most use cases are unaffected. However, this will make inner products involving non-scalar functions behave differently.
- Removed SBCS (1-byte APL character set) functionality since I plan to add more characters
  - You should use Unicode instead.
- Stencil operator (⌺) is here! I wasn't going to let the only implementation of Stencil be proprietary (or GPLed with its restrictions).
- The Fun Value (◫) function is a completely original function that can enclose functions and operators into values. 
  - These values can then be called (if functions) or applied to other functions (if operators).
  - It's even possible to create "operator operators" as regular functions applied to valued operators.
  - See docs/kronosta-builtins.txt for more info on this function.
- The ⎕STR system function allows you to access the output form of a value as a string.
  - The Format (⍕) function only works for numbers.
- The ⎕FILE system function can do file I/O and other file operations (no directory support yet though, use ⎕SH for that).
- The ⎕OSI system function can be used to detect operating system information


# How to build and run
[docs](https://github.com/dzaima/APL/blob/master/docs/chars.txt) | [differences from Dyalog APL](https://github.com/dzaima/APL/blob/master/docs/differences.txt)

An APL re-implementation in Java.

`./build` to build on Unix, `build.bat` to build on Windows, `./REPL` to start a REPL.

[Android app](https://github.com/dzaima/APL/tree/master/AndroidIDE)

# Processing integration

[docs](https://github.com/dzaima/APL/blob/master/APLP5/docs)

1. run `convert.py` in the folder `app`
2. run `APLP5` in [Processing](https://processing.org)

To choose what file to run as APL, in `void settings` change the `args` array (or export & pass an actual argument) with the filename. Some examples are given in the folder `data`.
