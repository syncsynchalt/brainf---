#!/bin/sh

JAR=$(ls target/brainfuck-*-with-dependencies.jar | tail -n 1)
java -jar "${JAR}" "$@"
