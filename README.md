# New features
kronosta/APL is a fork of dzaima/APL. I added the following:
- Inner Product now fully works, in vectors, matrices, and higher dimensions. (dzaima/APL only supports vectors as arguments.)
- Removed SBCS (1-byte APL character set) functionality since I plan to add more characters
  - You should use Unicode instead.

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
