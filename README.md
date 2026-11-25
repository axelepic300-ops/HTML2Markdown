# HTML2Markdown

## Descripción

HTML2Markdown es una aplicación desarrollada en Java que permite convertir páginas web HTML a formato Markdown.

El programa recibe una dirección URL, descarga el contenido HTML, analiza sus etiquetas y genera un archivo `.md` con el contenido convertido.

## Requisitos

- Java 17 o superior.
- IntelliJ IDEA.
- Conexión a Internet.

## Funcionalidades

- Descargar páginas mediante HTTP y HTTPS.
- Validar las respuestas del servidor.
- Convertir encabezados y párrafos.
- Convertir texto en negritas y cursivas.
- Convertir enlaces e imágenes.
- Convertir listas ordenadas y desordenadas.
- Convertir bloques de código y citas.
- Guardar archivos Markdown en UTF-8.
- Excluir etiquetas script y style.

## Estructura del proyecto

```text
HTML2Markdown/
├── README.md
├── .gitignore
├── src/
│   └── main/
│       └── java/
│           ├── HTML2Markdown.java
│           ├── WebClient.java
│           ├── HtmlParser.java
│           ├── MarkdownConverter.java
│           └── MarkdownWriter.java
├── tests/
│   ├── encabezados.html
│   ├── listas.html
│   ├── enlaces.html
│   └── documento_completo.html
└── output/
```

## Ejecución

El programa recibe una URL como argumento obligatorio y una ruta de salida opcional.

Ejemplo:

```bash
java HTML2Markdown https://dev.java/
```

También se puede indicar el nombre del archivo:

```bash
java HTML2Markdown https://dev.java/ salida.md
```

## Pruebas

Se realizaron pruebas con cuatro documentos HTML:

- encabezados.html: comprobación de encabezados y párrafos.
- listas.html: comprobación de listas ordenadas, desordenadas y anidadas.
- enlaces.html: comprobación de enlaces e imágenes.
- documento_completo.html: comprobación de diferentes etiquetas HTML.

Las cuatro pruebas se ejecutaron correctamente y generaron sus respectivos archivos Markdown.

## Preguntas de reflexión

**1. ¿Qué papel desempeña la aplicación dentro de una arquitectura cliente-servidor?**

La aplicación funciona como cliente, porque solicita información a un servidor web y recibe el documento HTML para procesarlo.

**2. ¿Qué responsabilidades corresponden al servidor y cuáles al cliente?**

El servidor almacena y proporciona la página HTML. El cliente realiza la solicitud, recibe la información, la convierte y guarda el resultado.

**3. ¿Qué diferencia existe entre recuperar HTML e interpretar su estructura?**

Recuperar HTML significa descargar el contenido de una página. Interpretarlo significa identificar sus etiquetas y comprender cómo está organizado.

**4. ¿Por qué no basta reemplazar etiquetas HTML con expresiones regulares?**

Porque HTML puede contener elementos anidados y estructuras complejas. Las expresiones regulares por sí solas no permiten interpretar correctamente toda esa estructura.

**5. ¿Qué dificultades presentan los elementos anidados?**

Pueden contener varias etiquetas dentro de otras. Es necesario conservar el orden y la relación entre los elementos al convertirlos.

**6. ¿Qué información puede perderse al convertir HTML a Markdown?**

Se pueden perder estilos, colores, tamaños, posiciones y otros detalles visuales que Markdown no representa de la misma manera.

**7. ¿Por qué deben resolverse las URL relativas?**

Porque necesitan una dirección base para convertirse en enlaces completos que puedan utilizarse correctamente.

**8. ¿Qué ventajas ofrece separar descarga, análisis y conversión en clases?**

Permite organizar mejor el código, facilita encontrar errores y hace que cada clase tenga una responsabilidad específica.

**9. ¿Qué limitaciones presenta ParserDelegator con HTML moderno?**

Es un analizador antiguo y no interpreta correctamente todas las características de HTML5.

**10. ¿Qué modificaciones serían necesarias para convertir tablas HTML a Markdown?**

Sería necesario identificar las etiquetas de tablas, filas y celdas, y convertirlas al formato de filas y columnas que utiliza Markdown.

## Tecnologías utilizadas

- Java 17.
- URL y URLConnection.
- HttpURLConnection.
- ParserDelegator.
- HTMLEditorKit.ParserCallback.
- Files y Path.
- Git y GitHub.
