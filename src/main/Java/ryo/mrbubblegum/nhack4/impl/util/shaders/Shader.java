package ryo.mrbubblegum.nhack4.impl.util.shaders;

import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Shader {
    protected Map<String, Integer> uniformsMap;
    protected int program;

    public Shader(String string) {
        int n;
        int n2;
        try {
            InputStream inputStream = this.getClass().getResourceAsStream("/assets/minecraft/shaders/vertex.vert");
            n2 = this.createShader(IOUtils.toString(inputStream), 35633);
            IOUtils.closeQuietly(inputStream);
            InputStream inputStream2 = this.getClass().getResourceAsStream(String.valueOf("/assets/minecraft/shaders/fragment/" + string));
            n = this.createShader(IOUtils.toString(inputStream2), 35632);
            IOUtils.closeQuietly(inputStream2);
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
        if (n2 == 0 || n == 0) {
            return;
        }
        this.program = ARBShaderObjects.glCreateProgramObjectARB();
        if (this.program == 0) {
            return;
        }
        ARBShaderObjects.glAttachObjectARB(this.program, n2);
        ARBShaderObjects.glAttachObjectARB(this.program, n);
        ARBShaderObjects.glLinkProgramARB(this.program);
        ARBShaderObjects.glValidateProgramARB(this.program);
    }

    public void updateUniforms(float f) {
    }

    public void setUniform(String string, int n) {
        this.uniformsMap.put(string, n);
    }

    public void startShader(float f) {
        GL11.glPushMatrix();
        GL20.glUseProgram(this.program);
        if (this.uniformsMap == null) {
            this.uniformsMap = new HashMap<String, Integer>();
            this.setupUniforms();
        }
        this.updateUniforms(f);
    }

    public void stopShader() {
        GL20.glUseProgram(0);
        GL11.glPopMatrix();
    }

    public void setupUniform(String string) {
        this.setUniform(string, GL20.glGetUniformLocation(this.program, string));
    }

    public void setupUniforms() {
    }

    private int createShader(String string, int n) {
        int n2 = 0;
        try {
            n2 = ARBShaderObjects.glCreateShaderObjectARB(n);
            if (n2 == 0) {
                return 0;
            }
            ARBShaderObjects.glShaderSourceARB(n2, string);
            ARBShaderObjects.glCompileShaderARB(n2);
            if (ARBShaderObjects.glGetObjectParameteriARB(n2, 35713) == 0) {
                throw new RuntimeException(String.valueOf("Error creating shader: " + this.getLogInfo(n2)));
            }
            return n2;
        } catch (Exception exception) {
            ARBShaderObjects.glDeleteObjectARB(n2);
            throw exception;
        }
    }

    private String getLogInfo(int n) {
        return ARBShaderObjects.glGetInfoLogARB(n, ARBShaderObjects.glGetObjectParameteriARB(n, 35716));
    }

    public int getUniform(String string) {
        return this.uniformsMap.get(string);
    }
}