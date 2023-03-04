# Data availability

Due to space constraints, all data needed to execute the experiments is available in an <a href="https://doi.org/10.5281/zenodo.7696454">external repository</a>.

Before the execution of the experiments, it is necessary to download all files from the repository and place them in the current folder.

## Data description

Here is a summary of the files needed to execute all experiments.

<table>
	<tbody>
		<tr>
			<td>File</td>
			<td>Description</td>
		</tr>
		<tr>
			<td>txs</td>
			<td>A text file containing a representation of all Bitcoin transactions that create and consume dust. See the description below for more information about the structure of this file.</td>
		</tr>
		<tr>
			<td>txs_addr_map.csv</td>
			<td>A CSV file that maps numeric address identifiers to real Bitcoin addresses. This file comprises all addresses appearing in the txs data set.</td>
		</tr>
		<tr>
			<td>labels.csv</td>
			<td>A CSV file containing categorical entity labels for Bitcoin addresses appeared in transactions between 2010 and 2018. This file has been derived from the <em>Entity-Address data set</em> [1,2] (see also:&nbsp;<a href="https://github.com/Maru92/EntityAddressBitcoin">https://github.com/Maru92/EntityAddressBitcoin</a>).</td>
		</tr>
		<tr>
			<td>outputs_spent_stats.csv&nbsp;</td>
			<td>A CSV file containing statistics about all spent outputs in the first 479970 blocks of the Bitcoin blockchain. The file describes the durations of dust and non-dust outputs. The duration is defined as the difference between the height of the block where the output is spent and the height of the block where it was created.&nbsp;</td>
		</tr>
		<tr>
			<td>cluster_sizes_*.csv</td>
			<td>These CSV files contain information about clusters of addresses induced by Bitcoin transactions. They have been used for the clustering analysis.
		</tr>
	</tbody>
</table>
