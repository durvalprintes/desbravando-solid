package cotuba.application;

import java.nio.file.Path;

import cotuba.domain.Ebook;
import cotuba.epub.GeradorEpub;
import cotuba.md.RenderizadorHtml;
import cotuba.pdf.GeradorPdf;

public class Cotuba {

  public void executa(Path diretorioDosMD, String formato, Path arquivoDeSaida) {
    Ebook ebook = new Ebook(formato, arquivoDeSaida, new RenderizadorHtml().renderiza(diretorioDosMD));

    if ("pdf".equals(formato)) {
      new GeradorPdf().gera(ebook);
    } else if ("epub".equals(formato)) {
      new GeradorEpub().gera(ebook);
    } else {
      throw new IllegalArgumentException("Formato do ebook inválido: " + formato);
    }
  }
}
