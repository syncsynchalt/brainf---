#!/bin/sh

JAR=$(ls target/brainfuck-1.0.jar | tail -n 1)
java -jar "${JAR}" "$@"
