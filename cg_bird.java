/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jogl_test;

/**
 *
 * @author Acer
 */
import javax.swing.JFrame;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

public class cg_bird implements GLEventListener {

    private GLU glu = new GLU();

    public static void main(String[] args) {
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        final GLCanvas glcanvas = new GLCanvas(capabilities);

        cg_bird birdDemo = new cg_bird();
        glcanvas.addGLEventListener(birdDemo);
        glcanvas.setSize(800, 600);

        final JFrame frame = new JFrame("3D Realistic Bird Side View - JOGL");
        frame.getContentPane().add(glcanvas);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);

        float[] ambientLight = {0.2f, 0.2f, 0.2f, 1f};
        float[] diffuseLight = {0.8f, 0.8f, 0.8f, 1f};
        float[] lightPosition = {1f, 1f, 2f, 0f};

        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientLight, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuseLight, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPosition, 0);

        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f); // white background
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {}

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        final GL2 gl = drawable.getGL().getGL2();
        if (height <= 0) height = 1;
        float aspect = (float) width / height;

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0, aspect, 1.0, 20.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();

        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        // Side view camera
        glu.gluLookAt(2.0, 1.0, 4.0,  // eye
                      0.0, 0.3, 0.0,  // center
                      0.0, 1.0, 0.0); // up

        drawBird(gl);
    }

    private void drawBird(GL2 gl) {
        GLUquadric quad = glu.gluNewQuadric();

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.3f, 0f); // Position the bird

        // === BODY (Sphere) ===
        gl.glColor3f(0.7f, 0.4f, 0.1f); // Brownish
        gl.glPushMatrix();
        gl.glScalef(1.2f, 0.9f, 0.9f);  // Oval body shape
        glu.gluSphere(quad, 0.3, 32, 32);
        gl.glPopMatrix();

        // === HEAD (Smaller Sphere) ===
        gl.glPushMatrix();
        gl.glTranslatef(0.35f, 0.15f, 0.0f); // to the right of body
        glu.gluSphere(quad, 0.15, 32, 32);
        gl.glPopMatrix();

        // === BEAK (Cone) ===
        gl.glColor3f(1.0f, 0.8f, 0.1f); // Yellow
        gl.glPushMatrix();
        gl.glTranslatef(0.5f, 0.15f, 0.0f); // in front of head
        gl.glRotatef(90f, 0f, 1f, 0f); // point to side
        glu.gluCylinder(quad, 0.03, 0.0, 0.12, 16, 1);
        gl.glPopMatrix();

        // === EYE (Small Sphere) ===
        gl.glColor3f(0f, 0f, 0f); // Black
        gl.glPushMatrix();
        gl.glTranslatef(0.40f, 0.2f, 0.11f); // slight to the front-right
        glu.gluSphere(quad, 0.025, 16, 16);
        gl.glPopMatrix();
        
        // === LEFT WING ===
        gl.glColor3f(0.3f, 0.3f, 0.7f); // Bluish
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.0f, 0.3f);
        gl.glRotatef(45, 1, 0, 0);
        gl.glBegin(GL2.GL_TRIANGLES);
        gl.glVertex3f(0.0f, 0.0f, 0.0f);
        gl.glVertex3f(-0.4f, 0.2f, 0.0f);
        gl.glVertex3f(-0.4f, -0.2f, 0.0f);
        gl.glEnd();
        gl.glPopMatrix();

        // === RIGHT WING ===
        gl.glColor3f(0.3f, 0.3f, 0.7f);
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.0f, -0.3f);
        gl.glRotatef(-45, 1, 0, 0);
        gl.glBegin(GL2.GL_TRIANGLES);
        gl.glVertex3f(0.0f, 0.0f, 0.0f);
        gl.glVertex3f(-0.4f, 0.2f, 0.0f);
        gl.glVertex3f(-0.4f, -0.2f, 0.0f);
        gl.glEnd();
        gl.glPopMatrix();

        // === TAIL (Triangles) ===
        gl.glColor3f(0.5f, 0.3f, 0.2f);
        gl.glBegin(GL2.GL_TRIANGLES);
        gl.glVertex3f(-0.35f, 0.0f, 0.0f);
        gl.glVertex3f(-0.55f, 0.1f, 0.1f);
        gl.glVertex3f(-0.55f, -0.1f, -0.1f);
        gl.glEnd();

        gl.glPopMatrix();
        glu.gluDeleteQuadric(quad);
    }
}

