package cotuba.md;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;
import java.util.stream.Stream;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Component;

import cotuba.domain.Capitulo;

@Component
public class RenderizadorHtml {

  public List<Capitulo> renderiza(Path diretorioDosMD) {
    PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**/*.md");

    try (Stream<Path> arquivosMD = Files.list(diretorioDosMD)) {
      return arquivosMD
          .filter(matcher::matches)
          .sorted()
          .map(arquivoMD -> {
            Capitulo capitulo = new Capitulo();
            capitulo.setConteudoHtml(HtmlRenderer.builder().build().render((parseMd(arquivoMD, capitulo))));
            return capitulo;
          }).toList();
    } catch (IOException ex) {
      throw new IllegalStateException(
          "Erro tentando encontrar arquivos .md em " + diretorioDosMD.toAbsolutePath(), ex);
    }
  }

  private Node parseMd(Path arquivoMD, Capitulo capitulo) {
    try {
      var document = Parser.builder().build().parseReader(Files.newBufferedReader(arquivoMD));
      
      document.accept(new AbstractVisitor() {
        @Override
        public void visit(Heading heading) {
          if (heading.getLevel() == 1) { // capítulo
            capitulo.setTitulo(((Text) heading.getFirstChild()).getLiteral());
          } else if (heading.getLevel() == 2) { // seção
          } else if (heading.getLevel() == 3) { // título
          }
        }
      });
      return document;
    } catch (IOException ex) {
      throw new IllegalStateException("Erro ao renderizar arquivo " + arquivoMD, ex);
    }
  }

}