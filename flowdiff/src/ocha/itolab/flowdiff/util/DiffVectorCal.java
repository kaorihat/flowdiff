package ocha.itolab.flowdiff.util;

import ocha.itolab.flowdiff.core.data.Grid;
import ocha.itolab.flowdiff.core.data.GridPoint;

public class DiffVectorCal {

	public Grid grid;
	public GridPoint angle[];

	/**
	 * 二つのベクトル間の角度を算出するメソッド
	 * @param grid1
	 * @param grid2
	 */
	public void calDiffAngle(Grid grid1,Grid grid2){
		grid = grid1;
		angle = new GridPoint[grid1.getNumGridPointAll()];

		//角度を求める
		for(int i=0; i<grid1.getNumGridPointAll(); i++){
			angle[i] = grid.getGridPoint(i);
			if(grid1.getEnvironment(i) != grid2.getEnvironment(i)){
				//建物のあるなしが異なっている場合、外れ値をいれる
				angle[i].setDiff(200);
			}else{
				//角度の計算
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
				double innerProduct = vec1[0]*vec2[0] + vec1[1]*vec2[1] +vec1[2]*vec2[2];
				double dist1 = Math.sqrt(vec1[0]*vec1[0] + vec1[1]*vec1[1] + vec1[2]*vec1[2]);
				double dist2 = Math.sqrt(vec2[0]*vec2[0] + vec2[1]*vec2[1] + vec2[2]*vec2[2]);
				cos = innerProduct/(dist1*dist2);
				//System.out.println("cos="+cos);
				//System.out.println(Math.acos(cos));
				angle[i].setDiff(Math.acos(cos));
			}

		}
	}

}
