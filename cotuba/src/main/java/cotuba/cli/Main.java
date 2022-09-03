package cotuba.cli;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import cotuba.CotubaConfig;
import cotuba.application.Cotuba;

public class Main {

  public static void main(String[] args) {

    var opcoesCli = new LeitorOpcoesCli(args);

    try {

      ApplicationContext appContext = new AnnotationConfigApplicationContext(CotubaConfig.class);
      appContext.getBean(Cotuba.class).executa(opcoesCli);

      System.out.println("Arquivo gerado com sucesso: " + opcoesCli.getFormato());

    } catch (Exception ex) {
      System.err.println(ex.getMessage());
      if (opcoesCli.isModoVerboso()) {
        ex.printStackTrace();
      }
      System.exit(1);
    }
  }

}
