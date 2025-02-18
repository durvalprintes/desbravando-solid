package cotuba.cli;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import cotuba.application.ParametrosCotuba;
import cotuba.domain.FormatoEbook;
import lombok.Getter;

@Getter
class LeitorOpcoesCli implements ParametrosCotuba {

  private Path diretorioDosMD;
  private FormatoEbook formato;
  private Path arquivoDeSaida;
  private boolean modoVerboso = false;

  public LeitorOpcoesCli(String[] args) {
    var options = criaOpcoes();
    CommandLine cmd = parseOptions(args, options);
    trataDiretorioDosMD(cmd);
    trataFormato(cmd);
    trataArquivoDeSaida(cmd);
    trataModoVerboso(cmd);
  }

  private Options criaOpcoes() {
    var options = new Options();

    var opcaoDeDiretorioDosMD = new Option("d", "dir", true,
        "Diretório que contém os arquivos md. Default: diretório atual.");
    options.addOption(opcaoDeDiretorioDosMD);

    var opcaoDeFormatoDoEbook = new Option("f", "format", true,
        "Formato de saída do ebook. Pode ser: pdf ou epub. Default: pdf");
    options.addOption(opcaoDeFormatoDoEbook);

    var opcaoDeArquivoDeSaida = new Option("o", "output", true,
        "Arquivo de saída do ebook. Default: book.{formato}.");
    options.addOption(opcaoDeArquivoDeSaida);

    var opcaoModoVerboso = new Option("v", "verbose", false,
        "Habilita modo verboso.");
    options.addOption(opcaoModoVerboso);

    return options;
  }

  private CommandLine parseOptions(String[] args, Options options) {
    var ajuda = new HelpFormatter();

    try {
      return new DefaultParser().parse(options, args);
    } catch (ParseException e) {
      ajuda.printHelp("cotuba", options);
      throw new IllegalArgumentException("Opção inválida", e);
    }
  }

  private void trataDiretorioDosMD(CommandLine cmd) {
    String nomeDoDiretorioDosMD = cmd.getOptionValue("dir");

    if (nomeDoDiretorioDosMD != null) {
      diretorioDosMD = Paths.get(nomeDoDiretorioDosMD);
      if (!Files.isDirectory(diretorioDosMD)) {
        throw new IllegalArgumentException(nomeDoDiretorioDosMD + " não é um diretório.");
      }
    } else {
      Path diretorioAtual = Paths.get("");
      diretorioDosMD = diretorioAtual;
    }
  }

  private void trataFormato(CommandLine cmd) {
    String nomeDoFormatoDoEbook = cmd.getOptionValue("format");

    if (nomeDoFormatoDoEbook != null) {
      formato = FormatoEbook.valueOf(nomeDoFormatoDoEbook.toUpperCase());
    } else {
      formato = FormatoEbook.PDF;
    }
  }

  private void trataArquivoDeSaida(CommandLine cmd) {
    String nomeDoArquivoDeSaidaDoEbook = cmd.getOptionValue("output");
    if (nomeDoArquivoDeSaidaDoEbook != null) {
      arquivoDeSaida = Paths.get(nomeDoArquivoDeSaidaDoEbook);
    } else {
      arquivoDeSaida = Paths.get("book." + formato.name().toLowerCase());
    }
    if (Files.isDirectory(arquivoDeSaida)) {
      try (Stream<Path> files = Files.walk(arquivoDeSaida)) {
        files
            .sorted(Comparator.reverseOrder())
            .map(Path::toFile).forEach(File::delete);
      } catch (IOException e) {
        throw new IllegalArgumentException(e);
      }
    } else {
      try {
        Files.deleteIfExists(arquivoDeSaida);
      } catch (IOException e) {
        throw new IllegalArgumentException(e);
      }
    }
  }

  private void trataModoVerboso(CommandLine cmd) {
    modoVerboso = cmd.hasOption("verbose");
  }
}
