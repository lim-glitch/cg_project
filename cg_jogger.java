/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jogl_test;

import com.jogamp.opengl.GLEventListener;

/**
 *
 * @author Acer
 */
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

import javax.swing.*;

public class cg_jogger implements GLEventListener {

    private float time = 0f;
    private GLU glu = new GLU();

    public static void main(String[] args) {
        JFrame frame = new JFrame("3D Jogging Human - Side View Facing Right");
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(profile);
        GLCanvas canvas = new GLCanvas(caps);

        canvas.addGLEventListener(new cg_jogger());
        frame.add(canvas);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        new Timer(16, e -> canvas.display()).start(); // ~60 FPS
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);

        float[] ambient = {0.2f, 0.2f, 0.2f, 1f};
        float[] diffuse = {0.8f, 0.8f, 0.8f, 1f};
        float[] position = {1f, 1f, 2f, 0f};

        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuse, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, position, 0);

        gl.glClearColor(1f, 1f, 1f, 1f); // white background
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {}

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        GL2 gl = drawable.getGL().getGL2();
        if (h == 0) h = 1;
        float aspect = (float) w / h;

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0, aspect, 1.0, 20.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        time += 0.1f;
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        // Camera from side, looking toward center
        glu.gluLookAt(3, 1, 5,   0, 1, 0,   0, 1, 0);

        drawHuman(gl);
    }

    private void drawHuman(GL2 gl) {
        GLUquadric quad = glu.gluNewQuadric();

        float bounce = (float) Math.abs(Math.sin(time)) * 0.05f; // gentle up-down bounce

        float legSwing = (float) Math.sin(time) * 30f;

        gl.glPushMatrix();
        gl.glTranslatef(0f, bounce, 0f);

        // === BODY ===
        gl.glColor3f(0f, 0f, 1.0f);
        gl.glPushMatrix();
        gl.glTranslatef(0f, 1.5f, 0f);
        gl.glScalef(0.4f, 1f, 0.2f);
        glu.gluSphere(quad, 0.4, 32, 32);
        gl.glPopMatrix();

        // === HEAD ===
        gl.glColor3f(1f, 0.8f, 0.6f);
        gl.glPushMatrix();
        gl.glTranslatef(0f, 2.0f, 0f);
        glu.gluSphere(quad, 0.15, 32, 32);

        // === eyes facing right (if viewer view is left)
        gl.glColor3f(0f, 0f, 0f);
        gl.glPushMatrix();
        gl.glTranslatef(-0.04f, 0.03f, 0.14f);
        glu.gluSphere(quad, 0.015, 10, 10);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.04f, 0.03f, 0.14f);
        glu.gluSphere(quad, 0.015, 10, 10);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // === ARMS (static) ===
        drawLimb(gl, glu, -0.25f, 1.25f, 0f, true);
        drawLimb(gl, glu, 0.25f, 1.25f, 0f, true);

        // === LEGS (animated) ===
        drawLeg(gl, glu, -0.08f, 1.2f, -legSwing);
        drawLeg(gl, glu, 0.08f, 1.2f, legSwing);
 
        gl.glPopMatrix();
        glu.gluDeleteQuadric(quad);
    }

    private void drawLimb(GL2 gl, GLU glu, float x, float y, float angle, boolean isArm) {
        float radius = isArm ? 0.07f : 0.08f;
        float length = isArm ? 0.4f : 0.5f;

        gl.glPushMatrix();
        gl.glTranslatef(x, y, 0f);
        gl.glRotatef(angle, 0f, 0f, 1f); // Arms rotate in Z (sideways, but static here)
        gl.glColor3f(0.6f, 0.4f, 0.3f);
        gl.glRotatef(-90f, 1f, 0f, 0f);
        GLUquadric quad = glu.gluNewQuadric();
        glu.gluCylinder(quad, radius, radius, length, 16, 16);
        gl.glPopMatrix();
    }

    private void drawLeg(GL2 gl, GLU glu, float x, float y, float angle) {
        float radius = 0.08f;
        float length = 0.6f;

        gl.glPushMatrix();
        gl.glTranslatef(x, y, 0f);

    // ? Rotate around Z for left-right swing in side view
        gl.glRotatef(angle, 0f, 0f, 1f);

    // Then align leg vertically down
        gl.glRotatef(90f, 1f, 0f, 0f);

        gl.glColor3f(0.3f, 0.3f, 0.6f); // blue pants
        GLUquadric quad = glu.gluNewQuadric();
        glu.gluCylinder(quad, radius, radius, length, 16, 16);
        glu.gluDeleteQuadric(quad);

        gl.glPopMatrix();
    }
}
