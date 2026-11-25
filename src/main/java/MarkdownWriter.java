package main.java;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class MarkdownWriter {

    public void guardar(Path destino, String contenido) throws IOException {
        Path directorio;

        directorio = destino.getParent();

        if (directorio != null) {
            Files.createDirectories(directorio);
        }

        Files.writeString(destino, contenido, StandardCharsets.UTF_8);
    }
}
