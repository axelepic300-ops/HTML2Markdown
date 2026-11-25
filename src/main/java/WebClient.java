package main.java;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebClient {

    private int codigoHTTP;

    public String descargarHTML(URL url) throws IOException {
        URLConnection conexion;
        HttpURLConnection conexionHTTP;
        InputStream entrada;
        ByteArrayOutputStream salida;
        byte[] buffer;
        int cantidad;
        int total;
        String tipoContenido;
        String contenido;
        String charset;
        Pattern patron;
        Matcher coincidencia;
        Charset codificacion;

        conexion = url.openConnection();
        conexion.setConnectTimeout(10000);
        conexion.setReadTimeout(15000);

        if (!(conexion instanceof HttpURLConnection)) {
            throw new IOException("La URL debe utilizar HTTP o HTTPS.");
        }

        conexionHTTP = (HttpURLConnection) conexion;
        conexionHTTP.setRequestMethod("GET");
        conexionHTTP.setRequestProperty("User-Agent", "HTML2Markdown/1.0");
        conexionHTTP.setInstanceFollowRedirects(true);

        try {
            codigoHTTP = conexionHTTP.getResponseCode();

            if (codigoHTTP < 200 || codigoHTTP >= 300) {
                throw new IOException("Error HTTP: " + codigoHTTP);
            }

            tipoContenido = conexionHTTP.getContentType();

            if (tipoContenido == null ||
                    (!tipoContenido.toLowerCase().contains("text/html") &&
                            !tipoContenido.toLowerCase().contains("application/xhtml+xml"))) {
                throw new IOException("El contenido recibido no es HTML.");
            }

            charset = "UTF-8";
            patron = Pattern.compile("charset\\s*=\\s*[\"']?([^;\"']+)", Pattern.CASE_INSENSITIVE);
            coincidencia = patron.matcher(tipoContenido);

            if (coincidencia.find()) {
                charset = coincidencia.group(1).trim();
            }

            try {
                codificacion = Charset.forName(charset);
            } catch (Exception e) {
                codificacion = StandardCharsets.UTF_8;
            }

            entrada = conexionHTTP.getInputStream();
            salida = new ByteArrayOutputStream();
            buffer = new byte[8192];
            total = 0;

            try {
                while ((cantidad = entrada.read(buffer)) != -1) {
                    total += cantidad;

                    if (total > 5 * 1024 * 1024) {
                        throw new IOException("El HTML supera el límite de 5 MB.");
                    }

                    salida.write(buffer, 0, cantidad);
                }
            } finally {
                entrada.close();
            }

            contenido = salida.toString(codificacion);

            return contenido;

        } finally {
            conexionHTTP.disconnect();
        }
    }

    public int getCodigoHTTP() {
        return codigoHTTP;
    }
}

