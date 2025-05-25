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
        float postWidth = 0.06f;
        float halfW = postWidth / 2;
        float baseY = -0.6f;
        float topY = 0.4f;

        // left side of post (dark gray)
        gl.glColor3f(0.1f, 0.1f, 0.1f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(x - halfW, baseY);
        gl.glVertex2f(x, baseY);
        gl.glVertex2f(x, topY);
        gl.glVertex2f(x - halfW, topY);
        gl.glEnd();

        // right side of post (light gray)
        gl.glColor3f(0.3f, 0.3f, 0.3f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(x, baseY);
        gl.glVertex2f(x + halfW, baseY);
        gl.glVertex2f(x + halfW, topY);
        gl.glVertex2f(x, topY);
        gl.glEnd();

        // outer lamp head (darker yellow)
        drawCircle(gl, x, 0.45f, 0.08f, 1.0f, 0.85f, 0.1f);

        // inner lamp head (bright yellow)
        drawCircle(gl, x, 0.45f, 0.05f, 1.0f, 1.0f, 0.0f);
    }

    // draw filled circle with specified color
    private void drawCircle(GL2 gl, float cx, float cy, float r, float red, float green, float blue) {
        gl.glColor3f(red, green, blue);
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glVertex2f(cx, cy);
        for (int i = 0; i <= 100; i++) {
            double angle = 2 * Math.PI * i / 100;
            float px = (float)(cx + r * Math.cos(angle));
            float py = (float)(cy + r * Math.sin(angle));
            gl.glVertex2f(px, py);
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
