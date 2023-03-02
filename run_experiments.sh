#!/bin/bash
#
#   File:       run_experiments.sh
#   Author:     Matteo Loporchio
#

# Filter transactions
java ExtractById data/txs data/txs_filtered_ids.csv data/txs_filtered

# Extract all inputs and outputs
java ExtractInputs data/txs_filtered data/txs_filtered_inputs.csv
java ExtractOutputs data/txs_filtered data/txs_filtered_outputs.csv

# Classify according to their inputs and compute output statistics
java Classify data/txs_filtered data/txs_filtered_inclass.csv
java OutputStats data/txs_filtered data/txs_filtered_outclass.csv