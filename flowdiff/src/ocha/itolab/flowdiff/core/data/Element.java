package ocha.itolab.flowdiff.core.data;

import ocha.itolab.flowdiff.core.streamline.Streamline;
import ocha.itolab.flowdiff.core.streamline.StreamlineGenerator;

public class Element {
	public GridPoint gp[] = new GridPoint[8];
	
	// 自分とStreamlineの交差判定を行うメソッド
	boolean intersect(int myId, Streamline sl, int mode) {
		if (mode == 0){ // 「かなりむず」だったら
			return StreamlineGenerator.lastElementId() == myId;
		}
		else if (mode == 1) { // 「ちょいむず」だったら（初期設定）
			int elementLength = StreamlineGenerator.elementIds.size();
			int i;
			for (i = 0; i < elementLength; i++){
				if (myId == StreamlineGenerator.elementIds.get(i)) return true;
			}
			return false;
		}
		else return false;
				
	}

}
