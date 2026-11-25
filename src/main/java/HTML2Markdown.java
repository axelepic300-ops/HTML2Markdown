package main.java;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HTML2Markdown {

    public static void main(String[] args) {
        URL url;
        String html;
        String markdown;
        String nombreArchivo;
        HtmlParser parser;
        HtmlParser.Node documento;
        WebClient cliente;
        MarkdownConverter conversor;
        MarkdownWriter escritor;
        Path destino;

        if (args.length < 1 || args.length > 2) {
            System.out.println("Uso: java HTML2Markdown <URL> [archivo.md]");
            return;
        }

        try {
            url = new URL(args[0]);

            if (!url.getProtocol().equals("http") &&
                    !url.getProtocol().equals("https")) {
                throw new IllegalArgumentException("La URL debe utilizar HTTP o HTTPS.");
            }

            System.out.println("HTML2Markdown");
            System.out.println("------------------------------");
            System.out.println("URL: " + url);
            System.out.println("Conectando con el servidor...");

            cliente = new WebClient();
            html = cliente.descargarHTML(url);

            System.out.println("Respuesta HTTP: " + cliente.getCodigoHTTP());
            System.out.println("Analizando HTML...");

            parser = new HtmlParser();
            documento = parser.analizar(html);

            System.out.println("Convirtiendo a Markdown...");

            conversor = new MarkdownConverter();
            markdown = conversor.convertir(documento, url);

            if (args.length == 2) {
                destino = Paths.get(args[1]);
            } else {
                nombreArchivo = Paths.get(url.getPath()).getFileName() == null
                        ? "pagina.md"
                        : Paths.get(url.getPath()).getFileName().toString();

                nombreArchivo = nombreArchivo.replaceFirst("\\.[^.]*$", "");

                if (nombreArchivo.isEmpty()) {
                    nombreArchivo = "pagina";
                }

                destino = Paths.get(nombreArchivo + ".md");
            }

            escritor = new MarkdownWriter();
            escritor.guardar(destino, markdown);

            System.out.println();
            System.out.println("Archivo generado: " + destino.toAbsolutePath());
            System.out.println("Tamaño: " + java.nio.file.Files.size(destino) + " bytes");
            System.out.println("Conversión finalizada.");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
