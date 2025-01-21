#!/usr/bin/env bash

# requires curl & jq

(
set -e
PS1="$"

current=$(cat gradle.properties | grep leafCommit | sed 's/leafCommit = //')
upstream=$(git ls-remote https://github.com/Winds-Studio/Leaf | grep ver/1.21.1 | cut -f 1)

if [ "$current" != "$upstream" ]; then
    sed -i 's/leafCommit = .*/leafCommit = '"$upstream"'/' gradle.properties
    {
      ./gradlew applyPatches --stacktrace && ./gradlew createMojmapPaperclipJar --stacktrace && ./gradlew rebuildPatches --stacktrace
    } || exit

    git add .

    leaf=$(curl -H "Accept: application/vnd.github.v3+json" https://api.github.com/repos/Winds-Studio/Leaf/compare/$current...ver/1.21.1 | jq -r '.commits[] | "Winds-Studio/Leaf@\(.sha[:7]) \(.commit.message | split("\r\n")[0] | split("\n")[0])"')

    updated=""
    logsuffix=""
    if [ ! -z "$leaf" ]; then
        logsuffix="$logsuffix\n\nLeaf Changes:\n$leaf"
        updated="Leaf"
    fi
    disclaimer="Upstream has released updates that appear to apply and compile correctly"

    log="Updated Upstream ($updated)\n\n${disclaimer}${logsuffix}"

    echo -e "$log" | git commit -F -
fi

) || exit 1