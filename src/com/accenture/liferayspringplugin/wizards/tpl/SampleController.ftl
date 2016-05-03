package ${packageName};

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
${loggerImport}
${exceptionImport}

@Controller(value = "${portletClass}")
@RequestMapping("VIEW")
public class ${portletClass} {
	${loggerDeclaration}
	@RenderMapping
	public String handleRenderRequest(RenderRequest request,
			RenderResponse response, Model model) {
		${loggerStatement}
		${exceptionStatement}
		return "view";
	}
	${exceptionMethod}
}
