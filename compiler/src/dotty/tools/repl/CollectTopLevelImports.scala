package dotty.tools.repl

import dotty.tools.dotc.ast.Trees._
import dotty.tools.dotc.ast.tpd
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.core.Phases.Phase

/** A phase that collects user defined top level imports.
 *
 *  These imports must be collected as typed trees and therefore
 *  after Typer.
 */
class CollectTopLevelImports extends Phase {
  import tpd._

  def phaseName: String = "collectTopLevelImports"

  private[this] var myImports: List[Import] = _
  def imports: List[Import] = myImports

  def run(implicit ctx: Context): Unit = {
    def topLevelImports(tree: Tree) = {
      val PackageDef(_, _ :: TypeDef(_, rhs: Template) :: Nil) = tree
      rhs.body.collect { case tree: Import => tree }
    }

    val tree = ctx.compilationUnit.tpdTree
    myImports = topLevelImports(tree)
  }
}
