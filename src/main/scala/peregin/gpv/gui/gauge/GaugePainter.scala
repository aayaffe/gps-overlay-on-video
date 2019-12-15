package peregin.gpv.gui.gauge

import scala.swing._
import java.awt.{RenderingHints, Color, Font, Dimension}
import peregin.gpv.model.{Sonda, InputValue}


trait GaugePainter {

  lazy val gaugeFont = new Font("Verdana", Font.PLAIN, 12)
  private var currentInput: Option[InputValue] = None
  private var debugging = false
  private var displayUnits: String = ""

  def desiredSize = new Dimension(75, 75)

  def paint(g: Graphics2D, w: Int, h: Int): Unit = {
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
    g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE)

    if (debugging) {
      g.setColor(Color.yellow)
      g.drawRoundRect(1, 1, w - 2, h - 2, 5, 5)
    }
  }

  final def paint(g: Graphics2D, w: Int, h: Int, sonda: Sonda): Unit = {
    sample(sonda)
    paint(g, w, h)
  }

  // each implementation should extract the desired input data from the sonda
  // e.g. speed gauge extracts speed input, etc.
  def sample(sonda: Sonda): Unit

  // each implementation should provide the default values used for testing or to show a sample in the gauges' list
  def defaultInput: InputValue

  def input: InputValue = currentInput.getOrElse(defaultInput)
  def input_= (v: InputValue): Unit = currentInput = Some(v)

  def debug: Boolean = debugging
  def debug_= (v: Boolean): Unit = debugging = v

  def units: String = displayUnits
  def units_= (v: String): Unit = displayUnits = v

  def textWidthShadow(g: Graphics2D, text: String, x: Double, y: Double, c: Color = Color.yellow): Unit = {
    val ix = x.toInt
    val iy = y.toInt
    g.setColor(Color.black)
    g.drawString(text, ix + 1, iy + 1)
    g.setColor(c)
    g.drawString(text, ix, iy)
  }

  def colorBasedOnInput: Color = input match {
    case _ if input.isInTop(10) => Color.red
    case _ if input.isInTop(20) => Color.yellow
    case _ if input.isInTop(50) => Color.green
    case _ => Color.gray
  }
}
