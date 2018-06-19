### convolution operation if ROI analysis is selected
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

currentAD = matrix(0,ceiling(numRow/tamano)*tamano,ceiling(numCol/tamano)*tamano)
currentAD[1:numRow,1:numCol] = current

if(RoiAna == TRUE){

	for(n in 1:length(roiXSelectionCoordinates)){
		
		#DefArrayRoi[n,frame]=mean(currentAD[cbind(ListArrayPoints[[n]][,1],ListArrayPoints[[n]][,2])])
		defArray[mittelPunktRois[n,1],mittelPunktRois[n,2],frame] = mean(currentAD[cbind(ListArrayPoints[[n]][,1],ListArrayPoints[[n]][,2])])
		
	}

} else{
#	tam=floor(tamano/2)
#	contCol=1
#	contRow=1

#	currentAD = matrix(0,ceiling(numRow/tamano)*tamano,ceiling(numCol/tamano)*tamano)
#	currentAD[1:numRow,1:numCol] = current

	for (colsArray in seq(ceiling(tamano/2),numCol,tamano)) {
  		for (rowsArray in seq(ceiling(tamano/2),numRow,tamano)) {
    
    	defArray[contRow,contCol,frame] = mean(mean(currentAD[(rowsArray-tam):(rowsArray+tam),(colsArray-tam):(colsArray+tam)]))
    	contRow = contRow+1
  
  		}
  
  		contRow = 1  
  		contCol = contCol+1

	}
}
