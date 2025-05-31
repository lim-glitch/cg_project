package Park;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;

import javax.swing.*;

public class SunRenderer implements GLEventListener {

    private final GLU glu = new GLU();
    private final GLUT glut = new GLUT();

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        // Enable depth and smooth shading
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glShadeModel(GL2.GL_SMOOTH);

        // Lighting configuration
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);

        float[] lightPos = {1.0f, 1.0f, 1.0f, 0.0f};  // Directional light
        float[] ambient = {0.3f, 0.3f, 0.1f, 1.0f};  // Warm ambient
        float[] diffuse = {1.0f, 1.0f, 0.6f, 1.0f};  // Bright yellow light

        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuse, 0);

        // Let material respond to light
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);

        // Enable blending for optional glow
        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

        // Anti-aliasing for better edges
        gl.glEnable(GL2.GL_LINE_SMOOTH);
        gl.glHint(GL2.GL_LINE_SMOOTH_HINT, GL2.GL_NICEST);

        // Light blue sky
        gl.glClearColor(0.6f, 0.85f, 1.0f, 1.0f);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {}

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        drawSun(gl);
    }

    private void drawSun(GL2 gl) {
        gl.glPushMatrix();
        gl.glTranslatef(0.6f, 0.6f, -0.5f); // position in sky

        // Sun core
        gl.glColor3f(1.0f, 1.0f, 0.0f); // bright yellow
        glut.glutSolidSphere(0.15, 60, 60); // higher-res sphere

        // Optional glow layer (subtle)
        gl.glColor4f(1.0f, 1.0f, 0.0f, 0.1f); // translucent yellow
        glut.glutSolidSphere(0.20, 60, 60);

        gl.glPopMatrix();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(-1.0, 1.0, -1.0, 1.0, -2.0, 2.0); // 2D-style projection with depth
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    public static void main(String[] args) {
        final JFrame frame = new JFrame("JOGL Sun Render");
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        final GLCapabilities capabilities = new GLCapabilities(profile);

        // Enable anti-aliasing
        capabilities.setSampleBuffers(true);
        capabilities.setNumSamples(4);

        final GLCanvas canvas = new GLCanvas(capabilities);
        SunRenderer sunScene = new SunRenderer();
        canvas.addGLEventListener(sunScene);

        frame.getContentPane().add(canvas);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        final Animator animator = new Animator(canvas);
        animator.start();
    }
}
