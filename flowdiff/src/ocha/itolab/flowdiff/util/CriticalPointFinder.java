
package ocha.itolab.flowdiff.util;

import java.util.ArrayList;

import ocha.itolab.flowdiff.core.data.Grid;
import ocha.itolab.flowdiff.core.data.GridPoint;

public class CriticalPointFinder {
	
	ArrayList<CriticalPoint> cp = new ArrayList<CriticalPoint>();
	
	/**
	 * 速度が0となる特異点を検出する
	 * @return 
	 */
	public ArrayList<CriticalPoint> find(Grid grid) {
		
		double[][] tmp = new double[5][3]; 
		
		//全ての要素に対して
		for (int i = 0; i < grid.getNumElement(); i++) {
			
			GridPoint gp[] = new GridPoint[8];
			gp[0] = grid.getElement(i).gp[0];
			gp[1] = grid.getElement(i).gp[1];
			gp[2] = grid.getElement(i).gp[2];
			gp[3] = grid.getElement(i).gp[3];
			gp[4] = grid.getElement(i).gp[4];
			gp[5] = grid.getElement(i).gp[5];
			gp[6] = grid.getElement(i).gp[6];
			gp[7] = grid.getElement(i).gp[7];
			
			//0となる点を補完算出する
			tmp[0] = interpolate(gp[0],gp[4],gp[5],gp[6]);
			tmp[1] = interpolate(gp[0],gp[1],gp[3],gp[5]);
			tmp[2] = interpolate(gp[0],gp[3],gp[5],gp[6]);
			tmp[3] = interpolate(gp[0],gp[2],gp[3],gp[6]);
			tmp[4] = interpolate(gp[3],gp[5],gp[6],gp[7]);
			
			//リストに加える
			for (int j = 0; j < 5; j++) {
				if(tmp[j] != null){
					CriticalPoint e = new CriticalPoint();
					e.setPosition(tmp[j][0], tmp[j][1], tmp[j][2]);
					cp.add(e);
				}
			}
		}
		return cp;
	}
	
	/**
	 * データの補完を行う
	 */
	public double[] interpolate(GridPoint gp0,GridPoint gp1,GridPoint gp2,GridPoint gp3){
		double[] coe = new double[3];
		double[] ans = new double[3];
		//行列へ代入する
		double[] x =  new double[4];
		double[] y =  new double[4];
		double[] z =  new double[4];
		
		double[] u =  new double[4];
		double[] v =  new double[4];
		double[] w =  new double[4];
		//各頂点の座標
		x[0] = gp0.getPosition()[0];
		x[1] = gp1.getPosition()[0];
		x[2] = gp2.getPosition()[0];
		x[3] = gp3.getPosition()[0];
		y[0] = gp0.getPosition()[1];
		y[1] = gp1.getPosition()[1];
		y[2] = gp2.getPosition()[1];
		y[3] = gp3.getPosition()[1];
		z[0] = gp0.getPosition()[2];
		z[1] = gp1.getPosition()[2];
		z[2] = gp2.getPosition()[2];
		z[3] = gp3.getPosition()[2];
		//各頂点のベクトル
		u[0] = gp0.getVector()[0];
		u[1] = gp1.getVector()[0];
		u[2] = gp2.getVector()[0];
		u[3] = gp3.getVector()[0];
		v[0] = gp0.getVector()[1];
		v[1] = gp1.getVector()[1];
		v[2] = gp2.getVector()[1];
		v[3] = gp3.getVector()[1];
		w[0] = gp0.getVector()[2];
		w[1] = gp1.getVector()[2];
		w[2] = gp2.getVector()[2];
		w[3] = gp3.getVector()[2];
		
		//特異点があるかどうかを調べる
		coe = coefficient(u, v, w);
		
		if(coe != null){
			//特異点がある場合　座標を算出
			ans = critical_pos(x,y,z,coe);
		}
		else{
			//特異点なし
			ans = null;
		}
		
		return ans;
	}
	
	/**
	 * 特異点を補完算出するための係数を求める
	 * @return
	 */
	public double[] coefficient(double[] u,double[] v, double[] w){
		//行列の生成
		double[][] mat = new double[3][3];
		double[][] dmat = new double[3][3];//逆行列
		double[] cmat = new double[3];//係数の行列
		
		mat[0][0] = u[0] - u[3];
		mat[0][1] = u[1] - u[3];
		mat[0][2] = u[2] - u[3];
		mat[1][0] = v[0] - v[3];
		mat[1][1] = v[1] - v[3];
		mat[1][2] = v[2] - v[3];
		mat[2][0] = w[0] - w[3];
		mat[2][1] = w[1] - w[3];
		mat[2][2] = w[2] - w[3];
		
		//逆行列の算出
		dmat = determinant(mat);
		
		//係数の算出
		if(dmat != null){
			cmat[0] = -(dmat[0][0]*u[3] + dmat[0][1]*v[3] + dmat[0][2]*w[3]);
			cmat[1] = -(dmat[1][0]*u[3] + dmat[1][1]*v[3] + dmat[1][2]*w[3]);
			cmat[2] = -(dmat[2][0]*u[3] + dmat[2][1]*v[3] + dmat[2][2]*w[3]);
		}
		else{
			cmat = null;
		}
		return cmat;
	}
	
	/**
	 * 特異点の解（位置）を求める　逆行列を算出しない場合
	 * @param u
	 * @param v
	 * @param w
	 * @param c
	 * @return
	 */
	public double[] critical_pos(double[] x,double[] y, double[] z, double[] c){
		//行列の生成
		double[][] mat = new double[3][3];
		double[] amat = new double[3];//係数の行列
		
		mat[0][0] = x[0] - x[3];
		mat[0][1] = x[1] - x[3];
		mat[0][2] = x[2] - x[3];
		mat[1][0] = y[0] - y[3];
		mat[1][1] = y[1] - y[3];
		mat[1][2] = y[2] - y[3];
		mat[2][0] = z[0] - z[3];
		mat[2][1] = z[1] - z[3];
		mat[2][2] = z[2] - z[3];
		
		amat[0] = mat[0][0]*c[0] + mat[0][1]*c[1] + mat[0][2]*c[2] + x[3];
		amat[1] = mat[1][0]*c[0] + mat[1][1]*c[1] + mat[1][2]*c[2] + y[3];
		amat[2] = mat[2][0]*c[0] + mat[2][1]*c[1] + mat[2][2]*c[2] + z[3];
		
		
		return amat;
	}
	
	/**
	 * 逆行列の算出を行う(逆行列がない場合はnullを返す)
	 * @param m
	 * @return
	 */
	public double[][] determinant(double[][] m){
		double[][] dmat = new double[3][3];
		//サラスの公式
		double det = m[0][0]*m[1][1]*m[2][2] + m[0][1]*m[1][2]*m[2][0] + m[0][2]*m[1][0]*m[2][1] - m[0][2]*m[1][1]*m[2][0] - m[0][0]*m[1][2]*m[2][1] - m[0][1]*m[1][0]*m[2][2];
		
		if(det == 0){
			//逆行列が存在しない場合
			dmat = null;
		}
		else{
			//逆行列の計算
			dmat[0][0] = (m[1][1]*m[2][2]-m[1][2]*m[2][1])/det;
			dmat[0][1] = (m[0][2]*m[2][1]-m[0][1]*m[2][2])/det;
			dmat[0][2] = (m[0][1]*m[1][2]-m[0][2]*m[1][1])/det;
			dmat[1][0] = (m[1][2]*m[2][0]-m[1][0]*m[2][2])/det;
			dmat[1][1] = (m[0][0]*m[2][2]-m[0][2]*m[2][0])/det;
			dmat[1][2] = (m[0][2]*m[1][0]-m[0][0]*m[1][2])/det;
			dmat[2][0] = (m[1][0]*m[2][1]-m[1][1]*m[2][0])/det;
			dmat[2][1] = (m[0][1]*m[2][0]-m[0][0]*m[2][1])/det;
			dmat[2][2] = (m[0][0]*m[1][1]-m[0][1]*m[1][0])/det;
		}
		return dmat;
	}
}
