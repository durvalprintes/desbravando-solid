package cotuba.domain;

import java.nio.file.Path;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Ebook {

  private String formato;
  private Path arquivoDeSaida;
  private List<Capitulo> capitulos;

  public Boolean isUltimoCapitulo(Capitulo ultimoCapitulo) {
    return !capitulos.stream().reduce((first, last) -> last).filter(capitulo -> capitulo.equals(ultimoCapitulo))
        .isEmpty();
  }
}
