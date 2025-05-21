/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Park;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.FPSAnimator;
import javax.swing.*;

public class CatRenderer implements GLEventListener {

    private final GLU glu = new GLU();
    private final GLUT glut = new GLUT();
    private float tailAngle = 0.0f;
    private boolean tailSwingingForward = true;

    public static void main(String[] args) {
        JFrame frame = new JFrame("3D Cat in JOGL");
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        GLCanvas canvas = new GLCanvas(capabilities);

        CatRenderer renderer = new CatRenderer();
        canvas.addGLEventListener(renderer);
        canvas.setSize(800, 600);

        frame.getContentPane().add(canvas);
        frame.setSize(frame.getContentPane().getPreferredSize());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        FPSAnimator animator = new FPSAnimator(canvas, 60);
        animator.start();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);

        float[] ambientLight = {0.3f, 0.3f, 0.3f, 1.0f};
        float[] diffuseLight = {0.8f, 0.8f, 0.8f, 1.0f};
        float[] lightPosition = {5f, 5f, 10f, 1.0f};

        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientLight, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuseLight, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPosition, 0);

        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);
        gl.glClearColor(0.7f, 0.8f, 1.0f, 1.0f); // Sky blue
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        if (height == 0) {
            height = 1;
        }
        float aspect = (float) width / height;

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0, aspect, 1.0, 100.0);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        // Camera looking at the front of the cat
        glu.gluLookAt(
                0, 2, 6, // eye position
                0, 1, 0, // center point the camera is looking at
                0, 1, 0 // up vector
        );

        gl.glColor3f(1, 1, 1); // reset color to white
        drawCat(gl);

        // Animate tail swing
        if (tailSwingingForward) {
            tailAngle += 1.0f;
            if (tailAngle > 30) {
                tailSwingingForward = false;
            }
        } else {
            tailAngle -= 1.0f;
            if (tailAngle < -30) {
                tailSwingingForward = true;
            }
        }

    }

    private void drawCat(GL2 gl) {
        // Body
        gl.glPushMatrix();
        gl.glTranslatef(0, 0.6f, 0);
        gl.glRotatef(90, 0, 1, 0);
        gl.glScalef(1.5f, 1f, 1f);
        glut.glutSolidSphere(0.9, 32, 32);
        gl.glPopMatrix();

        // Head
        gl.glPushMatrix();
        gl.glTranslatef(0, 2f, 0);
        glut.glutSolidSphere(0.6, 32, 32);

        // Eyes
        for (float x : new float[]{-0.25f, 0.25f}) {
            gl.glPushMatrix();
            gl.glColor3f(0, 0, 0); // Black eyes
            gl.glTranslatef(x, 0.1f, 0.55f);
            glut.glutSolidSphere(0.08, 10, 10);
            gl.glPopMatrix();
        }

        // Nose - upside down triangle
        gl.glPushMatrix();
        gl.glColor3f(1f, 0.6f, 0.7f); // Light pink nose
        gl.glTranslatef(0f, -0.1f, 0.6f);  // Position nose on face

        gl.glBegin(GL2.GL_TRIANGLES);
        // Left top vertex
        gl.glVertex3f(-0.05f, 0f, 0f);
        // Right top vertex
        gl.glVertex3f(0.05f, 0f, 0f);
        // Bottom tip vertex (pointing down)
        gl.glVertex3f(0f, -0.08f, 0f);
        gl.glEnd();

        gl.glPopMatrix();

        gl.glColor3f(1f, 1f, 1f); // Reset color to white
        gl.glPopMatrix(); // End head

        // Ears
        float earOffset = 0.4f;
        for (float x : new float[]{-earOffset, earOffset}) {
            gl.glPushMatrix();
            gl.glTranslatef(x, 2.4f, 0);

            gl.glRotatef(-90, 1, 0, 0);  // point cone upward

            if (x < 0) {
                gl.glRotatef(-37, 0, 1, 0);  // Left ear tilt left
            } else {
                gl.glRotatef(37, 0, 1, 0);   // Right ear tilt right
            }

            glut.glutSolidCone(0.2, 0.4, 20, 20);
            gl.glPopMatrix();
        }

        // Legs
        float[][] legPositions = {
            {-0.5f, -0.4f, 0.5f}, {0.5f, -0.4f, 0.5f},
            {-0.5f, -0.4f, -0.5f}, {0.5f, -0.4f, -0.5f}
        };
        for (float[] pos : legPositions) {
            gl.glPushMatrix();
            gl.glTranslatef(pos[0], pos[1], pos[2]);
            gl.glRotatef(-90, 1, 0, 0);
            glut.glutSolidCylinder(0.2, 1.0, 20, 20);
            gl.glPopMatrix();
        }

        // Tail
        gl.glPushMatrix();
        gl.glTranslatef(0.9f, 0f, -1.1f);        // Move to tail base
        gl.glRotatef(tailAngle, 0, 1, 0);      // Animate left-right swing
        gl.glRotatef(120, 1, 1, 0);            // Fixed angle for tail orientation
        glut.glutSolidCylinder(0.1, 1.2, 20, 20);
        gl.glPopMatrix();

        // Whiskers
        gl.glPushMatrix();
        gl.glColor3f(0, 0, 0);

        // Position at the nose level
        gl.glTranslatef(0f, 1.88f, 0.62f);

        // Left whiskers
        for (float y = 0f; y <= 0.1f; y += 0.05f) {
            gl.glPushMatrix();
            gl.glTranslatef(-0.4f, y, 0);
            gl.glRotatef(20, 0, 1, 0);
            gl.glRotatef(190, 1, 0, 0);
            glut.glutSolidCylinder(0.005, 0.5, 8, 1);
            gl.glPopMatrix();
        }

        // Right whiskers
        for (float y = 0f; y <= 0.1f; y += 0.05f) {
            gl.glPushMatrix();
            gl.glTranslatef(0.4f, y, 0);
            gl.glRotatef(-20, 0, 1, 0);
            gl.glRotatef(190, 1, 0, 0);
            glut.glutSolidCylinder(0.005, 0.5, 8, 1);
            gl.glPopMatrix();
        }

        gl.glPopMatrix();

    }
}
