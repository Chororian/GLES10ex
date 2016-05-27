package jp.ac.titech.itpro.sdl.gles10ex;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by kato on 2016/05/27.
 */
public class Prism implements SimpleRenderer.Obj {
    private FloatBuffer vbuf;
    private float x, y, z, l, h;
    private int n;

    public Prism(int n, float l, float h, float x, float y, float z) {
        if (n < 3) n = 3;
        if (l < 0) {
            l = -l;
        }else if (l == 0) {
            l = 1.0f;
        }
        if (h < 0) {
            h = -h;
        }else if (h == 0) {
            h = 1.0f;
        }

        float[] top, side, bottom;
        top = new float[(n + 2) * 3];
        side = new float[4 * n * 3];
        bottom = new float[(n + 2) * 3];
        vbuf = ByteBuffer.allocateDirect((((n + 2) * 2 + 4 * n) * 3) * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        top[0] = top[1] = bottom[0] = bottom[1] = 0.0f;
        top[2] = h / 2;
        bottom[2] = - h / 2;
        for (int i = 0; i < n; i++) {
            top[(i + 1) * 3] = bottom[(i + 1) * 3] = l * (float)Math.cos(i * (2 * Math.PI) / n);
            top[(i + 1) * 3 + 1] = bottom[(i + 1) * 3 + 1] = l * (float)Math.sin(i * (2 * Math.PI) / n);
            top[(i + 1) * 3 + 2] = h / 2;
            bottom[(i + 1) * 3 + 2] = - h / 2;
        }
        for(int i = 0; i < 3; i++) {
            top[(n + 1) * 3 + i] = top[3 + i];
            bottom[(n + 1) * 3 + i] = bottom[3 + i];
        }


        for (int i = 0; i < n; i++) {
            for(int j = 0; j < 3; j++) {
                side[12 * i + j] = top[(i + 1) * 3 + j];
                side[12 * i + 3 + j] = bottom[(i + 1) * 3 + j];
                side[12 * i + 6 + j] = top[(i + 2) * 3 + j];
                side[12 * i + 9 + j] = bottom[(i + 2) * 3 + j];
            }
        }

        vbuf.put(top);
        vbuf.put(side);
        vbuf.put(bottom);
        vbuf.position(0);
        this.x = x;
        this.y = y;
        this.z = z;
        this.h = h;
        this.l = l;
        this.n = n;
    }

    @Override
    public void draw(GL10 gl) {
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vbuf);

        gl.glNormal3f(0, 0, 1);
        gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, n + 2);

        for (int i = 0; i < n; i++) {
            gl.glNormal3f((float)Math.cos((i + 0.5) * (2 * Math.PI) / n), (float)Math.sin((i + 0.5) * (2 * Math.PI) / n), 0);
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, (n + 2) + i * 4, 4);
        }

        gl.glNormal3f(0, 0, -1);
        gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 5 * n + 2, n + 2);
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public float getZ() {
        return z;
    }
}
