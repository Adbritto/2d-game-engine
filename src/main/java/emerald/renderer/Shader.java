package emerald.renderer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.*;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private int shaderProgramID;

    private String vertexSrc, fragmentSrc, filePath;

    public Shader(String filepath) {
        this.filePath = filepath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filePath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            // Identify first pattern after #type
            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\r\n", index);
            String firstPattern = source.substring(index, eol).trim();

            // Identify second pattern after #type
            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index,eol).trim();

            if (firstPattern.equals("vertex")) {
                vertexSrc = splitString[1];
            } else if (firstPattern.equals("fragment")) {
                fragmentSrc = splitString[1];
            } else {
                throw new IOException("Unexpected token '" + firstPattern + "'");
            }

            if (secondPattern.equals("vertex")) {
                vertexSrc = splitString[2];
            } else if (secondPattern.equals("fragment")) {
                fragmentSrc = splitString[2];
            } else {
                throw new IOException("Unexpected token '" + secondPattern + "'");
            }

        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Error: Could not open file for shader: '" + filepath + "'";
        }
    }

    public void compile() {
        int vertexID, fragmentID;

        // ===========================================================
        // Compile and link shaders
        // ===========================================================

        // Load and Compile vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // Pass shader src code to GPU
        glShaderSource(vertexID, vertexSrc);
        glCompileShader(vertexID);

        //Check for errors
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filePath + "'\n\tVertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID));
            assert false : "";
        }

        // Load and Compile fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass shader src code to GPU
        glShaderSource(fragmentID, fragmentSrc);
        glCompileShader(fragmentID);

        //Check for errors
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filePath + "'\n\tFragment shader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID));
            assert false : "";
        }

        // Link shaders and check for errors
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        // Check for linking errors
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filePath + "'\n\tShader linking failed.");
            System.out.println(glGetProgramInfoLog(shaderProgramID));
            assert false : "";
        }
    }

    public void use() {
        // Bind shader program
        glUseProgram(shaderProgramID);
    }

    public void detach() {
    glUseProgram(0);
    }

    public void uploadMat4f(String varName, Matrix4f mat4) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }
}
