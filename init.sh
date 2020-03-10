#!/usr/bin/env bash

ROOT="jishodb/build"
DIRECTORY=${ROOT}/dict

mkdir ${ROOT}
mkdir ${DIRECTORY}
printf "Inflating sources...\n"
SOURCES=jishodb/src/main/res/dict
# sources can also be found at http://ftp.monash.edu/pub/nihongo/
for f in ${SOURCES}/*.zip; do
	unzip -q ${f} -d ${DIRECTORY}
done
for f in ${SOURCES}/*.gz; do
	STEM=$(basename "${f}" .gz)
	gunzip -c "${f}" > ${DIRECTORY}/"${STEM}"
done
echo "Done"
printf "Converting files to proper UTF-8 format...\n"
for f in ${DIRECTORY}/*; do
	nkf -O ${f} temp && mv temp ${f}
	mv ${f} `echo ${f} | tr [:upper:] [:lower:]`
done
echo "Done"
