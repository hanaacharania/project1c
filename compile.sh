#! /usr/bin/env bash

show_usage_and_exit () {
    printf "USAGE: $1 <source pathname (.src)>\n"
    exit
}

if [[ $# != 1 ]]; then
    show_usage_and_exit "$0"
fi

# Get the source path's base and type extention, checking the latter.
sourcePath=$1
extension=${sourcePath##*.}
if [[ ${extension} != "src" ]]; then
    show_usage_and_exit "$0"
fi
base=$(basename ${sourcePath} .${extension})

compilationLogPath=${base}.log
assemblyPath=${base}.asm
objectPath=${base}.o
executable=${base}

java Compiler ${sourcePath} > ${compilationLogPath} 2>&1
nasm -felf64 -gdwarf ${assemblyPath}
gcc -no-pie -z noexecstack -o ${executable} ${objectPath}
