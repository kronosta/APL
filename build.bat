del APL.jar
rmdir src\build
cd src
mkdir build
javac -encoding UTF-8 -Xmaxerrs 1000 -d ./build @../java-files.txt
cd build
jar -cvfe APL.jar APL.Main @../../java-class-files.txt
move APL.jar ../../APL.jar
cd ../..