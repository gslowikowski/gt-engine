package play.template2;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;

public class OutOveridingTest {

    @Test
    public void testLegacyOutOverriding() throws Exception {
        TemplateSourceRenderer sr = new TemplateSourceRenderer( new GTTemplateRepoBuilder().build());

        Map<String, Object> args = new HashMap<String, Object>();
        args.put("myData", "xxx");

        assertThat( sr.renderSrc("a%{ print 'b' }%c", args) ).isEqualTo("abc");
        assertThat( sr.renderSrc("a%{ println('b') }%c", args) ).isEqualTo("ab" + System.getProperty("line.separator") + "c");
        assertThat( sr.renderSrc("a%{ printf('%d',1) }%c", args) ).isEqualTo("a1c");


    }
}
