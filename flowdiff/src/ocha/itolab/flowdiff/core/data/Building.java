package ocha.itolab.flowdiff.core.data;

public class Building {

	Grid grid = new Grid();

	void init(Grid grid){
		for(int i = 0; i< grid.getNumGridPointAll();i++){
			grid.getGridPoint(i).setBuildingLabel(0);
		}
	}

	public void labeling(Grid grid){
		int label1 = 1;
		int label = 2;
		int x = grid.getNumGridPoint()[0];
		int y = grid.getNumGridPoint()[1];
		int z = grid.getNumGridPoint()[2];

		//初期化（ラベルを０にする）
		this.init(grid);

		//ラベリング処理
		for(int i=0; i<grid.getNumGridPointAll(); i++){
			int num = i;
			if(grid.getGridPoint(i).getBuildingLabel()!=0){
				continue;
			}
			if(grid.getGridPoint(i).getEnvironment() == 0.0){
				//建物がない　かつ　探査を行った場所はラベル１
				grid.getGridPoint(i).setBuildingLabel(label1);
			}

			while(grid.getGridPoint(num).getEnvironment()!=0.0){
				//建物にラベルつける（２以上）
				grid.getGridPoint(num).setBuildingLabel(label);

				//次の頂点の探索
				if(num+1 < grid.getNumGridPointAll()){
					if(grid.getGridPoint(num+1).getBuildingLabel()!=0 && grid.getGridPoint(num+1).getBuildingLabel()!=1){
						grid.getGridPoint(num).setBuildingLabel(grid.getGridPoint(num+1).getBuildingLabel());
					}
					if(grid.getGridPoint(num+1).getBuildingLabel()==0 && grid.getGridPoint(num+1).getEnvironment()!=0.0){
						//進む 右
						num++;
						continue;
					}
				}
				if(num-1 > 0){
					if(grid.getGridPoint(num-1).getBuildingLabel()!=0 && grid.getGridPoint(num-1).getBuildingLabel()!=1){
						grid.getGridPoint(num).setBuildingLabel(grid.getGridPoint(num-1).getBuildingLabel());
					}
					if(grid.getGridPoint(num-1).getBuildingLabel()==0 && grid.getGridPoint(num-1).getEnvironment()!=0.0){
						//戻る　左
						num--;
						continue;
					}
				}
				if(num+x < grid.getNumGridPointAll()){
					if(grid.getGridPoint(num+x).getBuildingLabel()!=0 && grid.getGridPoint(num+x).getBuildingLabel()!=1){
						grid.getGridPoint(num).setBuildingLabel(grid.getGridPoint(num+x).getBuildingLabel());
					}
					if(grid.getGridPoint(num+x).getBuildingLabel()==0 && grid.getGridPoint(num+x).getEnvironment()!=0.0){
						//上
						num = num+x;
						continue;
					}
				}
				if(num-x >0){
					if(grid.getGridPoint(num-x).getBuildingLabel()!=0 && grid.getGridPoint(num-x).getBuildingLabel()!=1){
						grid.getGridPoint(num).setBuildingLabel(grid.getGridPoint(num-x).getBuildingLabel());
					}
					if(grid.getGridPoint(num-x).getBuildingLabel()==0 && grid.getGridPoint(num-x).getEnvironment()!=0.0){
						//下
						num = num-x;
						continue;
					}
				}
				if(num - x*y >0){
					if(grid.getGridPoint(num-x*y).getBuildingLabel()!=0 && grid.getGridPoint(num-x*y).getBuildingLabel()!=1){
						grid.getGridPoint(num).setBuildingLabel(grid.getGridPoint(num-x*y).getBuildingLabel());
					}
					if(grid.getGridPoint(num-x*y).getBuildingLabel()==0 && grid.getGridPoint(num-x*y).getEnvironment()!=0.0){
						//前
						num = num-x*y;
						continue;
					}
				}
				if(num+x*y < grid.getNumGridPointAll()){
					if(grid.getGridPoint(num+x*y).getBuildingLabel()!=0 && grid.getGridPoint(num+x*y).getBuildingLabel()!=1){
						grid.getGridPoint(num).setBuildingLabel(grid.getGridPoint(num+x*y).getBuildingLabel());
					}
					if(grid.getGridPoint(num+x*y).getBuildingLabel()==0 && grid.getGridPoint(num+x*y).getEnvironment()!=0.0){
						//後ろ
						num = num+x*y;
						continue;
					}
				}
				label++;
				break;
			}
		}
		System.out.println("label="+label);
		//return label;
	}
}
