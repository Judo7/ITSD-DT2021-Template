// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/zouzhongdou/Downloads/ITSD-DT2021-Template/conf/routes
// @DATE:Fri Jun 25 23:24:49 BST 2021


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
