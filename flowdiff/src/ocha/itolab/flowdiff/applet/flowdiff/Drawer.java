package ocha.itolab.flowdiff.applet.flowdiff;


import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

import ocha.itolab.flowdiff.core.data.Element;
import ocha.itolab.flowdiff.core.data.Grid;
import ocha.itolab.flowdiff.core.streamline.Streamline;
import ocha.itolab.flowdiff.core.streamline.StreamlineGenerator;

import com.sun.opengl.util.gl2.GLUT;
//import com.jogamp.opengl.util.gl2.GLUT;



/**
 * �`�揈���̃N���X
 * 
 * @author itot
 */
public class Drawer implements GLEventListener {

	private GL gl;
	private GL2 gl2;
	private GLU glu;
	private GLUT glut;
	GLAutoDrawable glAD;
	GLCanvas glcanvas;

	Transformer trans = null;

	DoubleBuffer modelview, projection, p1, p2, p3, p4;
	IntBuffer viewport;
	int windowWidth, windowHeight;

	boolean isMousePressed = false, isAnnotation = true;
	boolean isImage = true, isWireframe = true;

	double linewidth = 1.0;
	long datemin, datemax;
	int authmax;

	int dragMode = 1;

	private double angleX = 0.0;
	private double angleY = 0.0;
	private double shiftX = 0.0;
	private double shiftY = 0.0;
	private double scale = 1.0;
	private double centerX = 0.5;
	private double centerY = 0.5;
	private double centerZ = 0.0;
	private double size = 0.25;

	Grid grid1 = null, grid2 = null;
	Streamline sl1 = null, sl2 = null;
	
	/**
	 * Constructor
	 * 
	 * @param width
	 *            �`��̈�̕�
	 * @param height
	 *            �`��̈�̍���
	 */
	public Drawer(int width, int height, GLCanvas c) {
		glcanvas = c;
		windowWidth = width;
		windowHeight = height;

		viewport = IntBuffer.allocate(4);
		modelview = DoubleBuffer.allocate(16);
		projection = DoubleBuffer.allocate(16);

		p1 = DoubleBuffer.allocate(3);
		p2 = DoubleBuffer.allocate(3);
		p3 = DoubleBuffer.allocate(3);
		p4 = DoubleBuffer.allocate(3);

		glcanvas.addGLEventListener((javax.media.opengl.GLEventListener) this);
	}

	public GLAutoDrawable getGLAutoDrawable() {
		return glAD;
	}

	/**
	 * �_�~�[���\�b�h
	 */
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
			boolean deviceChanged) {
	}

	/**
	 * Transformer���Z�b�g����
	 * 
	 * @param transformer
	 */
	public void setTransformer(Transformer view) {
		this.trans = view;
	}

	
	/**
	 * Grid���Z�b�g����
	 */
	public void setGrid1(Grid g) {
		grid1 = g;
		double minmax[] = grid1.getMinmaxPos();
		centerX = (minmax[0] + minmax[1]) * 0.5;
		centerY = (minmax[2] + minmax[3]) * 0.5;
		centerZ = (minmax[4] + minmax[5]) * 0.5;
	}
	
	/**
	 * Grid���Z�b�g����
	 */
	public void setGrid2(Grid g) {
		grid2 = g;
		double minmax[] = grid2.getMinmaxPos();
		centerX = (minmax[0] + minmax[1]) * 0.5;
		centerY = (minmax[2] + minmax[3]) * 0.5;
		centerZ = (minmax[4] + minmax[5]) * 0.5;
	}
	
	/**
	 * Streamline���Z�b�g����
	 */
	public void setStreamline1(Streamline s) {
		sl1 = s;
	}

	/**
	 * Streamline���Z�b�g����
	 */
	public void setStreamline2(Streamline s) {
		sl2 = s;
	}
	
	/**
	 * �`��̈�̃T�C�Y��ݒ肷��
	 * 
	 * @param width
	 *            �`��̈�̕�
	 * @param height
	 *            �`��̈�̍���
	 */
	public void setWindowSize(int width, int height) {
		windowWidth = width;
		windowHeight = height;
	}

	/**
	 * �}�E�X�{�^����ON/OFF��ݒ肷��
	 * 
	 * @param isMousePressed
	 *            �}�E�X�{�^����������Ă����true
	 */
	public void setMousePressSwitch(boolean isMousePressed) {
		this.isMousePressed = isMousePressed;
	}

	/**
	 * ��̑������Z�b�g����
	 * 
	 * @param lw
	 *            ��̑����i��f���j
	 */
	public void setLinewidth(double lw) {
		linewidth = lw;
	}

	/**
	 * Image�̉ۂ��Z�b�g����
	 */
	public void isImage(boolean is) {
		isImage = is;
	}

	/**
	 * Wireframe�̉ۂ��Z�b�g����
	 */
	public void isWireframe(boolean is) {
		isWireframe = is;
	}


	/**
	 * �}�E�X�h���b�O�̃��[�h��ݒ肷��
	 * 
	 * @param dragMode
	 *            (1:ZOOM 2:SHIFT 3:ROTATE)
	 */
	public void setDragMode(int newMode) {
		dragMode = newMode;
	}

	/**
	 * ����
	 */
	public void init(GLAutoDrawable drawable) {

		gl = drawable.getGL();
		gl2= drawable.getGL().getGL2();
		glu = new GLU();
		glut = new GLUT();
		this.glAD = drawable;

		gl.glEnable(GL.GL_RGBA);
		gl.glEnable(GL2.GL_DEPTH);
		gl.glEnable(GL2.GL_DOUBLE);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL2.GL_NORMALIZE);
		gl2.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, GL.GL_TRUE);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

	}

	/**
	 * �ĕ`��
	 */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {

		windowWidth = width;
		windowHeight = height;

		// �r���[�|�[�g�̒�`
		gl.glViewport(0, 0, width, height);

		// ���e�ϊ��s��̒�`
		gl2.glMatrixMode(GL2.GL_PROJECTION);
		gl2.glLoadIdentity();
		gl2.glOrtho(-width / 200.0, width / 200.0, -height / 200.0,
				height / 200.0, -1000.0, 1000.0);

		gl2.glMatrixMode(GL2.GL_MODELVIEW);

	}

	/**
	 * �`������s����
	 */
	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		// ���_�ʒu������
		gl2.glLoadIdentity();
		glu.gluLookAt(centerX, centerY, (centerZ + 20.0), centerX, centerY,
				centerZ, 0.0, 1.0, 0.0);

		shiftX = trans.getViewShift(0);
		shiftY = trans.getViewShift(1);
		scale = trans.getViewScaleY() * windowHeight / (size * 300.0);
		angleX = trans.getViewRotateY() * 45.0;
		angleY = trans.getViewRotateX() * 45.0;

		// �s����v�b�V��
		gl2.glPushMatrix();

		// �������񌴓_���ɕ��̂𓮂���
		gl2.glTranslated(centerX, centerY, centerZ);

		// �}�E�X�̈ړ��ʂɉ����ĉ�]
		gl2.glRotated(angleX, 1.0, 0.0, 0.0);
		gl2.glRotated(angleY, 0.0, 1.0, 0.0);

		// �}�E�X�̈ړ��ʂɉ����Ĉړ�
		gl2.glTranslated(shiftX, shiftY, 0.0);

		// �}�E�X�̈ړ��ʂɉ����Ċg��k��
		gl2.glScaled(scale, scale, scale);

		// ���̂����Ƃ̈ʒu�ɖ߂�
		gl2.glTranslated(-centerX, -centerY, -centerZ);

		// �ϊ��s��ƃr���[�|�[�g�̒l��ۑ�����
		gl.glGetIntegerv(GL.GL_VIEWPORT, viewport);
		gl2.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, modelview);
		gl2.glGetDoublev(GL2.GL_PROJECTION_MATRIX, projection);

		drawBox();
		
		if(grid1 != null && sl1 != null) {
			drawTarget(grid1, sl1);
			drawStartGrid(grid1);
			drawStreamline(sl1, 1);
			drawEndGrid(grid1);
		}
		if(grid2 != null && sl2 != null) {
			drawTarget(grid2, sl2);
			drawStartGrid(grid2);
			drawStreamline(sl2, 2);
			drawEndGrid(grid2);
		}
		
		// �s����|�b�v
		gl2.glPopMatrix();

	}

	
	/**
	 * �i�q�̈�𔠂ŕ`�悷��
	 */
	void drawBox() {
		if(grid1 == null) return;
		double minmax[] = grid1.getMinmaxPos();
		
		// 6�{�̃��[�v��`��
		gl2.glColor3d(0.5, 0.5, 0.5);
		gl2.glBegin(GL.GL_LINE_LOOP);
		gl2.glVertex3d(minmax[0], minmax[2], minmax[4]);
		gl2.glVertex3d(minmax[1], minmax[2], minmax[4]);
		gl2.glVertex3d(minmax[1], minmax[3], minmax[4]);
		gl2.glVertex3d(minmax[0], minmax[3], minmax[4]);
		gl2.glEnd();
		gl2.glBegin(GL.GL_LINE_LOOP);
		gl2.glVertex3d(minmax[0], minmax[2], minmax[5]);
		gl2.glVertex3d(minmax[1], minmax[2], minmax[5]);
		gl2.glVertex3d(minmax[1], minmax[3], minmax[5]);
		gl2.glVertex3d(minmax[0], minmax[3], minmax[5]);
		gl2.glEnd();
		gl2.glBegin(GL.GL_LINE_LOOP);
		gl2.glVertex3d(minmax[0], minmax[2], minmax[4]);
		gl2.glVertex3d(minmax[1], minmax[2], minmax[4]);
		gl2.glVertex3d(minmax[1], minmax[2], minmax[5]);
		gl2.glVertex3d(minmax[0], minmax[2], minmax[5]);
		gl2.glEnd();
		gl2.glBegin(GL.GL_LINE_LOOP);
		gl2.glVertex3d(minmax[0], minmax[3], minmax[4]);
		gl2.glVertex3d(minmax[1], minmax[3], minmax[4]);
		gl2.glVertex3d(minmax[1], minmax[3], minmax[5]);
		gl2.glVertex3d(minmax[0], minmax[3], minmax[5]);
		gl2.glEnd();
		gl2.glBegin(GL.GL_LINE_LOOP);
		gl2.glVertex3d(minmax[0], minmax[2], minmax[4]);
		gl2.glVertex3d(minmax[0], minmax[3], minmax[4]);
		gl2.glVertex3d(minmax[0], minmax[3], minmax[5]);
		gl2.glVertex3d(minmax[0], minmax[2], minmax[5]);
		gl2.glEnd();
		gl2.glBegin(GL.GL_LINE_LOOP);
		gl2.glVertex3d(minmax[1], minmax[2], minmax[4]);
		gl2.glVertex3d(minmax[1], minmax[3], minmax[4]);
		gl2.glVertex3d(minmax[1], minmax[3], minmax[5]);
		gl2.glVertex3d(minmax[1], minmax[2], minmax[5]);
		gl2.glEnd();
	}
	
	/**
	 * �n�_��`�悷��
	 */
	void drawStartGrid(Grid grid){
		int i, j, k;
		if(grid == null) return;
		i = grid.startPoint[0];
		j = grid.startPoint[1];
		k = grid.startPoint[2];

		double minmax[] = new double[6];
		minmax[0] = minmax[2] = minmax[4] = 1.0e+30;
		minmax[1] = minmax[3] = minmax[5] = -1.0e+30;
		
		Element element = grid.getElement(grid.calcElementId(i, j, k));
		for (int d = 0; d < 8; d++){
			double pos[] = element.gp[d].getPosition();
			for (int loop = 0; loop < 3; loop++){
				minmax[loop*2] = (minmax[loop*2] > pos[loop] ? pos[loop] : minmax[loop*2]);
				minmax[loop*2 + 1] = (minmax[loop*2 + 1] < pos[loop] ? pos[loop] : minmax[loop*2 + 1]);
			}
		}
		
		// 6�{�̃��[�v��`��
		gl2.glColor3d(1.0, 1.0, 1.0);
		
		gl2.glBegin(GL.GL_LINE_LOOP);
		gl2.glVertex3d(minmax[0], minmax[2], minmax[4]);
		gl2.glVertex3d(minmax[1], minmax[2], minmax[4]);
		gl2.glVertex3d(minmax[1], minmax[3], minmax[4]);
		gl2.glVertex3d(minmax[0], minmax[3], minmax[4]);
		gl2.glEnd();
		gl2.glBegin(GL.GL_LINE_LOOP);
		gl2.glVertex3d(minmax[0], minmax[2], minmax[5]);
		gl2.glVertex3d(minmax[1], minmax[2], minmax[5]);
		gl2.glVertex3d(minmax[1], minmax[3], minmax[5]);
		gl2.glVertex3d(minmax[0], minmax[3], minmax[5]);
		gl2.glEnd();
		gl2.glBegin(GL.GL_LINE_LOOP);
		gl2.glVertex3d(minmax[0], minmax[2], minmax[4]);
		gl2.glVertex3d(minmax[1], minmax[2], minmax[4]);
		gl2.glVertex3d(minmax[1], minmax[2], minmax[5]);
		gl2.glVertex3d(minmax[0], minmax[2], minmax[5]);
		gl2.glEnd();
		gl2.glBegin(GL.GL_LINE_LOOP);
		gl2.glVertex3d(minmax[0], minmax[3], minmax[4]);
		gl2.glVertex3d(minmax[1], minmax[3], minmax[4]);
		gl2.glVertex3d(minmax[1], minmax[3], minmax[5]);
		gl2.glVertex3d(minmax[0], minmax[3], minmax[5]);
		gl2.glEnd();
		gl2.glBegin(GL.GL_LINE_LOOP);
		gl2.glVertex3d(minmax[0], minmax[2], minmax[4]);
		gl2.glVertex3d(minmax[0], minmax[3], minmax[4]);
		gl2.glVertex3d(minmax[0], minmax[3], minmax[5]);
		gl2.glVertex3d(minmax[0], minmax[2], minmax[5]);
		gl2.glEnd();
		gl2.glBegin(GL.GL_LINE_LOOP);
		gl2.glVertex3d(minmax[1], minmax[2], minmax[4]);
		gl2.glVertex3d(minmax[1], minmax[3], minmax[4]);
		gl2.glVertex3d(minmax[1], minmax[3], minmax[5]);
		gl2.glVertex3d(minmax[1], minmax[2], minmax[5]);
		gl2.glEnd();
	}
	
	
	/**
	 * �I��`�悷��
	 */
	void drawTarget(Grid grid, Streamline sl){
		int i, j, k;
		if(grid == null) return;
		i = grid.target[0];
		j = grid.target[1];
		k = grid.target[2];

		double minmax[] = new double[6];
		minmax[0] = minmax[2] = minmax[4] = 1.0e+30;
		minmax[1] = minmax[3] = minmax[5] = -1.0e+30;
		
		Element element = grid.getElement(grid.calcElementId(i, j, k));
		for (int d = 0; d < 8; d++){
			double pos[] = element.gp[d].getPosition();
			for (int loop = 0; loop < 3; loop++){
				minmax[loop*2] = (minmax[loop*2] > pos[loop] ? pos[loop] : minmax[loop*2]);
				minmax[loop*2 + 1] = (minmax[loop*2 + 1] < pos[loop] ? pos[loop] : minmax[loop*2 + 1]);
			}
		}
		
		// 6�{�̃��[�v��`��
		if (sl == null) {
			gl2.glColor3d(1.0, 0.0, 0.0);
		}
		else if (grid.intersectWithTarget(sl)) {
			gl2.glColor3d(1.0, 1.0, 0.0);
		}
		else {
			gl2.glColor3d(1.0, 0.0, 0.0);
		}
		gl2.glBegin(GL.GL_LINE_LOOP);
		gl2.glVertex3d(minmax[0], minmax[2], minmax[4]);
		gl2.glVertex3d(minmax[1], minmax[2], minmax[4]);
		gl2.glVertex3d(minmax[1], minmax[3], minmax[4]);
		gl2.glVertex3d(minmax[0], minmax[3], minmax[4]);
		gl2.glEnd();
		gl2.glBegin(GL.GL_LINE_LOOP);
		gl2.glVertex3d(minmax[0], minmax[2], minmax[5]);
		gl2.glVertex3d(minmax[1], minmax[2], minmax[5]);
		gl2.glVertex3d(minmax[1], minmax[3], minmax[5]);
		gl2.glVertex3d(minmax[0], minmax[3], minmax[5]);
		gl2.glEnd();
		gl2.glBegin(GL.GL_LINE_LOOP);
		gl2.glVertex3d(minmax[0], minmax[2], minmax[4]);
		gl2.glVertex3d(minmax[1], minmax[2], minmax[4]);
		gl2.glVertex3d(minmax[1], minmax[2], minmax[5]);
		gl2.glVertex3d(minmax[0], minmax[2], minmax[5]);
		gl2.glEnd();
		gl2.glBegin(GL.GL_LINE_LOOP);
		gl2.glVertex3d(minmax[0], minmax[3], minmax[4]);
		gl2.glVertex3d(minmax[1], minmax[3], minmax[4]);
		gl2.glVertex3d(minmax[1], minmax[3], minmax[5]);
		gl2.glVertex3d(minmax[0], minmax[3], minmax[5]);
		gl2.glEnd();
		gl2.glBegin(GL.GL_LINE_LOOP);
		gl2.glVertex3d(minmax[0], minmax[2], minmax[4]);
		gl2.glVertex3d(minmax[0], minmax[3], minmax[4]);
		gl2.glVertex3d(minmax[0], minmax[3], minmax[5]);
		gl2.glVertex3d(minmax[0], minmax[2], minmax[5]);
		gl2.glEnd();
		gl2.glBegin(GL.GL_LINE_LOOP);
		gl2.glVertex3d(minmax[1], minmax[2], minmax[4]);
		gl2.glVertex3d(minmax[1], minmax[3], minmax[4]);
		gl2.glVertex3d(minmax[1], minmax[3], minmax[5]);
		gl2.glVertex3d(minmax[1], minmax[2], minmax[5]);
		gl2.glEnd();
	}
	
	/**
	 * ����̍s����������̊i�q��`��
	 */
	public void drawEndGrid(Grid grid){
		if(grid == null) return;
		
		double minmax[] = new double[6];
		minmax[0] = minmax[2] = minmax[4] = 1.0e+30;
		minmax[1] = minmax[3] = minmax[5] = -1.0e+30;
		
		Element element;
		int lastId = StreamlineGenerator.lastElementId();
		if (lastId > 0) {
			element = grid.getElement(lastId);
		}
		else {
			return;
		}
		// System.out.println("    lastElementId=" + StreamlineGenerator.lastElementId());
		for (int d = 0; d < 8; d++){
			double pos[] = element.gp[d].getPosition();
			for (int loop = 0; loop < 3; loop++){
				minmax[loop*2] = (minmax[loop*2] > pos[loop] ? pos[loop] : minmax[loop*2]);
				minmax[loop*2 + 1] = (minmax[loop*2 + 1] < pos[loop] ? pos[loop] : minmax[loop*2 + 1]);
			}
		}
		
		// 6�{�̃��[�v��`��

		gl2.glColor3d(0.0, 1.0, 0.0);
		
		gl2.glBegin(GL.GL_LINE_LOOP);
		gl2.glVertex3d(minmax[0], minmax[2], minmax[4]);
		gl2.glVertex3d(minmax[1], minmax[2], minmax[4]);
		gl2.glVertex3d(minmax[1], minmax[3], minmax[4]);
		gl2.glVertex3d(minmax[0], minmax[3], minmax[4]);
		gl2.glEnd();
		gl2.glBegin(GL.GL_LINE_LOOP);
		gl2.glVertex3d(minmax[0], minmax[2], minmax[5]);
		gl2.glVertex3d(minmax[1], minmax[2], minmax[5]);
		gl2.glVertex3d(minmax[1], minmax[3], minmax[5]);
		gl2.glVertex3d(minmax[0], minmax[3], minmax[5]);
		gl2.glEnd();
		gl2.glBegin(GL.GL_LINE_LOOP);
		gl2.glVertex3d(minmax[0], minmax[2], minmax[4]);
		gl2.glVertex3d(minmax[1], minmax[2], minmax[4]);
		gl2.glVertex3d(minmax[1], minmax[2], minmax[5]);
		gl2.glVertex3d(minmax[0], minmax[2], minmax[5]);
		gl2.glEnd();
		gl2.glBegin(GL.GL_LINE_LOOP);
		gl2.glVertex3d(minmax[0], minmax[3], minmax[4]);
		gl2.glVertex3d(minmax[1], minmax[3], minmax[4]);
		gl2.glVertex3d(minmax[1], minmax[3], minmax[5]);
		gl2.glVertex3d(minmax[0], minmax[3], minmax[5]);
		gl2.glEnd();
		gl2.glBegin(GL.GL_LINE_LOOP);
		gl2.glVertex3d(minmax[0], minmax[2], minmax[4]);
		gl2.glVertex3d(minmax[0], minmax[3], minmax[4]);
		gl2.glVertex3d(minmax[0], minmax[3], minmax[5]);
		gl2.glVertex3d(minmax[0], minmax[2], minmax[5]);
		gl2.glEnd();
		gl2.glBegin(GL.GL_LINE_LOOP);
		gl2.glVertex3d(minmax[1], minmax[2], minmax[4]);
		gl2.glVertex3d(minmax[1], minmax[3], minmax[4]);
		gl2.glVertex3d(minmax[1], minmax[3], minmax[5]);
		gl2.glVertex3d(minmax[1], minmax[2], minmax[5]);
		gl2.glEnd();
	
	}
	
	
	/**
	 * �����`��
	 */
	void drawStreamline(Streamline sl, int id) {
		
		// �܂���`��
		if(id == 1)
			gl2.glColor3d(1.0, 0.0, 1.0);
		if(id == 2)
			gl2.glColor3d(0.0, 1.0, 1.0);
		gl2.glBegin(GL2.GL_LINE_STRIP);
		for(int i = 0; i < sl.getNumVertex(); i++) {
			double pos[] = sl.getPosition(i);
			gl2.glVertex3d(pos[0], pos[1], pos[2]);
		}
		gl2.glEnd();
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}
}
