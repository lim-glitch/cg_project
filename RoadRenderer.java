package Park;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.Animator;

import javax.swing.*;

public class RoadRenderer implements GLEventListener {

    private final GLU glu = new GLU();

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);

        // Match sun direction (sun at +X, +Y, -Z → light vector points from sun to scene)
        float[] lightPos = {0.6f, 0.6f, -0.5f, 0.0f}; // 0.0 = directional light
        float[] ambient = {0.2f, 0.2f, 0.2f, 1.0f};
        float[] diffuse = {1.0f, 1.0f, 0.9f, 1.0f};  // Warm sunlight

        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuse, 0);

        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);

        gl.glClearColor(0.5f, 0.8f, 1.0f, 1.0f); // sky blue
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {}

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        // Camera setup
        glu.gluLookAt(
            0.0, 1.5, 3.0,    // eye
            0.0, 0.0, 0.0,    // center
            0.0, 1.0, 0.0     // up
        );

        drawPathway(gl);
    }

    private void drawPathway(GL2 gl) {
        // Road dimensions
        float length = 10.0f;
        float width = 0.6f;
        float height = 0.1f;

        float topY = -0.2f;
        float bottomY = topY - height;

        float halfLength = length / 2;
        float halfWidth = width / 2;

        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f(0.95f, 0.85f, 0.7f); // dirt path color

        // Top face (normal up)
        gl.glNormal3f(0.0f, 1.0f, 0.0f);
        gl.glVertex3f(-halfLength, topY, -halfWidth);
        gl.glVertex3f(halfLength, topY, -halfWidth);
        gl.glVertex3f(halfLength, topY, halfWidth);
        gl.glVertex3f(-halfLength, topY, halfWidth);

        // Bottom face (normal down)
        gl.glNormal3f(0.0f, -1.0f, 0.0f);
        gl.glVertex3f(-halfLength, bottomY, halfWidth);
        gl.glVertex3f(halfLength, bottomY, halfWidth);
        gl.glVertex3f(halfLength, bottomY, -halfWidth);
        gl.glVertex3f(-halfLength, bottomY, -halfWidth);

        // Front face
        gl.glNormal3f(0.0f, 0.0f, 1.0f);
        gl.glVertex3f(-halfLength, bottomY, halfWidth);
        gl.glVertex3f(-halfLength, topY, halfWidth);
        gl.glVertex3f(halfLength, topY, halfWidth);
        gl.glVertex3f(halfLength, bottomY, halfWidth);

        // Back face
        gl.glNormal3f(0.0f, 0.0f, -1.0f);
        gl.glVertex3f(-halfLength, bottomY, -halfWidth);
        gl.glVertex3f(halfLength, bottomY, -halfWidth);
        gl.glVertex3f(halfLength, topY, -halfWidth);
        gl.glVertex3f(-halfLength, topY, -halfWidth);

        // Left face
        gl.glNormal3f(-1.0f, 0.0f, 0.0f);
        gl.glVertex3f(-halfLength, bottomY, -halfWidth);
        gl.glVertex3f(-halfLength, topY, -halfWidth);
        gl.glVertex3f(-halfLength, topY, halfWidth);
        gl.glVertex3f(-halfLength, bottomY, halfWidth);

        // Right face
        gl.glNormal3f(1.0f, 0.0f, 0.0f);
        gl.glVertex3f(halfLength, bottomY, -halfWidth);
        gl.glVertex3f(halfLength, bottomY, halfWidth);
        gl.glVertex3f(halfLength, topY, halfWidth);
        gl.glVertex3f(halfLength, topY, -halfWidth);

        gl.glEnd();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        final GL2 gl = drawable.getGL().getGL2();

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        float aspect = (float) width / height;
        glu.gluPerspective(60.0, aspect, 0.1, 100.0); // perspective projection

        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    public static void main(String[] args) {
        final JFrame frame = new JFrame("JOGL Jogging Path Render");
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        final GLCapabilities capabilities = new GLCapabilities(profile);
        final GLCanvas canvas = new GLCanvas(capabilities);

        RoadRenderer scene = new RoadRenderer();
        canvas.addGLEventListener(scene);
        frame.getContentPane().add(canvas);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        final Animator animator = new Animator(canvas);
        animator.start();
    }
}

