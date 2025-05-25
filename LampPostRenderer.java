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

        JFrame frame = new JFrame("Park Lamp Post");
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

        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);

        float[] ambient = {0.3f, 0.3f, 0.3f, 1f};
        float[] diffuse = {0.8f, 0.8f, 0.8f, 1f};
        float[] position = {5f, 5f, 10f, 1f};

        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuse, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, position, 0);

        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);

        gl.glClearColor(0.7f, 0.8f, 1.0f, 1.0f);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {}

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        drawLamp(gl, -0.6f);
        drawLamp(gl,  0.6f);
    }

    private void drawLamp(GL2 gl, float x) {
        float baseY = -0.6f;
        float topY = 0.4f;
        float halfW = 0.025f;

        // Lamp post - left
        gl.glColor3f(0.1f, 0.1f, 0.1f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(x - halfW, baseY, 0f);
        gl.glVertex3f(x, baseY, 0f);
        gl.glVertex3f(x, topY, 0f);
        gl.glVertex3f(x - halfW, topY, 0f);
        gl.glEnd();

        // Lamp post - right
        gl.glColor3f(0.4f, 0.4f, 0.4f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(x, baseY, 0f);
        gl.glVertex3f(x + halfW, baseY, 0f);
        gl.glVertex3f(x + halfW, topY, 0f);
        gl.glVertex3f(x, topY, 0f);
        gl.glEnd();

        // Base - bottom
        gl.glColor3f(0.2f, 0.2f, 0.2f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(x - 0.035f, topY);
        gl.glVertex2f(x + 0.035f, topY);
        gl.glVertex2f(x + 0.03f, topY + 0.025f);
        gl.glVertex2f(x - 0.03f, topY + 0.025f);
        gl.glEnd();

        // Base - top
        gl.glColor3f(0.4f, 0.4f, 0.4f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(x - 0.025f, topY + 0.025f);
        gl.glVertex2f(x + 0.025f, topY + 0.025f);
        gl.glVertex2f(x + 0.02f, topY + 0.045f);
        gl.glVertex2f(x - 0.02f, topY + 0.045f);
        gl.glEnd();

        // Enlarged outer glow
        drawCircle(gl, x, topY + 0.1f, 0.075f, 1.0f, 0.85f, 0.25f);

        // Enlarged inner light bulb
        drawCircle(gl, x, topY + 0.1f, 0.06f, 1.0f, 1.0f, 0.0f);

        // Highlight reflection
        drawCircle(gl, x + 0.02f, topY + 0.12f, 0.014f, 1.0f, 1.0f, 1.0f);
    }

    private void drawCircle(GL2 gl, float cx, float cy, float r, float red, float green, float blue) {
        gl.glColor3f(red, green, blue);
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glVertex2f(cx, cy);
        for (int i = 0; i <= 100; i++) {
            double angle = 2 * Math.PI * i / 100;
            float x = (float)(cx + r * Math.cos(angle));
            float y = (float)(cy + r * Math.sin(angle));
            gl.glVertex2f(x, y);
        }
        gl.glEnd();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(-1, 1, -1, 1, -5, 5);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }
}
