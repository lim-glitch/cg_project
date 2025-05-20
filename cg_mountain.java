/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jogl_test;

/**
 *
 * @author Acer
 */
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

import javax.swing.*;

public class cg_mountain implements GLEventListener {

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
    public void dispose(GLAutoDrawable drawable) {}

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        draw3DMountains(gl);
    }

    private void draw3DMountains(GL2 gl) {
        
    // Central Mountain - Use two triangles to simulate a 3D ridge
    gl.glBegin(GL2.GL_TRIANGLES);

    // Front left face - slightly darker for shadow
    gl.glColor3f(0.3f, 0.6f, 0.3f);
    gl.glNormal3f(-0.5f, 0.5f, 1);  // simulate lighting angle
    gl.glVertex3f(-0.6f, -0.4f, 0.0f);
    gl.glVertex3f(0.0f, 0.6f + noise(0), 0.05f);//slighly forward
    gl.glVertex3f(0.0f, -0.3f + noise(0), -0.05f);

    // Front right face - brighter
    gl.glColor3f(0.4f, 0.8f, 0.4f);
    gl.glNormal3f(0.5f, 0.5f, 1);
    gl.glVertex3f(0.0f, -0.4f + noise(2), -0.05f);
    gl.glVertex3f(0.0f, 0.55f+ noise(3), 0.05f);
    gl.glVertex3f(0.6f, -0.4f + noise(4), 0.0f);

    gl.glEnd();

    // Left smaller mountain - also with 2 faces
    gl.glBegin(GL2.GL_TRIANGLES);
    gl.glColor3f(0.25f, 0.5f, 0.25f);
    gl.glNormal3f(-0.5f, 0.5f, 1);
    gl.glVertex3f(-0.9f, -0.4f, 0.0f);
    gl.glVertex3f(-0.3f, 0.5f , 0.05f);
    gl.glVertex3f(0.1f, -0.4f + noise(6), -0.05f);

    gl.glColor3f(0.3f, 0.7f, 0.3f);
    gl.glNormal3f(0.5f, 0.5f, 1);
    gl.glVertex3f(-0.3f, -0.4f , 0.0f);
    gl.glVertex3f(-0.3f, 0.5f, 0.0f);
    gl.glVertex3f(0.1f, -0.4f, 0.0f);
    gl.glEnd();

    // Right mountain with more steep face
    gl.glBegin(GL2.GL_TRIANGLES);
    gl.glColor3f(0.35f, 0.6f, 0.35f);
    gl.glNormal3f(-0.6f, 0.4f, 1);
    gl.glVertex3f(0.3f, -0.4f + noise(7), 0.0f);
    gl.glVertex3f(0.7f, 0.4f + noise(8), 0.0f);
    gl.glVertex3f(0.7f, -0.4f + noise(9), 0.0f);

    gl.glColor3f(0.4f, 0.9f, 0.4f);
    gl.glNormal3f(0.6f, 0.4f, 1);
    gl.glVertex3f(0.7f, -0.4f, 0.0f);
    gl.glVertex3f(0.7f, 0.4f, 0.0f);
    gl.glVertex3f(1.0f, -0.4f, 0.0f);
    gl.glEnd();
}
    
    
    
    private float noise(int seed){
        double raw = Math.sin(seed*12.9898 + seed*78.233) *43758.5453;
        return (float)((raw - Math.floor(raw)) * 0.08 - 0.04);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(-1.0, 1.0, -1.0, 1.0, -1.0, 1.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    public static void main(String[] args) {
        final JFrame frame = new JFrame("Realistic 3D Mountains");
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        final GLCapabilities capabilities = new GLCapabilities(profile);
        final GLCanvas canvas = new GLCanvas(capabilities);
        cg_mountain scene = new cg_mountain();
        canvas.addGLEventListener(scene);
        frame.getContentPane().add(canvas);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        final Animator animator = new Animator(canvas);
        animator.start();
    }
}
