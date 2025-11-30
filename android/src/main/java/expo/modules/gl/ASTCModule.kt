package expo.modules.gl

import android.opengl.EGL14
import android.opengl.EGLConfig
import android.opengl.GLES20
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

class ASTCModule : Module() {
  override fun definition() = ModuleDefinition {
    Name("ExpoGLASTC")

    Function("isASTCSupported") {
      return@Function detectASTC()
    }
  }

  private fun detectASTC(): Boolean {
    val display = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
    val version = IntArray(2)
    EGL14.eglInitialize(display, version, 0, version, 1)

    val attribList = intArrayOf(
      EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
      EGL14.EGL_NONE
    )

    val configs = arrayOfNulls<EGLConfig>(1)
    val numConfigs = IntArray(1)
    EGL14.eglChooseConfig(display, attribList, 0, configs, 0, 1, numConfigs, 0)
    val config = configs[0] ?: return false

    val ctxAttribs = intArrayOf(EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, EGL14.EGL_NONE)
    val context = EGL14.eglCreateContext(display, config, EGL14.EGL_NO_CONTEXT, ctxAttribs, 0)

    val surfaceAttribs = intArrayOf(EGL14.EGL_WIDTH, 1, EGL14.EGL_HEIGHT, 1, EGL14.EGL_NONE)
    val surface = EGL14.eglCreatePbufferSurface(display, config, surfaceAttribs, 0)

    EGL14.eglMakeCurrent(display, surface, surface, context)

    val ext = GLES20.glGetString(GLES20.GL_EXTENSIONS)
    val supported =
        ext?.contains("GL_KHR_texture_compression_astc_ldr") == true ||
        ext?.contains("GL_OES_texture_compression_astc") == true

    // Clean up
    EGL14.eglMakeCurrent(display, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT)
    EGL14.eglDestroySurface(display, surface)
    EGL14.eglDestroyContext(display, context)
    EGL14.eglTerminate(display)

    return supported
  }
}
