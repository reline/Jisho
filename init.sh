#!/usr/bin/env bash

ROOT="jishodb/src/main/res"
DIRECTORY=${ROOT}/raw

mkdir ${ROOT}
mkdir ${DIRECTORY}
printf "Inflating sources..."
for f in sources/*.zip; do
	unzip -q ${f} -d ${DIRECTORY}
done
for f in sources/*.gz; do
	STEM=$(basename "${f}" .gz)
	gunzip -c "${f}" > ${DIRECTORY}/"${STEM}"
done
echo "done"
printf "Converting files to proper UTF-8 format..."
for f in ${DIRECTORY}/*; do
	nkf -O ${f} temp && mv temp ${f}
	mv ${f} `echo ${f} | tr [:upper:] [:lower:]`
done
echo "done"
