import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import javax.swing.*;

public class GrassRenderer implements GLEventListener {

    public static void main(String[] args) {
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        GLCanvas canvas = new GLCanvas(capabilities);
        GrassRenderer renderer = new GrassRenderer();
        canvas.addGLEventListener(renderer);
        canvas.setSize(600, 400);

        JFrame frame = new JFrame("Grass Scene");
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

        drawGrassRow(gl, -0.6f, true);  // first row: big-small
        drawGrassRow(gl, -0.8f, false); // second row: small-big
    }

    // draw a row of grass with alternating big-small pattern
    private void drawGrassRow(GL2 gl, float baseY, boolean startWithBig) {
        float x = -1.0f;
        int index = 0;

        while (x < 1.0f) {
            boolean isBig = (index % 2 == 0) == startWithBig;

            float width = isBig ? 0.12f : 0.08f;
            float height = isBig ? 0.22f : 0.15f;

            // left triangle (dark)
            gl.glColor3f(0.0f, 0.45f, 0.0f);
            gl.glBegin(GL2.GL_TRIANGLES);
            gl.glVertex2f(x, baseY);
            gl.glVertex2f(x + width / 2, baseY + height);
            gl.glVertex2f(x + width / 2, baseY);
            gl.glEnd();

            // right triangle (light)
            gl.glColor3f(0.0f, 0.75f, 0.0f);
            gl.glBegin(GL2.GL_TRIANGLES);
            gl.glVertex2f(x + width / 2, baseY);
            gl.glVertex2f(x + width, baseY);
            gl.glVertex2f(x + width / 2, baseY + height);
            gl.glEnd();

            x += width + 0.01f;
            index++;
        }
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
