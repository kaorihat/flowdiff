
package ocha.itolab.flowdiff.applet.flowdiff;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ocha.itolab.flowdiff.core.data.FileReader;
import ocha.itolab.flowdiff.core.data.Grid;
import ocha.itolab.flowdiff.core.streamline.Streamline;
import ocha.itolab.flowdiff.core.streamline.StreamlineGenerator;


public class ViewingPanel extends JPanel {

	// ベクタ場のファイルを読み込む（相対パス）
	static String url1 = "file:../data/kassoro/ari/";
	static String url2 = "file:../data/kassoro/nashi/";

	
	public JButton  openDataButton, viewResetButton, generateButton, viewVectorButton, viewCriticalPoint, viewVorticity;
	public JRadioButton viewRotateButton, viewScaleButton, viewShiftButton, grid1View, grid2View, bothGridView;
	public JLabel xText, yText, zText, vtext, vhText,vecviewText;
	public JSlider sliderX, sliderY, sliderZ,sliderVH,vheight;
	public Container container;
	File currentDirectory;

	/* Selective canvas */
	Canvas canvas;
	
	/* Cursor Sensor */
	boolean cursorSensorFlag = false;

	/* Action listener */
	ButtonListener bl = null;
	RadioButtonListener rbl = null;
	CheckBoxListener cbl = null;
	SliderListener sl = null;

	/* Data */
	Grid grid1 = null;
	Grid grid2 = null;
	
	/*button toggle flag*/
	boolean viewVorticity_flag = false;//trueの時オン
	
	public ViewingPanel() {
		// super class init
		super();
		setSize(200, 800);

		JTabbedPane tabbedpane = new JTabbedPane();
		// パネル1
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(11,1));
		openDataButton = new JButton("ファイル読込");
		viewResetButton = new JButton("元に戻す");
		p1.add(openDataButton);
		p1.add(viewResetButton);
		ButtonGroup group1 = new ButtonGroup();
		viewRotateButton = new JRadioButton("回転",true);//最初にチェックが入っている
		group1.add(viewRotateButton);
		p1.add(viewRotateButton);
		viewScaleButton = new JRadioButton("拡大・縮小");
		group1.add(viewScaleButton);
		p1.add(viewScaleButton);
		viewShiftButton = new JRadioButton("移動");
		group1.add(viewShiftButton);
		p1.add(viewShiftButton);
		
		vecviewText = new JLabel(" ベクトル表示");
		p1.add(vecviewText);
		ButtonGroup group2 = new ButtonGroup();
		bothGridView = new JRadioButton("両方",true);//最初にチェックが入っている
		group2.add(bothGridView);
		p1.add(bothGridView);
		grid1View = new JRadioButton("grid1");//最初にチェックが入っている
		group2.add(grid1View);
		p1.add(grid1View);
		grid2View = new JRadioButton("grid2");//最初にチェックが入っている
		group2.add(grid2View);
		p1.add(grid2View);
		
		vheight = new JSlider(0, 85, 10);
		vtext = new JLabel(" ベクトル面地上から: " + vheight.getValue());
		vheight.setMajorTickSpacing(10);
		vheight.setMinorTickSpacing(5);
		vheight.setPaintTicks(true);
		vheight.setLabelTable(vheight.createStandardLabels(20));
		vheight.setPaintLabels(true);
		p1.add(vheight);
		p1.add(vtext);
		
		// パネル2
		JPanel p2 = new JPanel();
		p2.setLayout(new GridLayout(14,1));
		sliderX = new JSlider(0, 100, 10);
		sliderX.setMajorTickSpacing(10);
		sliderX.setMinorTickSpacing(5);
		sliderX.setPaintTicks(true);
		sliderX.setLabelTable(sliderX.createStandardLabels(20));
	    sliderX.setPaintLabels(true);
	    xText = new JLabel(" よこ: " + sliderX.getValue());
		p2.add(sliderX);
		p2.add(xText);
		sliderY = new JSlider(0, 100, 10);
		sliderY.setMajorTickSpacing(10);
		sliderY.setMinorTickSpacing(5);
		sliderY.setPaintTicks(true);
		sliderY.setLabelTable(sliderY.createStandardLabels(20));
	    sliderY.setPaintLabels(true);
	    yText = new JLabel(" たて: " + sliderY.getValue());
		p2.add(sliderY);
		p2.add(yText);
		sliderZ = new JSlider(0, 100, 10);
		sliderZ.setMajorTickSpacing(10);
		sliderZ.setMinorTickSpacing(5);
		sliderZ.setPaintTicks(true);
		sliderZ.setLabelTable(sliderZ.createStandardLabels(20));
	    sliderZ.setPaintLabels(true);
	    zText = new JLabel(" たかさ: " + sliderZ.getValue());
		p2.add(sliderZ);
		p2.add(zText);
		generateButton = new JButton("流線決定");
		p2.add(generateButton);
		viewCriticalPoint = new JButton("渦中心表示");
		p2.add(viewCriticalPoint);
		viewVorticity = new JButton("渦度表示");
		p2.add(viewVorticity);
		sliderVH = new JSlider(0, 85, 10);
		vhText = new JLabel(" 高さ(渦度): " + sliderVH.getValue());
		sliderVH.setMajorTickSpacing(10);
		sliderVH.setMinorTickSpacing(5);
		sliderVH.setPaintTicks(true);
		sliderVH.setLabelTable(sliderVH.createStandardLabels(20));
		sliderVH.setPaintLabels(true);
		p2.add(sliderVH);
		p2.add(vhText);
		
		ButtonGroup group3 = new ButtonGroup();
		bothGridView = new JRadioButton("両方",true);//最初にチェックが入っている
		group3.add(bothGridView);
		p2.add(bothGridView);
		grid1View = new JRadioButton("grid1");
		group3.add(grid1View);
		p2.add(grid1View);
		grid2View = new JRadioButton("grid2");
		group3.add(grid2View);
		p2.add(grid2View);
		//
		// パネル群のレイアウト
		//
		tabbedpane.addTab("tab1", p1);
		tabbedpane.addTab("tab2", p2);
		this.add(tabbedpane);
		
		//
		// リスナーの追加
		//
		if (bl == null)
			bl = new ButtonListener();
		addButtonListener(bl);

		if (rbl == null)
			rbl = new RadioButtonListener();
		addRadioButtonListener(rbl);

		if (cbl == null)
			cbl = new CheckBoxListener();
		addCheckBoxListener(cbl);

		if (sl == null)
			sl = new SliderListener();
		addSliderListener(sl);
	}

	/**
	 * Canvasをセットする
	 * @param c Canvas
	 */
	public void setCanvas(Object c) {
		canvas = (Canvas) c;
	}


	/**
	 * Cursor Sensor の ON/OFF を指定するフラグを返す
	 * @return cursorSensorFlag
	 */
	public boolean getCursorSensorFlag() {
		return cursorSensorFlag;
	}


	/**
	 * ラジオボタンのアクションの検出を設定する
	 * @param actionListener ActionListener
	 */
	public void addRadioButtonListener(ActionListener actionListener) {
		viewRotateButton.addActionListener(actionListener);
		viewScaleButton.addActionListener(actionListener);
		viewShiftButton.addActionListener(actionListener);
		bothGridView.addActionListener(actionListener);
		grid1View.addActionListener(actionListener);
		grid2View.addActionListener(actionListener);
	}

	/**
	 * ボタンのアクションの検出を設定する
	 * @param actionListener ActionListener
	 */
	public void addButtonListener(ActionListener actionListener) {
		openDataButton.addActionListener(actionListener);
		viewResetButton.addActionListener(actionListener);
		generateButton.addActionListener(actionListener);
		viewCriticalPoint.addActionListener(actionListener);
		viewVorticity.addActionListener(actionListener);
	}

	/**
	 * チェックボックスのアクションの検出を設定する
	 * @param actionListener ActionListener
	 */
	public void addCheckBoxListener(CheckBoxListener checkBoxListener) {
	}

	/**
	 * スライダのアクションの検出を設定する
	 * @param actionListener ActionListener
	 */
	public void addSliderListener(ChangeListener changeListener) {
		vheight.addChangeListener(changeListener);
		sliderX.addChangeListener(changeListener);
		sliderY.addChangeListener(changeListener);
		sliderZ.addChangeListener(changeListener);
		sliderVH.addChangeListener(changeListener);
	}
	
	/**
	 * ボタンのアクションを検知するActionListener
	 * @author itot
	 */
	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton buttonPushed = (JButton) e.getSource();

			if (buttonPushed == openDataButton) {
				grid1 = FileReader.getGrid(url1);
				grid2 = FileReader.getGrid(url2);
				sliderX.setValue(10);
				sliderY.setValue(10);
				sliderZ.setValue(10);
				canvas.setGrid1(grid1);
				canvas.setGrid2(grid2);
				canvas.setStreamline1(null);
				canvas.setStreamline2(null);
			}
			
			if (buttonPushed == viewResetButton) {
				grid1.setStartPoint(10, 10, 10);
				grid2.setStartPoint(10, 10, 10);
				sliderX.setValue(10);
				sliderY.setValue(10);
				sliderZ.setValue(10);
				canvas.viewReset();
			}
			
			if (buttonPushed == generateButton) {
				Streamline sl1 = new Streamline();
				Streamline sl2 = new Streamline();
				int eIjk[] = new int[3];
				int numg[] = grid1.getNumGridPoint();
				eIjk[0] = sliderX.getValue() * numg[0] / 100;
				eIjk[1] = sliderY.getValue() * numg[1] / 100;
				eIjk[2] = sliderZ.getValue() * numg[2] / 100;
				StreamlineGenerator.generate(grid1, sl1, eIjk, null);
				System.out.println("    target:" + grid1.intersectWithTarget(sl1)); 
				canvas.setStreamline1(sl1);
				StreamlineGenerator.generate(grid2, sl2, eIjk, null);
				System.out.println("    target:" + grid1.intersectWithTarget(sl2)); 
				canvas.setStreamline2(sl2);
				
			}
			
			if (buttonPushed == viewVectorButton) {
				
			}
			
			if (buttonPushed == viewCriticalPoint) {
				canvas.setCriticalPoint(true);
			}
			if (buttonPushed == viewVorticity) {
				viewVorticity_flag = !viewVorticity_flag;
				canvas.setVorticity(viewVorticity_flag);
			}
			canvas.display();
		}
	}


	/**
	 * ファイルダイアログにイベントがあったときに、対応するディレクトリを特定する
	 * @return ファイル
	 */
	String getDirectory() {
		JFileChooser dirChooser = new JFileChooser();
		dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int selected = dirChooser.showOpenDialog(container);
		if (selected == JFileChooser.APPROVE_OPTION) { // open selected
			return dirChooser.getSelectedFile().getAbsolutePath();
		} else if (selected == JFileChooser.CANCEL_OPTION) { // cancel selected
			return null;
		} 
		
		return null;
	}

	
	/**
	 * 拡張子がJPGであるファイルの名前一式を配列に確保して返す
	 */
	String[] getJpegFilenames(String dirname) {
	
		File directory = new File(dirname);
		String[] filelist = directory.list();
		int num = 0;
		for(int i = 0; i < filelist.length; i++) {
			if(filelist[i].endsWith("JPG") || filelist[i].endsWith("jpg"))
				num++;
		}
		
		String jpeglist[] = new String[num];
		num = 0;
		for(int i = 0; i < filelist.length; i++) {
			if(filelist[i].endsWith("JPG") || filelist[i].endsWith("jpg"))
				jpeglist[num++] = filelist[i];
		}
		
		return jpeglist;
	}
			
			
	/**
	 * ラジオボタンのアクションを検知するActionListener
	 * @author itot
	 */
	class RadioButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JRadioButton buttonPushed = (JRadioButton) e.getSource();
			if (buttonPushed == viewRotateButton) {
				canvas.setDragMode(3);
			}
			if (buttonPushed == viewScaleButton) {
				canvas.setDragMode(1);
			}
			if (buttonPushed == viewShiftButton) {
				canvas.setDragMode(2);
			}
			if(buttonPushed == bothGridView){
				canvas.setVectorView(1);
			}
			if(buttonPushed == grid1View){
				canvas.setVectorView(2);
			}
			if(buttonPushed == grid2View){
				canvas.setVectorView(3);
			}
			canvas.display();
		}
	}

	/**
	 * チェックボックスのアクションを検知するItemListener
	 * @author itot
	 */
	class CheckBoxListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			JCheckBox stateChanged = (JCheckBox) e.getSource();

			// 再描画
			canvas.display();
		}
	}

	/**
	 * スライダのアクションを検知するActionListener
	 * @author itot
	 */
	class SliderListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			int numg[] = grid1.getNumGridPoint();
			JSlider changedSlider = (JSlider) e.getSource();
			if (changedSlider == sliderX) {
				xText.setText(" よこ:" + sliderX.getValue());
				grid1.startPoint[0] = sliderX.getValue() * numg[0] / 100;
				grid2.startPoint[0] = sliderX.getValue() * numg[0] / 100;
			}
			else if (changedSlider == sliderY) {
				yText.setText(" たて:" + sliderY.getValue());
				grid1.startPoint[1] = sliderY.getValue() * numg[1] / 100;
				grid2.startPoint[1] = sliderY.getValue() * numg[1] / 100;
			}
			else if (changedSlider == sliderZ) {
				zText.setText(" たかさ:" + sliderZ.getValue());
				grid1.startPoint[2] = sliderZ.getValue() * numg[2] / 100;
				grid2.startPoint[2] = sliderZ.getValue() * numg[2] / 100;
			}
			else if(changedSlider == vheight){
				vtext.setText(" ベクトル面地上から: " + vheight.getValue());
				canvas.setVheight(vheight.getValue());
			}
			else if(changedSlider == sliderVH){
				vhText.setText(" 高さ(渦度): " + sliderVH.getValue());
				canvas.setVortheight(sliderVH.getValue());
			}
			canvas.display();
		}
	}
}
