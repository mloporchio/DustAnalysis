# DustAnalysis

## Description

This GitHub repository contains the code used to generate the results presented in [1].

The paper includes a comprehensive analysis of Bitcoin transactions involving _dust_, small amounts of currency that are lower than the fee required to spend them in a transaction. The work illustrates how dust is created and consumed and identifies the main creators and consumers. Moreover, it discusses how consumption has changed over time. Finally, it examines transactions that are suspected of being involved in dust attacks, and measures their impact on address deanonymization.

## Data availability

Due to space constraints, all data needed to execute the experiments is available in an <a href="https://doi.org/10.5281/zenodo.7696454">external repository</a>. Before the execution of the experiments, download all files from the external repository and place them in the <code>data</code> folder.

## Cite this work

If the code included in this repository or the associated data have been useful, please cite the following article in your work.

<pre>
@article{loporchio2023bitcoin, 
  title={Is Bitcoin gathering dust? An analysis of low-amount Bitcoin transactions},
  author={Loporchio, Matteo and Bernasconi, Anna and Di Francesco Maesa, Damiano and Ricci, Laura},
  journal={Applied Network Science},
  volume={8},
  number={1},
  pages={1--28},
  year={2023},
  publisher={SpringerOpen}
}
</pre>

## References

1. Loporchio, Matteo, et al. "Is Bitcoin gathering dust? An analysis of low-amount Bitcoin transactions." Applied Network Science 8.1 (2023): 1-28.
