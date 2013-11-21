package ocha.itolab.flowdiff.applet.flowdiff;




public class Settings {

	//********変更パラメータここだけいじればおｋ*************
	//<ファイル関連>
	public static int preset = 100; //ここでどのファイルを読むかをきめる　風向80~110

	//<処理関連>
	public static int partial = 2; //間引き 間引かないときは１
	public static double vort_threshold = 2.0;

	//<描写関連>
	public static double[] ground_color = {0.2,0.2,0.2}; //地面の色変更
	public static float vector_border = 1.0f; //ベクトルの太さ
	public static boolean building_exits = true; //建物の有無（あとでラベリングする位置を変えて高速化しよう）

	//<GUI関連>

	//********************************************

	//いじっちゃだめよ Drawerのdefault_angleに入る
	public static double angle = preset/180.0*Math.PI;

	//風向80度
	static String[] url80 = {"file:../data/kassoro/nashi80/","file:../data/kassoro/ari80/"};
	//風向90度
	static String[] url90 = {"file:../data/kassoro/nashi90/","file:../data/kassoro/ari90/"};
	//風向100度
	static String[] url100 = {"file:../data/kassoro/nashi100/","file:../data/kassoro/ari100/"};
	//風向110度
	static String[] url110 = {"file:../data/kassoro/nashi/","file:../data/kassoro/ari/"};

	//presetに応じたファイル名を返す静的メソッド
	//引数　０：建物なし　１：建物あり
	public static String filename(int i){
		switch(preset){
		case 80:
			return url80[i];
		case 90:
			return url90[i];
		case 100:
			return url100[i];
		case 110:
			return url110[i];
		}
		System.out.println("preset file error");
		return "";
	}
}
