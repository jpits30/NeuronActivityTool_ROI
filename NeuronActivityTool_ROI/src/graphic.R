# script to graphic the signals when the mouse is clicked
indX = ceiling((coordX+1)/tamano)
indY =  ceiling((coordY+1)/tamano)
indYInv=col+1-indY
indice = (indY-1)*fil + (indX)

plot(signals[indice,],type="l",main=paste('Graph',indX,',',indYInv),xlab="frames", ylab="pixel intensity")
grid(10,10,lwd=2)
if(is.list(peak_list[[indice]])){
points(peak_list[[indice]][[1]],peak_list[[indice]][[2]],col="red",pch=18)
}
