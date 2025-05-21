package Park;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;

public class BenchRenderer implements GLEventListener {

    private final GLU glu = new GLU();
    private final GLUT glut = new GLUT();

    public static void main(String[] args) {
        JFrame frame = new JFrame("3D Bench in JOGL");
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        GLCanvas canvas = new GLCanvas(capabilities);

        BenchRenderer renderer = new BenchRenderer();
        canvas.addGLEventListener(renderer);
        canvas.setSize(800, 600);

        frame.getContentPane().add(canvas);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        FPSAnimator animator = new FPSAnimator(canvas, 60);
        animator.start();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        // Enable depth and lighting
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glShadeModel(GL2.GL_SMOOTH);

        // Light position
        float[] lightPos = {0.0f, 5.0f, 10.0f, 1.0f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos, 0);

        // Background color
        gl.glClearColor(0.7f, 0.8f, 1.0f, 1.0f); // Sky blue
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();

        if (height <= 0) height = 1;
        float aspect = (float) width / height;

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0, aspect, 1.0, 100.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        glu.gluLookAt(0, 2, 10, 0, 1, 0, 0, 1, 0);

        // Set color
        gl.glColor3f(0.6f, 0.3f, 0.1f); // brown

        drawBench(gl);
    }

    private void drawBench(GL2 gl) {
        // Seat
        gl.glPushMatrix();
        gl.glTranslatef(0, 1, 0);
        gl.glScalef(4, 0.2f, 1);
        drawCube(gl);
        gl.glPopMatrix();

        // Backrest
        gl.glPushMatrix();
        gl.glTranslatef(0, 1.8f, -0.4f);
        gl.glScalef(4, 1.0f, 0.2f);
        drawCube(gl);
        gl.glPopMatrix();

        // Legs
        float[][] legPositions = {
            {-1.8f, 0.5f, 0.4f}, {1.8f, 0.5f, 0.4f},
            {-1.8f, 0.5f, -0.4f}, {1.8f, 0.5f, -0.4f}
        };

        for (float[] pos : legPositions) {
            gl.glPushMatrix();
            gl.glTranslatef(pos[0], pos[1], pos[2]);
            gl.glScalef(0.2f, 1.0f, 0.2f);
            drawCube(gl);
            gl.glPopMatrix();
        }
    }

    private void drawCube(GL2 gl) {
        glut.glutSolidCube(1.0f);
    }
}
