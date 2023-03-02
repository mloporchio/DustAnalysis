#
#   File:   spent_stats.py
#   Author: Matteo Loporchio
#
#   This Python script processes the output of the OutputConsumption Java class
#   by computing aggregate statistics for all spent outputs.
#   The output of this script is a CSV file where each row corresponds to a duration value. 
#   For each duration value D, we compute:
#
#   1) The number of dust outputs spent after D blocks.
#   2) The number of non-dust outputs spent after D blocks.
#   3) The percentage of dust outputs over the total number of outputs spent after D blocks.
#   4) The percentage of non-dust outputs over the total number of outputs spent after D blocks.
#

import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from scipy import stats
import sys

inputFile = sys.argv[1]
outputFile = sys.argv[2]

# Load dataframe from the input file.
spentOutputs = pd.read_csv(inputFile)

# Separate dust and non-dust outputs.
spentOutputsDust = spentOutputs[(spentOutputs.amount >= 1) & (spentOutputs.amount <= 545)]
spentOutputsNonDust = spentOutputs[spentOutputs.amount > 545]

# Print the average number of blocks for each category.
print('Avg. number of blocks before expenditure')
print('Dust:\t\t{}\nNon dust:\t{}'.format(spentOutputsDust['duration'].mean(), spentOutputsNonDust['duration'].mean()))

# Build the statistics CSV.
d1 = spentOutputsDust.groupby('duration').size()
d2 = spentOutputsNonDust.groupby('duration').size()
index = range(0, max(d1.index[-1], d2.index[-1])+1)
d1 = d1.reindex(index).fillna(0).astype(int)
d2 = d2.reindex(index).fillna(0).astype(int)
ds = d1 + d2
d1p = d1 / ds
d2p = d2 / ds
df = pd.DataFrame({'dust':d1,'nonDust':d2,'dustPerc':d1p,'nonDustPerc':d2p}).fillna(0).reset_index()
df.to_csv(outputFile, index=False)