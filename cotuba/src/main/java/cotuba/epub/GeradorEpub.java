package cotuba.epub;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.stereotype.Component;

import cotuba.application.GeradorEbook;
import cotuba.domain.Ebook;
import cotuba.domain.FormatoEbook;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubWriter;
import nl.siegmann.epublib.service.MediatypeService;

@Component
public class GeradorEpub implements GeradorEbook {

  @Override
  public boolean accept(FormatoEbook formato) {
    return FormatoEbook.EPUB.equals(formato);
  }

  @Override
  public void gera(Ebook ebook) {

    var epub = new Book();

    ebook.getCapitulos().forEach(capitulo -> {
      epub.addSection(capitulo.getTitulo(),
          new Resource(capitulo.getConteudoHtml().getBytes(), MediatypeService.XHTML));

      var epubWriter = new EpubWriter();
      try {
        epubWriter.write(epub, Files.newOutputStream(ebook.getArquivoDeSaida()));
      } catch (IOException ex) {
        throw new IllegalStateException("Erro ao criar arquivo EPUB: " + ebook.getArquivoDeSaida().toAbsolutePath(),
            ex);
      }
    });
  }
}
