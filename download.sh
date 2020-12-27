#!/bin/bash

# Removed yes/no prompt from start.sh and
# automatically download dependency and index files.

#####################################################################
# Check for dependencies
echo "Checking dependencies..."
checker="gerbil_data/.ignore_download"
file="gerbil_data/gerbil_data.zip"
url="https://github.com/dice-group/gerbil/releases/download/v1.2.6/gerbil_data.zip"

mkdir -p "gerbil_data/cache" || error "Could not create gerbil_data/cache directory"

if [ ! -f "$checker" ]; then
    touch "checker"
    echo "Downloading dependencies ... ($url)"
    curl --retry 4 -L -o "$file" "$url"

    if [ ! -f "$file" ]; then
        error "Couldn't downloading dependency data: $file"
    else
        echo "Extracting dependencies ... "
        unzip "$file"
    fi
fi

#####################################################################
# Check for dbpedia sameAs index
echo "Checking DBpedia sameAs index..."
if [ ! -d "gerbil_data/indexes/dbpedia" ]; then
	mkdir -p "gerbil_data/indexes/dbpedia" || error "Could not create gerbil_data/indexes/dbpedia directory"
	file="gerbil_data/indexes/dbpedia/dbpedia_index.zip"
	url="https://hobbitdata.informatik.uni-leipzig.de/gerbil/dbpedia_index_2016.zip"
	echo "Downloading index ... ($url)"
	curl --retry 4 -L -o "$file" "$url"

	if [ ! -f "$file" ]; then
		echo "Couldn't downloading index file: $file"
	else
		echo "Extracting index ... "
		unzip "$file" -d "gerbil_data/indexes/dbpedia"
	fi
fi

#####################################################################
# Check for dbpedia entity check index
echo "Checking DBpedia entity check index..."
if [ ! -d "gerbil_data/indexes/dbpedia_check" ]; then
	mkdir -p "gerbil_data/indexes/dbpedia_check" || error "Could not create gerbil_data/indexes/dbpedia_check directory"
	file="gerbil_data/indexes/dbpedia_check/dbpedia_check_index.zip"
	url="https://hobbitdata.informatik.uni-leipzig.de/gerbil/dbpedia_check_index_2017.zip"
	echo "Downloading index ... ($url)"
	curl --retry 4 -L -o "$file" "$url"

	if [ ! -f "$file" ]; then
		echo "Couldn't downloading index file: $file"
	else
		echo "Extracting index ... "
		unzip "$file" -d "gerbil_data/indexes/dbpedia_check"
	fi
fi
