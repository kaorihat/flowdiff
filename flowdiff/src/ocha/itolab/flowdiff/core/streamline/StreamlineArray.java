package ocha.itolab.flowdiff.core.streamline;

import java.util.ArrayList;

public class StreamlineArray {

	public static ArrayList<Streamline> streamlines1 = new ArrayList<Streamline>();
	public static ArrayList<Streamline> streamlines2 = new ArrayList<Streamline>();
	public static ArrayList<int[]> deperture = new ArrayList<int[]>();
	
	/**
	 * 流線リストのセット
	 * @param s1
	 */
	public static void addList1(Streamline s1){
		streamlines1.add(s1);
	}
	public static void addList2(Streamline s2){
		streamlines2.add(s2);
	}
	
	/**
	 * 流線リスト全体を返す
	 * @return 
	 */
	
	public static ArrayList<Streamline> getAllList1(){
		return streamlines1;
	}
	public static ArrayList<Streamline> getAllList2(){
		return streamlines2;
	}
	
	/**
	 * 流線リスト全体をクリアする
	 * @return 
	 */
	
	public static void  clearAllList1(){
		streamlines1.clear();
	}
	public static void clearAllList2(){
		streamlines2.clear();
	}
	
	/**
	 * 流線始点のリストのセット・返す・クリアする
	 */
	public static void addDeperture(int[] eIjk){
		deperture.add(eIjk);
	}
	public static ArrayList<int[]> getAllDeperture(){
		return deperture;
	}
	/**
	 * 流線始点のリストを全てクリアする
	 */
	public static void clearAllDeperture(){
		deperture.clear();
	}
}
