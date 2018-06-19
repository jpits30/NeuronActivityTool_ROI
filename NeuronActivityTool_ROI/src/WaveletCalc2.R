#See: http://stackoverflow.com/questions/4090169/elegant-way-to-check-for-missing-packages-and-install-them
list.of.packages <- c("TTR", "tiff","wmtsa","changepoint","cpm","pracma","MASS","IM","sp","graphics")
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
require(graphics)
require(MASS)

directory = paste(getwd(),"/workspace/",sep ="") #### Use this line for Windows and Linux ####
#### comment previous line and uncomment next line for Mac ####
# directory = "/Applications/Bio7.app/Contents/MacOS/workspace/" #### use this line for Mac ####

source(paste(directory,"NeuronActivityTool_ROI/src/criticalPoint.R",sep =""))


col=ceiling(numCol/tamano)
fil=ceiling(numRow/tamano)

indi=t(matrix(0,col,fil))
peak_list=list(list())
wavtrans_list=list(list())
wavtree_list=list(list())
signals=matrix(0,fil*col,length(defArray[1,1,]))

	for (j in seq(1,length(defArray[,,1]),1)) {
  		
  		r = ((j-1) %% nrow(defArray)) + 1
  		c = floor((j-1) / nrow(defArray)) + 1
  		series=defArray[r,c,] 
  		signals[j,]=series
  		peak_list[[j]]=0

   		if(mean(series)>MNT){
  			
  			wav_trans = wavCWT(series)
  			wavtrans_list[[j]]=wav_trans
  			er1 <- tryCatch(wavCWTTree(wav_trans, n.octave.min=1, tolerance=0.0, type="maxima"),error=function(e) e)
  
  			if(inherits(er1, "error")){
    			wavtree_list[[j]]=0
    		next}
  
 			wav_tree = wavCWTTree(wav_trans, n.octave.min=1, tolerance=0.0, type="maxima") 
 			wavtree_list[[j]]=wav_tree
 			er2 <- tryCatch(wavCWTPeaks(wav_tree, snr.min=SNR, scale.range=NULL, length.min=5, noise.span=NULL, noise.fun="quantile", noise.min=NULL),error=function(e2) e2)
  
 			if(!inherits(er2, "error")){
  
    			peaks = wavCWTPeaks(wav_tree, snr.min=SNR, scale.range=NULL, length.min=5, noise.span=NULL, noise.fun="quantile", noise.min=NULL)
    			    			
    			indi[j]=length(peaks[[1]])
    			peak_list[[j]]=peaks
 
    		}
   		}
	}
	
	indi[which(indi<minPeaks)]=0

##   This part does the ploting
	if (length(peak_list)!=0){

		indiBio=indi
		indi=indi[,ncol(indi):1]
		img=current
		puntos=which(indi>=1,arr.ind=FALSE)
		puntosBio=which(indiBio>=1,arr.ind=TRUE)
		img = img*255/max(img)
		graphics.off()
		filePdf<-paste(directory,"NeuronActivityTool_ROI/results/result","WS",toString(tamano),"SNR",toString(SNR),"SAT",toString(MNT),"MAC",toString(minPeaks),".pdf",sep ="")
		pdf(filePdf,height=ceiling(ncol(img)*(7/max(ncol(img),nrow(img)))),width=ceiling(nrow(img)*(7/max(ncol(img),nrow(img)))));
		image(img[,ncol(img):1],axes = FALSE, col = grey(seq(0, 1, length = 256)),main=paste('Total activity',sum(indi[indi>=1])))
		abline(h=seq(0,1,1/col), v=seq(0,1,1/fil), col="gray", lty=3)
		axis(2,pos=0, at=seq(0.5/col,1,1/col),labels=1:col)
		axis(1,pos=0, at=seq(0.5/fil,1,1/fil),labels=1:fil)
		par(new=TRUE)
		markCol=seq(1/col/2,1-1/col/2,1/col)
		markRow=seq(1/fil/2,1-1/fil/2,1/fil)
		markers=expand.grid(markRow,markCol)
		plot(markers[puntos,1],markers[puntos,2],pch=1,cex=indi[puntos]*(1/max(indi)),xaxs="i",yaxs="i",col="red",xlab = NA,ylab = NA,axes=FALSE,xlim=c(0,1),ylim=c(0,1))    
		par(mfrow=c(3,1))
		cont_g=0
		Matrix_List <- vector("list",length(puntos))
		MatrixStrings = paste("Coordinates ; Peaks X-coordinates ;  Peaks Y-coordinates")
		row_Matrix_List=1
		
		for(var in 1:length(peak_list)){
 
			if(peak_list[[var]][[1]]!=0){
  
  				if(length(peak_list[[var]][[1]])>=minPeaks) {
      
        			r = ((var-1) %% nrow(defArray)) + 1
    				c = floor((var-1) / nrow(defArray)) + 1
    				cInv = col+1-c
    				
    				if(cont_g==3){
    				    par(mfrow=c(3,1))
    					cont_g=0
    				}
    				
    				limGau = cpt.meanvar(signals[var,],penalty="Manual",pen.value=0.01,method="AMOC",class=TRUE)
    				infoCritical = criticalPoint(signals[var,],peak_list[[var]][[1]],limGau@cpts[1]) 
    				par(mar=c(2,3,2,2)+0.1)
    				plot(signals[var,],type="l",main=paste('Graph',r,',',cInv,'    ', 'Total Activity', length(peak_list[[var]][[1]])),xlab = NA,ylab = NA)
    				#plot(signals[var,],type="l",main=paste('Graph',r,',',cInv,'    ', 'Total Activity', length(peak_list[[var]][[1]]),'Signal Position ',var),xlab="frames", ylab="pixel intensity")
    				points(peak_list[[var]][[1]],peak_list[[var]][[2]],col="red",pch=18)
    				filaNueva=list(c(r,cInv),peak_list[[var]][[1]],signals[var,])
    				Matrix_List[[row_Matrix_List]]=filaNueva
    				
    				MatrixStrings = c(MatrixStrings,paste("{",r,",",cInv,"}",";","[",paste(peak_list[[var]][[1]],collapse=","),"]",";","[",paste(peak_list[[var]][[2]],collapse=","),"]",sep="")) 
    				row_Matrix_List=row_Matrix_List+1
    
    				if(criticalEnable){
    					
    					vecPolCorners1 = c(infoCritical[[3]],rev(infoCritical[[3]]))
    					vecPolCorners2 = c(signals[var,infoCritical[[3]]],matrix(infoCritical[[1]],1,length(infoCritical[[3]])))
    					abline(h=infoCritical[[1]])
    					areaUnCurv = polyarea(vecPolCorners1,vecPolCorners2)
    					polygon(vecPolCorners1, vecPolCorners2, border=NA, col = rgb(0/255,255/255,0/255,0.8)) 
    					lines(signals[var,])
    					abline(v=limGau@cpts[1])
    					title(paste('Area under the curve',round(abs(areaUnCurv))),adj=0)
    					
    				}
    				
    				if(VarianceEnable){
    					
    					meanSig = rollapply(signals[var,], WinSize, mean, fill = "extend", align = "right")
						varSig = rollapply(signals[var,], WinSize, var, fill = "extend", align = "right")
						varSig1  = varSig+meanSig
						varSig2 = -varSig+meanSig
    					areaSig = polyarea(c(1:length(varSig1),length(varSig1):1),c(varSig1,varSig2[length(varSig2):1]))
    					polygon(c(1:length(varSig1),length(varSig1):1),c(varSig1,varSig2[length(varSig2):1]),col=rgb(1,1,0,alpha=0.5),border=FALSE)
    					title(paste('Variance Area',round(abs(areaSig))),adj=1)
    				}
    
    				cont_g=cont_g+1
    			
    			}
  			}
		}
	dev.off()
	
	fileTxtPun<-paste(directory,"NeuronActivityTool_ROI/results/result","WS",toString(tamano),"SNR",toString(SNR),"SAT",toString(MNT),"MAC",toString(minPeaks),".txt",sep ="")
	write.matrix(t(indiBio),fileTxtPun)
	save(Matrix_List, file=paste(directory,"NeuronActivityTool_ROI/results/Matrix_List.RData",sep =""))
    fileTxtStr<-paste(directory,"NeuronActivityTool_ROI/results/Peaks","WS",toString(tamano),"SNR",toString(SNR),"SAT",toString(MNT),"MAC",toString(minPeaks),".txt",sep ="")
    write.matrix(MatrixStrings,fileTxtStr)

	}	
#}	