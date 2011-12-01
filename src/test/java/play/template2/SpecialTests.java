package play.template2;

import org.junit.Test;
import play.template2.exceptions.GTRuntimeExceptionWithSourceInfo;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;

public class SpecialTests {

    @Test
    public void testTagFileBodyToStringAndInclude() throws Exception{
        GTTemplateRepo tr = new GTTemplateRepoBuilder()
                .withTemplateRootFolder( new File("src/test/resources/template_root/"))
                .build();

        TemplateSourceRenderer sr = new TemplateSourceRenderer(tr);

        Map<String, Object> args = new HashMap<String, Object>();

        assertThat(sr.renderSrc("A#{printBodyInVerbatim}BC#{/printBodyInVerbatim}C", args)).isEqualTo("A\n[from tag. body: BC]\nC");
        assertThat(sr.renderSrc("#{include 'simpleTemplate.txt'/}-A#{printBodyInVerbatim}BC#{/printBodyInVerbatim}C", args)).isEqualTo("[from simpleTemplate]-A\n[from tag. body: BC]\nC");
        assertThat(sr.renderSrc("A#{printBodyInVerbatim}BC\n#{include 'simpleTemplate.txt'/}#{/printBodyInVerbatim}C", args)).isEqualTo("A\n[from tag. body: BC\n[from simpleTemplate]]\nC");

    }
    
    @Test
    public void testErrorMessage_missingInclude() throws Exception{

        GTTemplateRepo tr = new GTTemplateRepoBuilder()
                .withTemplateRootFolder( new File("src/test/resources/template_root/"))
                .build();

        TemplateSourceRenderer sr = new TemplateSourceRenderer(tr);

        Map<String, Object> args = new HashMap<String, Object>();

        GTRuntimeExceptionWithSourceInfo e=null;
        try {
            sr.renderSrc("#{include 'missing.txt'/}", args);
        }catch(GTRuntimeExceptionWithSourceInfo _e) {
            e = _e;
        }
        
        assertThat(e).isNotNull();
        assertThat(e.getMessage()).isEqualTo("Cannot find template");
        assertThat(e.templateLocation.relativePath).contains("_generated_key_"); // generated source without filename


        e=null;
        try {
            sr.renderSrc("#{printBodyInVerbatim}#{include 'missing.txt'/}#{/printBodyInVerbatim}", args);
        }catch(GTRuntimeExceptionWithSourceInfo _e) {
            e = _e;
        }

        assertThat(e).isNotNull();
        assertThat(e.getMessage()).isEqualTo("Cannot find template");
        assertThat(e.templateLocation.relativePath).contains("_generated_key_"); // generated source without filename



    }
}
