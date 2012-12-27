#!/bin/sh

JAR=$(ls target/brainfuck-*.jar | tail -n 1)
java ${JAR}
