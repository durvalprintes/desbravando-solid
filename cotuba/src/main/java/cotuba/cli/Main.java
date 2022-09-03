package cotuba.cli;

import java.nio.file.Path;

import cotuba.application.Cotuba;

public class Main {

  public static void main(String[] args) {

    LeitorOpcoesCli opcoesCli = new LeitorOpcoesCli(args);

    Path diretorioDosMD = opcoesCli.getDiretorioDosMD();
    String formato = opcoesCli.getFormato();
    Path arquivoDeSaida = opcoesCli.getArquivoDeSaida();

    try {

      new Cotuba().executa(diretorioDosMD, formato, arquivoDeSaida);

      System.out.println("Arquivo gerado com sucesso: " + formato);

    } catch (Exception ex) {
      System.err.println(ex.getMessage());
      if (opcoesCli.isModoVerboso()) {
        ex.printStackTrace();
      }
      System.exit(1);
    }
  }

}
