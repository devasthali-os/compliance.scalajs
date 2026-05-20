package com.pratyabhi.compliance.dom

import org.scalajs.dom
import org.scalajs.dom.{Element, HTMLElement, Node}

object DomBuilder:

  def el[A <: Element](tag: String)(f: A => Unit = (a: A) => ()): A =
    val node = dom.document.createElement(tag).asInstanceOf[A]
    f(node)
    node

  def cls(e: Element, name: String): Unit =
    e.asInstanceOf[HTMLElement].className = name

  def text(content: String): org.scalajs.dom.Text =
    dom.document.createTextNode(content)

  def append(parent: Node, children: Node*): Unit =
    children.foreach(parent.appendChild)

  def clear(node: Element): Unit =
    while node.firstChild ne null do node.removeChild(node.firstChild)

  def onClick(el: Element, handler: () => Unit): Unit =
    el.addEventListener("click", (_: dom.Event) => handler())

  def setAttr(el: Element, name: String, value: String): Unit =
    el.setAttribute(name, value)

  def style(el: Element, pairs: (String, String)*): Unit =
    val html = el.asInstanceOf[HTMLElement]
    pairs.foreach { case (k, v) => html.style.setProperty(k, v) }
