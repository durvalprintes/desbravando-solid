package cotuba.application;

import java.nio.file.Path;
import java.util.List;

import cotuba.domain.Capitulo;

public interface RenderizadorHtml {

  List<Capitulo> renderiza(Path diretorioDosMD);
}
