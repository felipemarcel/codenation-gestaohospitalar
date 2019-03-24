package gestao.repositoryTest;

import gestao.repository.ProdutoRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepository repository;

    @Test
    public void contextLoads() {
        Assertions.assertThat(repository).isNotNull();
    }
}
