package Park;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;

import javax.swing.*;

public class CloudRenderer implements GLEventListener {

    private final GLUT glut = new GLUT();

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        // Enable depth testing for proper overlap
        gl.glEnable(GL2.GL_DEPTH_TEST);

        // Enable smooth shading
        gl.glShadeModel(GL2.GL_SMOOTH);

        // Lighting setup
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);

        float[] lightPos = {1.0f, 1.0f, 1.0f, 0.0f}; // Directional light
        float[] diffuse = {0.9f, 0.9f, 0.9f, 1.0f};
        float[] ambient = {0.3f, 0.3f, 0.3f, 1.0f};

        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuse, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient, 0);

        // Material follows color
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);

        // Anti-aliasing
        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL2.GL_LINE_SMOOTH);
        gl.glHint(GL2.GL_LINE_SMOOTH_HINT, GL2.GL_NICEST);

        // Background sky color
        gl.glClearColor(0.6f, 0.85f, 1.0f, 1.0f); // light blue sky
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {}

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        drawCloud(gl, -0.5f, 0.6f);
        drawCloud(gl, 0.3f, 0.75f);
    }

    private void drawCloud(GL2 gl, float x, float y) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, -0.5f); // Position the cloud

        // Main puff color
        gl.glColor3f(1.0f, 1.0f, 1.0f); // pure white

        // Draw puff spheres (high-res)
        glut.glutSolidSphere(0.08, 40, 40);
        gl.glTranslatef(0.1f, 0.02f, -0.01f);
        glut.glutSolidSphere(0.1, 40, 40);
        gl.glTranslatef(0.1f, -0.02f, 0.01f);
        glut.glutSolidSphere(0.07, 40, 40);

        // Optional translucent puff (adds softness — try toggling)
        gl.glEnable(GL2.GL_BLEND);
        gl.glColor4f(1.0f, 1.0f, 1.0f, 0.3f);
        gl.glTranslatef(-0.15f, 0.01f, -0.02f);
        glut.glutSolidSphere(0.09, 40, 40);
        gl.glDisable(GL2.GL_BLEND);

        gl.glPopMatrix();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(-1.0, 1.0, -1.0, 1.0, -2.0, 2.0); // Orthographic 2D-style
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    public static void main(String[] args) {
        final JFrame frame = new JFrame("JOGL Cloud Render");
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        final GLCapabilities capabilities = new GLCapabilities(profile);

        // Enable anti-aliasing
        capabilities.setSampleBuffers(true);
        capabilities.setNumSamples(4);

        final GLCanvas canvas = new GLCanvas(capabilities);
        CloudRenderer scene = new CloudRenderer();
        canvas.addGLEventListener(scene);

        frame.getContentPane().add(canvas);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        final Animator animator = new Animator(canvas);
        animator.start();
    }
}

