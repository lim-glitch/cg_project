import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import javax.swing.*;

public class TreeRenderer implements GLEventListener {

    public static void main(String[] args) {
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        GLCanvas canvas = new GLCanvas(capabilities);
        TreeRenderer renderer = new TreeRenderer();
        canvas.addGLEventListener(renderer);
        canvas.setSize(600, 400);

        JFrame frame = new JFrame("Tree Scene");
        frame.getContentPane().add(canvas);
        frame.setSize(frame.getContentPane().getPreferredSize());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        FPSAnimator animator = new FPSAnimator(canvas, 60, true);
        animator.start();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.5f, 0.8f, 1.0f, 1.0f); // set background
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {}

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        drawTree(gl, -0.6f); // draw left tree
        drawTree(gl, 0.6f);  // draw right tree
    }

    private void drawTree(GL2 gl, float offsetX) {
        gl.glColor3f(0.55f, 0.27f, 0.07f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(offsetX - 0.05f, -0.6f);
        gl.glVertex2f(offsetX + 0.05f, -0.6f);
        gl.glVertex2f(offsetX + 0.05f, -0.2f);
        gl.glVertex2f(offsetX - 0.05f, -0.2f);
        gl.glEnd(); // draw trunk

        gl.glColor3f(0.0f, 0.6f, 0.0f);
        drawCircle(gl, offsetX, 0.0f, 0.2f);
        drawCircle(gl, offsetX, 0.2f, 0.17f);
        drawCircle(gl, offsetX, 0.37f, 0.15f);
    }

    private void drawCircle(GL2 gl, float cx, float cy, float r) {
        int numSegments = 100;
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glVertex2f(cx, cy);
        for (int i = 0; i <= numSegments; i++) {
            double angle = 2 * Math.PI * i / numSegments;
            float x = (float)(cx + r * Math.cos(angle));
            float y = (float)(cy + r * Math.sin(angle));
            gl.glVertex2f(x, y);
        }
        gl.glEnd(); // draw circle
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(-1, 1, -1, 1, -1, 1);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }
}