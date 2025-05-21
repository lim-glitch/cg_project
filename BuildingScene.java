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

public class BuildingScene implements GLEventListener {

    private final GLUT glut = new GLUT();

    public static void main(String[] args) {
        JFrame frame = new JFrame("3D Buildings Without Texture - JOGL");
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        GLCanvas canvas = new GLCanvas(capabilities);

        BuildingScene scene = new BuildingScene();
        canvas.addGLEventListener(scene);
        canvas.setSize(800, 600);

        frame.getContentPane().add(canvas);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        FPSAnimator animator = new FPSAnimator(canvas, 60);
        animator.start();
    }

    private GLU glu = new GLU();

    @Override
public void init(GLAutoDrawable drawable) {
    GL2 gl = drawable.getGL().getGL2();
    gl.glEnable(GL2.GL_DEPTH_TEST);
    gl.glClearColor(0.5f, 0.8f, 1.0f, 1.0f); // Sky blue

    // Enable lighting
    gl.glEnable(GL2.GL_LIGHTING);
    gl.glEnable(GL2.GL_LIGHT0);

    // Light properties
    float[] lightAmbient = { 0.2f, 0.2f, 0.2f, 1.0f };
    float[] lightDiffuse = { 0.8f, 0.8f, 0.8f, 1.0f };
    float[] lightPosition = { 0.0f, 10.0f, 10.0f, 1.0f };

    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmbient, 0);
    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDiffuse, 0);
    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPosition, 0);

    // Material properties
    float[] materialSpecular = { 1.0f, 1.0f, 1.0f, 1.0f };
    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, materialSpecular, 0);
    gl.glMateriali(GL2.GL_FRONT, GL2.GL_SHININESS, 50);

    // Allow glColor* to set material color
    gl.glEnable(GL2.GL_COLOR_MATERIAL);
    gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);
}


    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        // Camera directly in front of buildings
        gl.glTranslatef(0f, -5f, -30f);

        // Draw buildings
        drawBuilding(gl, -9f, 0f, 0f, 3f, 4f, 3f, new float[]{0.0f, 0.6f, 0.9f});
        drawBuilding(gl, -6.8f, 0f, 0f, 3f, 8f, 2f, new float[]{0.6f, 0.6f, 0.9f});
        drawBuilding(gl, -3.7f, 0f, 0f, 3f, 6f, 3f, new float[]{0.9f, 0.4f, 0.4f});
        drawBuilding(gl, -1f, 0f, 0f, 2f, 10f, 2f, new float[]{0.4f, 0.9f, 0.4f});
        drawBuilding(gl, 1.5f, 0f, 0f, 3f, 7f, 1.5f, new float[]{0.8f, 0.8f, 0.2f});
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        if (height <= 0) {
            height = 1;
        }

        float aspect = (float) width / height;
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0, aspect, 1.0, 100.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    private void drawBuilding(GL2 gl, float x, float y, float z, float width, float height, float depth, float[] color) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y + height / 2f, z);

        gl.glScalef(width, height, depth);

        gl.glColor3fv(color, 0);
        drawLitCube(gl);
        
        gl.glPopMatrix();

        // Draw windows on the front face
        drawWindows(gl, x, y, z + depth / 2f + 0.01f, width, height, color);
    }

    private void drawWindows(GL2 gl, float x, float y, float z, float width, float height, float[] buildingColor) {
        gl.glColor3f(0.2f, 0.4f, 0.8f); // window color (blueish)

        int rows = (int) (height * 1.2);
        int cols = (int) (width * 2);
        float windowWidth = width / cols * 0.6f;
        float windowHeight = height / rows * 0.6f;
        float spacingX = width / cols;
        float spacingY = height / rows;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                float winX = x - width / 2 + j * spacingX + spacingX / 2;
                float winY = y + i * spacingY + spacingY / 2;

                gl.glBegin(GL2.GL_QUADS);
                gl.glVertex3f(winX - windowWidth / 2, winY - windowHeight / 2, z);
                gl.glVertex3f(winX + windowWidth / 2, winY - windowHeight / 2, z);
                gl.glVertex3f(winX + windowWidth / 2, winY + windowHeight / 2, z);
                gl.glVertex3f(winX - windowWidth / 2, winY + windowHeight / 2, z);
                gl.glEnd();
            }
        }
    }

    private void drawLitCube(GL2 gl) {
        float[][] vertices = {
            {-0.5f, -0.5f, 0.5f}, {0.5f, -0.5f, 0.5f},
            {0.5f, 0.5f, 0.5f}, {-0.5f, 0.5f, 0.5f},
            {-0.5f, -0.5f, -0.5f}, {0.5f, -0.5f, -0.5f},
            {0.5f, 0.5f, -0.5f}, {-0.5f, 0.5f, -0.5f}
        };

        int[][] faces = {
            {0, 1, 2, 3}, // front
            {1, 5, 6, 2}, // right
            {5, 4, 7, 6}, // back
            {4, 0, 3, 7}, // left
            {3, 2, 6, 7}, // top
            {4, 5, 1, 0} // bottom
        };

        float[][] normals = {
            {0, 0, 1}, {1, 0, 0}, {0, 0, -1},
            {-1, 0, 0}, {0, 1, 0}, {0, -1, 0}
        };

        for (int i = 0; i < faces.length; i++) {
            gl.glBegin(GL2.GL_QUADS);
            gl.glNormal3fv(normals[i], 0);
            for (int idx : faces[i]) {
                gl.glVertex3fv(vertices[idx], 0);
            }
            gl.glEnd();
        }
    }

}
