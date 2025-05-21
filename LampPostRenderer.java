import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import javax.swing.*;

public class LampPostRenderer implements GLEventListener {

    public static void main(String[] args) {
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        GLCanvas canvas = new GLCanvas(capabilities);
        LampPostRenderer renderer = new LampPostRenderer();
        canvas.addGLEventListener(renderer);
        canvas.setSize(600, 400);

        JFrame frame = new JFrame("Lamp Post Scene");
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

        drawLamp(gl, -0.6f); // left lamp
        drawLamp(gl, 0.6f);  // right lamp
    }

    private void drawLamp(GL2 gl, float x) {
        gl.glColor3f(0.1f, 0.1f, 0.1f); // lamp post color
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(x - 0.03f, -0.6f);
        gl.glVertex2f(x + 0.03f, -0.6f);
        gl.glVertex2f(x + 0.03f, 0.4f);
        gl.glVertex2f(x - 0.03f, 0.4f);
        gl.glEnd(); // draw post

        gl.glColor3f(1.0f, 1.0f, 0.0f); // lamp head color
        float cy = 0.45f, r = 0.07f;
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glVertex2f(x, cy);
        for (int i = 0; i <= 100; i++) {
            double angle = 2 * Math.PI * i / 100;
            float px = (float)(x + r * Math.cos(angle));
            float py = (float)(cy + r * Math.sin(angle));
            gl.glVertex2f(px, py);
        }
        gl.glEnd(); // draw head
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