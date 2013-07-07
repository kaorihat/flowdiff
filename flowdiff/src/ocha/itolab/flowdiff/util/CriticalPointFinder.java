
package ocha.itolab.flowdiff.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Vector;

public class CriticalPointFinder {
	static BufferedWriter writer = null;
	
	static Vector seglist1 = new Vector();
	static Vector seglist2 = new Vector();
	
	public static int CRITICALPOINT_EFD = 1;
	public static int CRITICALPOINT_CFD = 2;
	
	static double critical_y = 0.0;
	static double critical_z = 0.0;
	
	
	/**
	 * 速度が0となる特異点を検出してファイル出力する
	 */
/*	public static void find(Mesh mesh, int type) {

		seglist1.clear();
		seglist2.clear();
		
		// ファイルを開く
		if(type == CRITICALPOINT_EFD)
			open(new File("../Newdata/Criticalpoint/critical_point_efd_test.dat"));
			//open(new File("../data/critical_point_efd_test.dat"));
		if(type == CRITICALPOINT_CFD)
			open(new File("../Newdata/Criticalpoint/critical_point_cfd_test.dat"));
		writeLine("VARIABLES = \"X\", \"Y\", \"Z\"");
		
			  // 各要素について
			  for(int i = 1; i <= mesh.getNumElements(); i++) {
				  Element e = mesh.getElement(i);
					  // 要素の頂点を抽出
				  int numv = e.getNumVertex();
				  Vertex vertex[] = e.getVertex();
				  //System.out.println("numv="+numv);
			
				  //	double u = 0.0;
				  double v = 0.0;
				  double w = 0.0;
				  double y0 = vertex[0].getPosition()[1];
				  double y1 = vertex[1].getPosition()[1];
				  double y2 = vertex[2].getPosition()[1];
					  
				  double z0 = vertex[0].getPosition()[2];
				  double z1 = vertex[1].getPosition()[2];
				  double z2 = vertex[2].getPosition()[2];
				  
				  double v0 = vertex[0].getVector1()[1];
				  double v1 = vertex[1].getVector1()[1];
				  double v2 = vertex[2].getVector1()[1];
			
				  double w0 = vertex[0].getVector1()[2];
				  double w1 = vertex[1].getVector1()[2];
				  double w2 = vertex[2].getVector1()[2];
				  
				  double check1 = (v0-v2)*(w1-w2)-(v1-v2)*(w0-w2); //逆行列を持つかの判定値
				  double check2 = (y0-y2)*(z1-z2)-(y1-y2)*(z0-z2);
					  
				  double Jv[] = new double[4];//ヤコビアン
				  //	boolean boundary = true;
			
				  //抽出した要素から行列を生成する
				  if(check1!=0&&(v0!=-999.0 || v1!=-999.0 || v2!=-999.0)){
					  v = ((v1-v2)*w2+(w2-w1)*v2)/check1;
					  w = ((w0-w2)*v2+(v2-v0)*w2)/check1;
						  
					  if(v >= 0 && w >= 0 && 1-v-w >= 0){
						  critical_y = (y0 - y2)*v + (y1 - y2)*w + y2 ;
						  critical_z = (z0 - z2)*v + (z1 - z2)*w + z2 ;
					  
						  //渦中心判定
						  if(check2 !=0){//逆行列をもつ場合
							  Jv[0]=((z1-z2)*(v0-v2)+(z2-z0)*(v1-v2))/check2;
							  Jv[1]=((z1-z2)*(w0-w2)+(z2-z0)*(w1-w2))/check2;
							  Jv[2]=((y2-y1)*(v0-v2)+(y0-y2)*(v1-v2))/check2;
							  Jv[3]=((y2-y1)*(w0-w2)+(y0-y2)*(w1-w2))/check2;
							  if(evaluate_eigenvalue(Jv)==true){
								  if(type == CRITICALPOINT_CFD){
									  println(vertex[0].getPosition()[0]+"  "+critical_y+"  "+critical_z);
								  }else{
									  println(vertex[0].getPosition()[0]+"  "+critical_y+"  "+critical_z);
								  }
							  }
						  }
					  }
				  }
				  if(numv==3)continue;
				  double y3 = vertex[3].getPosition()[1];
				  double z3 = vertex[3].getPosition()[2];
				  double v3 = vertex[3].getVector1()[1];
				  double w3 = vertex[3].getVector1()[2];
				  
				  double check3 =(v0-v3)*(w2-w3)-(v2-v3)*(w0-w3);
				  double check4 = (y0-y3)*(z2-z3)-(y2-y3)*(z0-z3);
				  
				  if(v0==-999.0 || v2==-999.0 || v3==-999.0)continue;
				  if(check3!=0){	 
					  v = ((v2-v3)*w3-(w2-w3)*v3)/check3;
					  w = ((w0-w3)*v3-(v0-v3)*w3)/check3;
						  
					  if(v >= 0 && w >= 0 && 1-v-w >= 0){
						  critical_y = (y0 - y3)*v + (y2 - y3)*w + y3 ;
						  critical_z = (z0 - z3)*v + (z2 - z3)*w + z3 ;
					  
						  
						  //渦中心判定
						  if(check4 !=0){//逆行列をもつ場合
							  Jv[0]=((z2-z3)*(v0-v3)+(z3-z0)*(v2-v3))/check4;
							  Jv[1]=((z2-z3)*(w0-w3)+(z3-z0)*(w2-w3))/check4;
							  Jv[2]=((y3-y2)*(v0-v3)+(y0-y3)*(v2-v3))/check4;
							  Jv[3]=((y3-y2)*(w0-w3)+(y0-y3)*(w2-w3))/check4;
							  if(evaluate_eigenvalue(Jv)==true){
								  if(type == CRITICALPOINT_CFD){
									  println(vertex[0].getPosition()[0]+"  "+critical_y+"  "+critical_z);
								  }
								  else{
									  println(vertex[0].getPosition()[0]+"  "+critical_y+"  "+critical_z);
								  }
							  }
						  }
					  }
				  }
			  }
		// ファイルを閉じる
		close();
	}
	
	/**/
	/*固有値を求め、渦中心かどうか判定*/
	static boolean evaluate_eigenvalue(double[] det){
		double a,b,c;
		boolean check = false;//渦中心かどうかの判定値
		a = 1;
		b = (-1.0)*det[0]-det[3];
		c = det[0]*det[3]-det[1]*det[2];//解の公式を解くための値！
		double answer1,answer2;
		
	/*	if(b*b-4.0*a*c == 0){
			answer1 = (-1.0)*b/2.0*a ;
			answer2 = answer1;
		}*/
		if(b*b-4.0*a*c > 0){
			answer1 = ((-1.0)*b + Math.sqrt(b*b-4.0*a*c))/(2.0*a);
			answer2 = ((-1.0)*b - Math.sqrt(b*b-4.0*a*c))/(2.0*a);
			if(answer1 <0 && answer2 <0){
				check = true;
			}
		}
		else if(b*b-4.0*a*c != 0 && (-1.0)*b/(2.0*a) != 0){
				check = true ;
		}
		else{
			check = false;
		}
		return check;
	}
	
	/**
	 * ファイルを開く
	 */
	static BufferedWriter open(File file) {	
		try {
			 writer = new BufferedWriter(
			    		new FileWriter(file));
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
		return writer;
	}
	
	/**
	 * ファイルを閉じる
	 */
	static void close() {
		
		try {
			writer.close();
		} catch (Exception e) {
			System.err.println(e);
			return;
		}
	}
	
	/**
	 * 1行を書き込む
	 */
	static void writeLine(String word) {
		try {
			writer.write(word, 0, word.length());
			writer.flush();
			writer.newLine();
		} catch (Exception e) {
			System.err.println(e);
			return;
		}
	}
	/**
	 * 1行をファイルに書き込む
	 */
	static void println(String word) {
		try {
			writer.write(word, 0, word.length());
			writer.flush();
			writer.newLine();
		} catch (Exception e) {
			System.err.println(e);
			return;
		}
	}	
	
}
