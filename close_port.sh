#!/bin/bash

# Fermer les ports en tuant les processus qui les utilisent
# Remplacez 5000 par le port que vous souhaitez fermer
PORT=5000
TEST_PORT=6000

# Trouver les PIDs des processus utilisant les ports
PIDS=$(lsof -t -i :$PORT)
TEST_PIDS=$(lsof -t -i :$TEST_PORT)

# Combiner les PIDs en une seule liste
ALL_PIDS=$(echo "$PIDS $TEST_PIDS" | tr ' ' '\n' | sort -u)

# Tuer les processus
if [ ! -z "$ALL_PIDS" ]; then
    kill -9 $ALL_PIDS
    echo "Closed ports $PORT and $TEST_PORT"
else
    echo "Ports $PORT and $TEST_PORT are not in use"
fi

