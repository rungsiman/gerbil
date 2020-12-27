#!/bin/bash

export REPOSITORY="rungsiman"
export PROJECT="gerbil"
export VERSION=$(mvn -q -Dexec.executable="echo" -Dexec.args='${project.version}' --non-recursive org.codehaus.mojo:exec-maven-plugin:1.3.1:exec)

if [[ "$1" == "build" ]]; then
    echo "Building a docker image with tag '$REPOSITORY/$PROJECT'..."

    docker build -t rungsiman/$PROJECT .
    docker tag rungsiman/$PROJECT rungsiman/$PROJECT:$VERSION
    docker tag rungsiman/$PROJECT rungsiman/$PROJECT:latest

elif [[ "$1" == "run" ]]; then
    sh download.sh
    echo "Running '$REPOSITORY/$PROJECT'..."

    docker run -it --rm \
        -p 8080:8080 \
        --name gerbil \
        --mount type=bind,source="$(pwd)"/gerbil_data,target=/gerbil_data \
        "$REPOSITORY/$PROJECT"

elif [[ "$1" == "upload" ]]; then
    echo "Uploading '$REPOSITORY/$PROJECT'..."

    sudo docker push rungsiman/$PROJECT:$VERSION
    sudo docker push rungsiman/$PROJECT:latest
fi
