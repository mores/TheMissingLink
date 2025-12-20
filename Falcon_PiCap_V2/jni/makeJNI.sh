#!/bin/sh

if [ ! -n "${JAVA_HOME}" ]; then
    echo "The variable JAVA_HOME needs to be set."
    exit
fi

javac -h . HelloJNI.java

gcc -Wall -g -I"${JAVA_HOME}/include" -I"${JAVA_HOME}/include/linux" -I/usr/include/libdrm -shared -fPIC -o libhello.so HelloJNI.c -ldrm

java -Djava.library.path=. HelloJNI
