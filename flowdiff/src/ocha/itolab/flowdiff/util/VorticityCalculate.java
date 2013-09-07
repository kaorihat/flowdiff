package ocha.itolab.flowdiff.util;

import ocha.itolab.flowdiff.core.data.Grid;
import ocha.itolab.flowdiff.core.data.GridPoint;

public class VorticityCalculate {
	public GridPoint gp[] = new GridPoint[8];
	public Vorticity vorticity[];
	
	public void calculatevorticity(Grid grid){
		vorticity = new Vorticity[grid.getNumElementAll()];
		for(int i = 0;i <grid.getNumElementAll();i++){
			vorticity[i] = new Vorticity();
			GridPoint gp[] = new GridPoint[4];
			gp[0]=grid.getElement(i).getElement(0);
			gp[1]=grid.getElement(i).getElement(1);
			gp[2]=grid.getElement(i).getElement(5);
			gp[3]=grid.getElement(i).getElement(4);
			
			double dx = Math.abs(gp[0].getPosition()[0] - gp[1].getPosition()[0]);
			double dz = Math.abs(gp[0].getPosition()[2] - gp[3].getPosition()[2]);
			
			//頂点を決める
			double x = (gp[0].getPosition()[0] + gp[1].getPosition()[0])/2;
			double y =  gp[0].getPosition()[1] ;
			double z = (gp[0].getPosition()[2] + gp[3].getPosition()[2])/2;
			vorticity[i].setPosition(x, y, z);
			
			//抽出した要素から渦度を計算する
			double xvdiff = (gp[1].getVector()[0] - gp[0].getVector()[0]) + (gp[2].getVector()[0] - gp[3].getVector()[0]);
			double zvdiff = (gp[0].getVector()[2] - gp[3].getVector()[2]) + (gp[2].getVector()[2] - gp[1].getVector()[2]);
			double rot = ((zvdiff)/dx - (xvdiff)/dz)/2;
			
			vorticity[i].setVorticity(rot);
		}
	}
	
	public void minmax(Grid grid){
		double min = 0;
		double max = 0;
		for(int i = 0;i <grid.getNumElementAll();i++){
			if(max < vorticity[i].getVorticity()){
				max = vorticity[i].getVorticity();
			}else if(min > vorticity[i].getVorticity()){
				min = vorticity[i].getVorticity();
			}
		}
		System.out.println(max+":"+min);
	}

}