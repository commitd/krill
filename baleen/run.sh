#!/bin/bash

mkdir input
rm -rf output_structure
rm -rf output_entities
mkdir output_structure
mkdir output_entities
java -jar baleen-2.3.0.jar baleen.yml
