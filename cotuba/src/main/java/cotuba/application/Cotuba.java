package cotuba.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cotuba.domain.Ebook;

@Component
public class Cotuba {

  @Autowired
  private RenderizadorHtml renderizadorHtml;

  @Autowired
  private GeradorPdf geradorPdf;

  @Autowired
  private GeradorEpub geradorEpub;

  public void executa(ParametrosCotuba parametros) {
    Ebook ebook = new Ebook(
        parametros.getFormato(),
        parametros.getArquivoDeSaida(),
        renderizadorHtml.renderiza(parametros.getDiretorioDosMD()));

    if ("pdf".equals(parametros.getFormato())) {
      geradorPdf.gera(ebook);
    } else if ("epub".equals(parametros.getFormato())) {
      geradorEpub.gera(ebook);
    } else {
      throw new IllegalArgumentException("Formato do ebook inválido: " + parametros.getFormato());
    }
  }
}
