package cotuba.html;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import cotuba.application.GeradorEbook;
import cotuba.domain.Ebook;
import cotuba.domain.FormatoEbook;

@Component
public class GeradorHtml implements GeradorEbook {

  @Override
  public boolean accept(FormatoEbook formato) {
    return FormatoEbook.HTML.equals(formato);
  }

  @Override
  public void gera(Ebook ebook) {
    try {
      Path diretorioHtml = Files.createDirectory(ebook.getArquivoDeSaida());
      AtomicInteger ordem = new AtomicInteger(1);
      String html = """
          <!DOCTYPE html><html lang="pt-BR">
          <head><meta charset="UTF-8"><title>%s</title></head>
          <body>%s</body></html>""";
      ebook.getCapitulos().stream().forEach(capitulo -> {
        try {
          String nomeArquivo = formataNomeArquivo(ordem.getAndIncrement(), capitulo.getTitulo());
          Path arquivoHtml = diretorioHtml.resolve(nomeArquivo);
          Files.writeString(arquivoHtml, html.formatted(capitulo.getTitulo(), capitulo.getConteudoHtml()),
              StandardCharsets.UTF_8);
        } catch (IOException e) {
          throw new IllegalArgumentException(
              "Erro ao criar arquivo HTML: " + ebook.getArquivoDeSaida().toAbsolutePath(), e);
        }
      });
    } catch (IOException e) {
      throw new IllegalArgumentException("Erro ao criar diretorio HTML: " + ebook.getArquivoDeSaida().toAbsolutePath(),
          e);
    }

  }

  private String formataNomeArquivo(int ordem, String titulo) {
    return ordem + "-"
        + Normalizer.normalize(titulo.toLowerCase(), Normalizer.Form.NFD).replaceAll("[^\\w]", "")
        + ".html";
  }
}
