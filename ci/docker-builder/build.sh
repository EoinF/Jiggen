#!/bin/bash
set -x

git clone https://github.com/EoinF/Jiggen.git
chmod +x Jiggen/ci/build-task.sh
cd Jiggen/ci
./build-task.sh