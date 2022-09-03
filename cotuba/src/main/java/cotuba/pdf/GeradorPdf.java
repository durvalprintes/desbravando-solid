package cotuba.pdf;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.property.AreaBreakType;

import cotuba.domain.Ebook;

public class GeradorPdf {

  public void gera(Ebook ebook) {
    try (var writer = new PdfWriter(Files.newOutputStream(ebook.getArquivoDeSaida()));
        var pdf = new PdfDocument(writer);
        var pdfDocument = new Document(pdf)) {

      ebook.getCapitulos().forEach((capitulo -> {
        try {
          List<IElement> convertToElements = HtmlConverter.convertToElements(capitulo.getConteudoHtml());
          for (IElement element : convertToElements) {
            pdfDocument.add((IBlockElement) element);
          }
          if (!ebook.isUltimoCapitulo(capitulo)) {
            pdfDocument.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
          }
        } catch (IOException e) {
          throw new IllegalArgumentException(e);
        }
      }));

    } catch (Exception ex) {
      throw new IllegalStateException("Erro ao criar arquivo PDF: " + ebook.getArquivoDeSaida().toAbsolutePath(), ex);
    }

  }
}
