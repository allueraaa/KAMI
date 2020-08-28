package me.zeroeightsix.kami.util

import me.zeroeightsix.kami.mc
import me.zeroeightsix.kami.multiplyMatrix
import me.zeroeightsix.kami.toScreen
import net.minecraft.client.util.math.Vector4f
import net.minecraft.util.math.Matrix4f
import net.minecraft.util.math.Quaternion
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d

object VectorMath {

    /**
     * Projects a 3D world coordinate onto the near plane (screen).
     *
     * @return A vector with 4 components: the `x` and `y` are screen-coordinates, and the `w` value depicts the distance of the world coordinate to the camera.
     */
    @JvmStatic
    fun project3Dto2D(
        position: Vec3d,
        modelMatrix: Matrix4f,
        projectionMatrix: Matrix4f
    ): Vector4f? {
        // Let's treat Quaternion as a 4x1 for convenience

        val quaternion = Quaternion(
            position.x.toFloat(),
            position.y.toFloat(),
            position.z.toFloat(),
            1.0F
        )

        val out = projectionMatrix.multiplyMatrix(modelMatrix.multiplyMatrix(quaternion))

        // will be <0f if the coordinate was 'behind' the camera, thus returning a mirrored coordinate
        if (out.w <= 0.0f) {
            return null
        }

        val screen = out.toScreen()
        val x = screen.x * mc.window.width
        val y = screen.y * mc.window.height

        return if (x.isInfinite() || y.isInfinite()) {
            null
        } else Vector4f(x, y, screen.z, 1f / (screen.w * 2f))
    }

    @JvmStatic
    fun divideVec2f(vec: Vec2f?, factor: Float): Vec2f? {
        if (vec == null) return null
        return Vec2f(vec.x / factor, vec.y / factor)
    }

}