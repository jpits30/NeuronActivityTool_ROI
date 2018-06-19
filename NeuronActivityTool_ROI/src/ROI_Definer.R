list.of.packages <- c("TTR", "tiff","wmtsa","changepoint","cpm","pracma","MASS","IM","sp")
new.packages <- list.of.packages[!(list.of.packages %in% installed.packages()[,"Package"])]
if(length(new.packages)) install.packages(new.packages)

require(TTR)
require(IM)
require(tiff)
require(wmtsa)
require(changepoint)
require(cpm)
require(pracma)
require(sp)

tam=floor(tamano/2)
contCol=1
contRow=1


	ListArrayPoints = vector("list", length(roiXSelectionCoordinates))
	rowInd = 1:numRow
	colInd = 1:numCol
	mittelPunktRois = matrix(0,length(roiXSelectionCoordinates),2) 

	for(j in 1:length(rowInd)){
		for(l in 1:length(colInd)){
		
			for(m in 1:length(roiXSelectionCoordinates)){
			
				if(point.in.polygon(rowInd[j],colInd[l],roiXSelectionCoordinates[[m]],roiYSelectionCoordinates[[m]])){
		          ListArrayPoints[[m]] = rbind(ListArrayPoints[[m]],c(rowInd[j],colInd[l]))
				}
				
				mittelPunktRois[m,1] = ceiling(mean(ListArrayPoints[[m]][,1])/tamano)
				mittelPunktRois[m,2] = ceiling(mean(ListArrayPoints[[m]][,2])/tamano)
				
			}
		}
	}