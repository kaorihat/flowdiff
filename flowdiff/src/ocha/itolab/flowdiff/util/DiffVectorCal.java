package ocha.itolab.flowdiff.util;

import ocha.itolab.flowdiff.core.data.Grid;

public class DiffVectorCal {
	
	public DiffVector angle[];
	
	/**
	 * 二つのベクトル間の角度を算出するメソッド
	 * @param grid1
	 * @param grid2
	 */
	public void calDiffAngle(Grid grid1,Grid grid2){
		
		angle = new DiffVector[grid1.getNumGridPointAll()];
		
		//角度を求める
		for(int i=0; i<grid1.getNumGridPointAll(); i++){
			angle[i] = new DiffVector();
			double[] vec1 = new double[3];
			double[] vec2 = new double[3];
			double cos = 0.0;
			
			vec1[0] = grid1.getGridPoint(i).getVector()[0];
			vec1[1] = grid1.getGridPoint(i).getVector()[1];
			vec1[2] = grid1.getGridPoint(i).getVector()[2];
			vec2[0] = grid2.getGridPoint(i).getVector()[0];
			vec2[1] = grid2.getGridPoint(i).getVector()[1];
			vec2[2] = grid2.getGridPoint(i).getVector()[2];
			
			//内積から角度を算出
			cos = vec1[0]*vec2[0] + vec1[1]*vec2[1] +vec1[2]*vec2[2];
			angle[i].setDiff(Math.acos(cos));
			
		}
	}
	
}
