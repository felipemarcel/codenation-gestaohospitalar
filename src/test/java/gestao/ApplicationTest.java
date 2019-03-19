package gestao;

import gestao.api.HospitalController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

    @Autowired
    private HospitalController controller;

    // char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    @Test
    public void contextLoads() {
        assertThat(controller).isNotNull();
    }
}
