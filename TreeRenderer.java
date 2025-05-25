import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import javax.swing.*;

public class TreeRenderer implements GLEventListener {

    public static void main(String[] args) {
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        GLCanvas canvas = new GLCanvas(capabilities);
        TreeRenderer renderer = new TreeRenderer();
        canvas.addGLEventListener(renderer);
        canvas.setSize(600, 400);

        JFrame frame = new JFrame("Tree Scene");
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
        gl.glClearColor(0.5f, 0.8f, 1.0f, 1.0f); // sky blue
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {}

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        drawTree(gl, -0.6f); // left tree
        drawTree(gl, 0.6f);  // right tree
    }

    private void drawTree(GL2 gl, float offsetX) {
        // draw trunk with gradient (left side dark, right side light)
        float trunkTop = -0.2f;
        float trunkBottom = -0.6f;
        float trunkHalfWidth = 0.05f;

        // left half - darker brown
        gl.glColor3f(0.4f, 0.2f, 0.0f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(offsetX - trunkHalfWidth, trunkBottom);
        gl.glVertex2f(offsetX, trunkBottom);
        gl.glVertex2f(offsetX, trunkTop);
        gl.glVertex2f(offsetX - trunkHalfWidth, trunkTop);
        gl.glEnd();

        // right half - lighter brown
        gl.glColor3f(0.7f, 0.4f, 0.1f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(offsetX, trunkBottom);
        gl.glVertex2f(offsetX + trunkHalfWidth, trunkBottom);
        gl.glVertex2f(offsetX + trunkHalfWidth, trunkTop);
        gl.glVertex2f(offsetX, trunkTop);
        gl.glEnd();

        // draw 3D layered leaves
        drawTriangle3D(gl, offsetX, -0.2f, 0.4f, 0.3f, new float[]{0.0f, 0.5f, 0.0f}, new float[]{0.0f, 0.7f, 0.0f});
        drawTriangle3D(gl, offsetX, 0.05f, 0.3f, 0.22f, new float[]{0.0f, 0.7f, 0.0f}, new float[]{0.0f, 0.9f, 0.0f});
        drawTriangle3D(gl, offsetX, 0.25f, 0.25f, 0.16f, new float[]{0.0f, 0.9f, 0.0f}, new float[]{0.3f, 1.0f, 0.3f});
    }

    private void drawTriangle3D(GL2 gl, float centerX, float baseY, float height, float halfWidth, float[] darkColor, float[] lightColor) {
        // left half
        gl.glColor3f(darkColor[0], darkColor[1], darkColor[2]);
        gl.glBegin(GL2.GL_TRIANGLES);
        gl.glVertex2f(centerX - halfWidth, baseY);
        gl.glVertex2f(centerX, baseY + height);
        gl.glVertex2f(centerX, baseY);
        gl.glEnd();

        // right half
        gl.glColor3f(lightColor[0], lightColor[1], lightColor[2]);
        gl.glBegin(GL2.GL_TRIANGLES);
        gl.glVertex2f(centerX, baseY);
        gl.glVertex2f(centerX + halfWidth, baseY);
        gl.glVertex2f(centerX, baseY + height);
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
