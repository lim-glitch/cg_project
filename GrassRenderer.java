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

        gl.glColor3f(0.0f, 0.6f, 0.0f); // grass color
        gl.glBegin(GL2.GL_TRIANGLES);

        // first row of grass
        for (float x = -1.0f; x < 1.0f; x += 0.1f) {
            gl.glVertex2f(x, -0.6f);
            gl.glVertex2f(x + 0.05f, -0.4f);
            gl.glVertex2f(x + 0.1f, -0.6f);
        }

        // second row of grass
        for (float x = -1.0f; x < 1.0f; x += 0.1f) {
            gl.glVertex2f(x, -0.8f);
            gl.glVertex2f(x + 0.05f, -0.6f);
            gl.glVertex2f(x + 0.1f, -0.8f);
        }

        gl.glEnd();
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