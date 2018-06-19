# script to find the point of general tendency change. Used when the General tendecy dataction is checked.
criticalPoint <- function(signal, peaks, start){

  SF = length(signal)/50
  generalMean = mean(signal)
  firstMean = mean(signal[1:peaks[1]])
  lastMean = mean(signal[peaks[length(peaks)]:length(signal)])
  softSignal = SMA(signal,SF)
  baseLine = min(firstMean,lastMean)
  activeSignal = matrix(NA,1,length(signal))
  nonActiveSignal = matrix(NA,1,length(signal))
  activeSignal = which(signal >= baseLine, arr.ind=TRUE)
  meanDifErste = mean(signal[1:start])-baseLine
  meanDifLetzte = mean(signal[start:length(signal)])-baseLine
  
  if(meanDifErste <= meanDifLetzte)
  	activeSignal = activeSignal[activeSignal > start]
  else
  	activeSignal = activeSignal[activeSignal  < start] 

  criticalInfo=list(baseLine,softSignal,activeSignal)#activeSignal,nonActiveSignal)
  return(criticalInfo)
}