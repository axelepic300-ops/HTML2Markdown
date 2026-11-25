package main.java;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

public class HtmlParser {

    public static class Node {
        public String tag;
        public Map<String, String> attributes;
        public List<Object> children;

        public Node(String tag) {
            this.tag = tag;
            this.attributes = new HashMap<>();
            this.children = new ArrayList<>();
        }
    }

    private Node root;
    private Deque<Node> pila;

    public Node analizar(String html) throws IOException {
        Node documento;
        ParserDelegator parser;
        HTMLEditorKit.ParserCallback callback;

        root = new Node("document");
        pila = new ArrayDeque<>();
        pila.push(root);

        documento = root;
        parser = new ParserDelegator();

        callback = new HTMLEditorKit.ParserCallback() {

            @Override
            public void handleStartTag(HTML.Tag tag, MutableAttributeSet atributos, int posicion) {
                Node nodo;
                String nombre;

                nombre = tag.toString();
                nodo = crearNodo(nombre, atributos);
                pila.peek().children.add(nodo);

                if (!esEtiquetaVacia(nombre)) {
                    pila.push(nodo);
                }
            }

            @Override
            public void handleEndTag(HTML.Tag tag, int posicion) {
                String nombre;
                nombre = tag.toString();

                while (pila.size() > 1) {
                    if (pila.pop().tag.equals(nombre)) {
                        break;
                    }
                }
            }

            @Override
            public void handleSimpleTag(HTML.Tag tag, MutableAttributeSet atributos, int posicion) {
                Node nodo;
                nodo = crearNodo(tag.toString(), atributos);
                pila.peek().children.add(nodo);
            }

            @Override
            public void handleText(char[] texto, int posicion) {
                pila.peek().children.add(new String(texto));
            }

            private Node crearNodo(String nombre, MutableAttributeSet atributos) {
                Node nodo;
                Object clave;
                Object valor;

                nodo = new Node(nombre);

                for (java.util.Enumeration<?> elementos = atributos.getAttributeNames();
                     elementos.hasMoreElements();) {

                    clave = elementos.nextElement();
                    valor = atributos.getAttribute(clave);

                    nodo.attributes.put(clave.toString(), valor.toString());
                }

                return nodo;
            }

            private boolean esEtiquetaVacia(String nombre) {
                return nombre.equals("br") ||
                        nombre.equals("img") ||
                        nombre.equals("hr") ||
                        nombre.equals("meta") ||
                        nombre.equals("link") ||
                        nombre.equals("input");
            }
        };

        parser.parse(new StringReader(html), callback, true);

        return documento;
    }
}
