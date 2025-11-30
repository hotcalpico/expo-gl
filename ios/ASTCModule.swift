import ExpoModulesCore
// import Metal

public class ASTCModule: Module {
  public func definition() -> ModuleDefinition {
    Name("ExpoGLASTC")

    Function("isASTCSupported") {
      // guard let device = MTLCreateSystemDefaultDevice() else {
      //   return false
      // }
      // return device.supportsFeatureSet(.iOS_GPUFamily2_v1)

      // ASTC をサポートしているのは iPhone6 以降。
      // それを踏まえ、iPhone5 以前はアプリのサポート対象外として無視する前提で、判定をスキップするようにした。
      // 理由としては、下手に判定処理を挟む方が性能・バグの発生リスク・メンテナンスのコストが悪化すると考えたため。
      return true;
    }
  }
}
