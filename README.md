# NeuronActivityTool_ROI

A new version of the Neuron Activity Tool with a ROI analysis feature

Readme 								March 2018, vers I

**1	NA3 – Neural Activity cubic**

Computation of local and 'signal-close-to-noise' calcium activity

![Logo](https://github.com/jpits30/NeuronActivityTool/blob/master/Logo.png)

Calcium imaging has become a standard tool to investigate local, spontaneous, or cell-autonomous calcium signals in neurons. Some of these calcium signals are fast and ‘small’, thus making it difficult to identify real signaling events due to an unavoidable signal noise. Therefore, it is difficult to assess the spatiotemporal activity footprint of individual neurons or a neuronal network. We developed this open source tool to automatically extract, count, and localize calcium signals from the whole x,y-t image series. The tool is useful for an unbiased comparison of activity states of neurons, helps to assess local calcium transients, and even visualizes local homeostatic calcium activity. The calcium event computation is based on a continuous wavelet transform-guided peak detection to identify calcium signal candidates. The highly sensitive calcium event definition is based on identification of peaks in 1D data through analysis of a 2D wavelet transform surface. The tool is powerful enough to visualize signal-close-to-noise calcium activity, but can also be applied on other imaging data.

**2	Concept**

This bioinformatics tool facilitates the unbiased assessment of calcium signals from x,y-t imaging raw data. The tool can be adapted for use with diverse imaging data sets in which changes in bit-values show a transient-like character. 
This tool shifts the focus from calcium spike analysis of cell bodies to local ‘signal-close-to-noise’ analysis in any area of a neuron.
The idea is to use an unbiased x,y-grid, to include all pixels in an imaging video and to offer an activity distribution map. The tool avoids a preferential look at one activity mode of a neuron, e.g. the spiking behavior of neuronal somata, but instead tries to find every activity by detecting signals-close-to-noise, however small.

![Fig9](https://github.com/jpits30/NeuronActivityTool/blob/master/Figure9-mod.png)
Figure 1. This open source tool automatically extracts, counts, and localizes calcium signals in the whole x,y-t image series.

The tool is highly sensitive for local, specific activity and challenges the "all is just noise" hypothesis. Our tool accepts that a signal trace is never smooth. Pre-processing of raw data is not needed and all information from a grid window is included.

**3	Description of operation**

The tool bases its algorithm on a continuous wavelet transform-guided peak detection to identify calcium signal candidates. Intensity signals are automatically calculated in the whole x,y-field of the image series. For this, a grid with defined pixel size separates the x,y-field in independently analyzed grid windows. The tool can also be used to compute ROI information. 

Output data 

1)	A dataset is created and shows loci of activity, individual traces of active loci and calculates a number of activity events. 
2)	The tool can also compute the signal variance in a sliding window to visualize activity-dependent signal fluctuations.

**4	User Manual and Installation** 

A detailed USER MANUAL describes the installation procedure for Windows, Mac computers and Linux. 
Signal extraction is computed on ImageJ and activity events are calculated on ‘R’ (https://www.r-project.org). 
Both computations are embedded in the Bio7 environment, an open platform (http://bio7.org/). 
To date, the software has been tested on Windows with the BIO7 2.4 for Windows 64 bits.
The system was also proved on a Linux 3.11.10-7. openSUSE 13.1 (Bottle)(x86_64) and a Mac OS X Yosemite 10.10.5, and some other Mac systems.
The application was developed on a Windows X64 Intel core i-7 machine with 16 Gigabyte (GB) of RAM memory. For time series analysis, a Windows X64 Intel core i-5 machine with 4 Gigabyte of RAM memory is sufficient, but we recommend having more RAM (e.g. 8 – 16 GB).

**5	Test data and Raw data** 

Test data and raw data can be found at our institutional homepage. 
https://www.biozentrum.uni-wuerzburg.de/bioinfo/computing/neuralactivitycubic/

**6	Contributors**

Juan Prada1¶, Manju Sasi2¶, Corinna Martin, Sibylle Jablonka2, Thomas Dandekar1*, and Robert Blum2*
1 Department of Bioinformatics, University of Würzburg, Würzburg, Germany
2 Institute of Clinical Neurobiology, University Hospital Würzburg, Würzburg, Germany 
o	Conceptualization: Juan Prada, Thomas Dandekar, Robert Blum.
o	Software: Juan Prada, Thomas Dandekar
o	Imaging data: Manju Sasi, Corinna Martin, Robert Blum.
o	Methodology: Manju Sasi, Sibylle Jablonka, Robert Blum.
o	Supervision: Thomas Dandekar, Robert Blum.
o	Validation: Juan Prada, Manju Sasi, Corinna Martin, Thomas Dandekar, Robert Blum

**7	References**

Prada J, Sasi M, Martin C, Jablonka S, Dandekar T, Blum R (2018) An open source tool for automatic spatiotemporal assessment of calcium transients and local 'signal-close-to-noise' activity in calcium imaging data. PLoS computational biology 14:e1006054 

DOI: 10.1371/journal.pcbi.1006054

**9	Questions, comments, issues**

Please contact us at juan.prada_salcedo@uni-wuerzburg.de (concept, software issues, algorithm) or Blum_R@UKW.de (concept, usage, research questions),.

**10	License**

This program is free software; you can redistribute it and/or modify it.
This program is distributed in the hope that it helps to answer some of your research questions. We distribute this software without any warranty.

