package cotuba.cli;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import cotuba.CotubaConfig;
import cotuba.application.Cotuba;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

  public static void main(String[] args) {

    var opcoesCli = new LeitorOpcoesCli(args);

    try (AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(
        CotubaConfig.class);) {
      applicationContext.getBean(Cotuba.class).executa(opcoesCli);
      log.info("Arquivo gerado com sucesso: " + opcoesCli.getFormato());
    } catch (Exception ex) {
      log.error(ex.getMessage());
      if (opcoesCli.isModoVerboso()) {
        ex.printStackTrace();
      }
      System.exit(1);
    }
  }

}
