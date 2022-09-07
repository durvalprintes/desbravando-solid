package cotuba.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cotuba.domain.Ebook;
import cotuba.md.RenderizadorHtml;

@Component
public class Cotuba {

  @Autowired
  private RenderizadorHtml renderizadorHtml;

  @Autowired
  private List<GeradorEbook> geradoresEbook;

  public void executa(ParametrosCotuba parametros) {
    Ebook ebook = new Ebook(
        parametros.getFormato(),
        parametros.getArquivoDeSaida(),
        renderizadorHtml.renderiza(parametros.getDiretorioDosMD()));

    geradoresEbook.stream()
        .filter(gerador -> gerador.accept(ebook.getFormato())).findAny()
        .orElseThrow(() -> new IllegalArgumentException("Formato do ebook inválido: " + parametros.getFormato()))
        .gera(ebook);
  }
}
