package main.java;

import java.net.URL;
import java.util.List;

public class MarkdownConverter {

    private URL urlBase;

    public String convertir(HtmlParser.Node documento, URL url) {
        StringBuilder resultado;

        urlBase = url;
        resultado = new StringBuilder();

        procesarHijos(documento, resultado, 0);

        return resultado.toString().trim() + "\n";
    }

    private void procesarHijos(HtmlParser.Node nodo, StringBuilder resultado, int nivel) {
        Object hijo;
        HtmlParser.Node elemento;
        String texto;

        for (Object objeto : nodo.children) {
            if (objeto instanceof String) {
                texto = (String) objeto;
                resultado.append(texto);
            } else {
                elemento = (HtmlParser.Node) objeto;
                procesarElemento(elemento, resultado, nivel);
            }
        }
    }

    private void procesarElemento(HtmlParser.Node nodo, StringBuilder resultado, int nivel) {
        String etiqueta;
        String contenido;
        String destino;
        String alt;
        StringBuilder interior;
        HtmlParser.Node hijo;
        int i;

        etiqueta = nodo.tag;
        interior = new StringBuilder();

        if (etiqueta.equals("script") || etiqueta.equals("style") ||
                etiqueta.equals("head") || etiqueta.equals("noscript")) {
            return;
        }

        if (etiqueta.equals("ul") || etiqueta.equals("ol")) {
            procesarLista(nodo, resultado, nivel);
            return;
        }

        if (etiqueta.equals("img")) {
            destino = resolverURL(nodo.attributes.get("src"));
            alt = nodo.attributes.getOrDefault("alt", "");
            resultado.append("![").append(alt).append("](").append(destino).append(")");
            return;
        }

        if (etiqueta.equals("br")) {
            resultado.append("  \n");
            return;
        }

        if (etiqueta.equals("hr")) {
            agregarBloque(resultado, "---");
            return;
        }

        procesarHijos(nodo, interior, nivel);
        contenido = interior.toString().trim();

        if (contenido.isEmpty()) {
            return;
        }

        switch (etiqueta) {
            case "h1":
                agregarBloque(resultado, "# " + contenido);
                break;
            case "h2":
                agregarBloque(resultado, "## " + contenido);
                break;
            case "h3":
                agregarBloque(resultado, "### " + contenido);
                break;
            case "h4":
                agregarBloque(resultado, "#### " + contenido);
                break;
            case "h5":
                agregarBloque(resultado, "##### " + contenido);
                break;
            case "h6":
                agregarBloque(resultado, "###### " + contenido);
                break;
            case "p":
            case "div":
            case "section":
            case "article":
                agregarBloque(resultado, contenido);
                break;
            case "strong":
            case "b":
                resultado.append("**").append(contenido).append("**");
                break;
            case "em":
            case "i":
                resultado.append("*").append(contenido).append("*");
                break;
            case "a":
                destino = resolverURL(nodo.attributes.get("href"));

                if (destino.isEmpty()) {
                    resultado.append(contenido);
                } else {
                    resultado.append("[").append(contenido).append("](").append(destino).append(")");
                }
                break;
            case "blockquote":
                agregarBloque(resultado, "> " + contenido.replace("\n", "\n> "));
                break;
            case "code":
                resultado.append("`").append(contenido).append("`");
                break;
            case "pre":
                agregarBloque(resultado, "```\n" + interior.toString().trim() + "\n```");
                break;
            default:
                resultado.append(interior);
                break;
        }
    }

    private void procesarLista(HtmlParser.Node lista, StringBuilder resultado, int nivel) {
        HtmlParser.Node elemento;
        HtmlParser.Node hijo;
        StringBuilder contenido;
        String marcador;
        String texto;
        String etiqueta;
        int numero;
        int espacios;

        numero = 1;
        espacios = nivel * 2;

        for (Object objeto : lista.children) {
            if (!(objeto instanceof HtmlParser.Node)) {
                continue;
            }

            elemento = (HtmlParser.Node) objeto;

            if (!elemento.tag.equals("li")) {
                continue;
            }

            contenido = new StringBuilder();

            for (Object objetoHijo : elemento.children) {
                if (objetoHijo instanceof String) {
                    contenido.append(objetoHijo);
                } else {
                    hijo = (HtmlParser.Node) objetoHijo;

                    if (hijo.tag.equals("ul") || hijo.tag.equals("ol")) {
                        continue;
                    }

                    procesarElemento(hijo, contenido, nivel);
                }
            }

            marcador = lista.tag.equals("ol") ? numero + ". " : "- ";
            texto = contenido.toString().trim();

            resultado.append(" ".repeat(espacios))
                    .append(marcador)
                    .append(texto)
                    .append("\n");

            for (Object objetoHijo : elemento.children) {
                if (objetoHijo instanceof HtmlParser.Node) {
                    hijo = (HtmlParser.Node) objetoHijo;
                    etiqueta = hijo.tag;

                    if (etiqueta.equals("ul") || etiqueta.equals("ol")) {
                        procesarLista(hijo, resultado, nivel + 1);
                    }
                }
            }

            numero++;
        }
    }

    private void agregarBloque(StringBuilder resultado, String contenido) {
        if (resultado.length() > 0 &&
                resultado.charAt(resultado.length() - 1) != '\n') {
            resultado.append("\n");
        }

        resultado.append("\n").append(contenido).append("\n\n");
    }

    private String resolverURL(String referencia) {
        URL destino;

        if (referencia == null || referencia.isEmpty()) {
            return "";
        }

        try {
            destino = new URL(urlBase, referencia);
            return destino.toString();
        } catch (Exception e) {
            return referencia;
        }
    }
}
