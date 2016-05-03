package com.accenture.liferayspringplugin.wizards;

import static com.accenture.liferayspringplugin.wizards.Constants.*;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

import com.accenture.liferayspringplugin.wizards.plugin.FinishWizardPage;
import com.accenture.liferayspringplugin.wizards.plugin.LiferaySpringAntWizardPage;
import com.accenture.liferayspringplugin.wizards.plugin.LiferaySpringMavenWizardPage;
import com.accenture.liferayspringplugin.wizards.plugin.LoggerWizardPage;
import com.accenture.liferayspringplugin.wizards.plugin.PluginTypeWizardPage;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

/**
 * This is a Liferay Spring Portlet wizard. Its role is to create a new ant
 * based spring portlet.
 * 
 * @author Digital Liferay Capability IDC
 */

public class LiferaySpringWizard extends Wizard implements INewWizard {
	private PluginTypeWizardPage pTypePage;
	private LiferaySpringAntWizardPage antPage;
	private LiferaySpringMavenWizardPage mvnPage;
	private LoggerWizardPage lPage;
	private FinishWizardPage fPage;

	/**
	 * Constructor for LiferaySpringWizard.
	 */
	public LiferaySpringWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		antPage = new LiferaySpringAntWizardPage();
		mvnPage = new LiferaySpringMavenWizardPage(this);
		pTypePage = new PluginTypeWizardPage(this, antPage, mvnPage);
		addPage(pTypePage);
		addPage(mvnPage);
		addPage(antPage);
		lPage = new LoggerWizardPage();
		addPage(lPage);
		fPage = new FinishWizardPage();
		addPage(fPage);
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	public boolean performFinish() {

		final String sdkPath;
		final String projectName;
		final String portletName;
		final String packageName;
		final String viewPath;
		final String displayCategory;

		if(pTypePage.isAntPlugin()) {
			//Get details if plugin type is ant
			sdkPath = antPage.getSdkHome();
			portletName = antPage.getPortletName();
			packageName = antPage.getPackageName();
			viewPath = antPage.getJspPath();
			displayCategory = antPage.getDisplayCategry();
			projectName = antPage.getProjectName();
		} else {
			//Get details if plugin type is maven
			sdkPath = mvnPage.getSdkHome();
			portletName = mvnPage.getPortletName();
			packageName = mvnPage.getPackageName();
			viewPath = mvnPage.getJspPath();
			displayCategory = mvnPage.getDisplayCategry();
			if(mvnPage.getProjectName().length() == 0) {
				projectName = mvnPage.getGroupId();
			} else {
				projectName = mvnPage.getProjectName();
			}		
		}

		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException {
				try {
					doFinish(sdkPath, projectName, portletName, packageName,
							viewPath, displayCategory, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error",
					realException.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * The worker method. It will find the container, create the file if missing
	 * or just replace its contents, and open the editor on the newly created
	 * file.
	 * 
	 * @param displayCategory
	 * @param viewPath
	 * @param packageName
	 * @param portletName
	 * @param enabledSDK 
	 */

	private void doFinish(String sdkPath, String projectName,
			String portletName, String packageName, String viewPath,
			String displayCategory, IProgressMonitor monitor)
			throws CoreException {
		// create a sample file
		monitor.beginTask(CREATING + projectName, 2);
		String groupId = EMPTY;
		String artifactId = EMPTY;
		String version = EMPTY;
		if(!pTypePage.isAntPlugin()) {
			groupId = projectName;
			artifactId = portletName;
			version = packageName;
			String packageSuffix = portletName;
			packageSuffix = packageSuffix.replaceAll("[^a-zA-Z0-9]", "");
			packageName = projectName + DOT + packageSuffix;
			projectName = portletName;
		} else if(!projectName.endsWith(PORTLET)) {
			projectName = projectName + PORTLET_;
		}

		try {

			File file = WizardUtil.getPluginLocation(sdkPath, pTypePage.isAntPlugin() && antPage.isEnabledSDK());

			if (file.isDirectory()) {
				File projectFolder = new File(file, projectName);
				projectFolder.mkdir();

				// 1. Configure FreeMarker
				//
				// You should do this ONLY ONCE, when your application starts,
				// then reuse the same Configuration object elsewhere.

				Configuration cfg = new Configuration(new Version(FREEMARKET_VERSION));

				// Where do we load the templates from:
				cfg.setClassForTemplateLoading(LiferaySpringWizard.class,
						TEMPLATES_FREEMARKER_PATH);

				// Some other recommended settings:
				cfg.setIncompatibleImprovements(new Version(2, 3, 20));
				cfg.setDefaultEncoding("UTF-8");
				cfg.setLocale(Locale.US);
				cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

				// 2. Proccess template(s)
				//
				// You will do this for several times in typical applications.

				// 2.1. Prepare the template input:

				Map<String, Object> input = new HashMap<String, Object>();

				input.put(PROJECT_NAME, projectName);
				String portletDisplayName = portletName.substring(0, 1).toUpperCase() + portletName.substring(1);
				input.put(PORTLET_DISPLAY_NAME, portletDisplayName);
				input.put(PORTLET_NAME, portletName);
				String portletConfigName = portletName.replaceAll(PORTLET_NAME_REPLACE_REGEX, "-");
				input.put(PORTLET_CONFIG_NAME, portletConfigName);
				input.put(DISPLAY_CATEGORY, displayCategory);
				if(!viewPath.endsWith(SLASH)) {
					viewPath = viewPath + SLASH ;
				}
				if(!viewPath.startsWith(SLASH)) {
					viewPath = SLASH + viewPath;
				}
				input.put(VIEW_PATH, viewPath);
				
				StringBuilder portletClass = new StringBuilder();
				for (String word : portletName.split(PORTLET_NAME_SPLIT)) {
					portletClass.append(word.substring(0, 1).toUpperCase() + word.substring(1));
				}
				String pClass = portletClass.toString().replaceAll(PORTLET_NAME_REPLACE_REGEX, EMPTY);
				
				input.put(PORTLET_CLASS, pClass );
				input.put(PACKAGE_NAME, packageName);
				
				String loggerImport = EMPTY;
				String loggerDeclaration = EMPTY;
				String loggerStatement = EMPTY;
				String exceptionImport = EMPTY;
				String exceptionMethod = EMPTY;
				String exceptionStatement = EMPTY;
				if(lPage.isLogEnabled()) {
					loggerImport = "\nimport org.slf4j.Logger;\nimport org.slf4j.LoggerFactory;";
					loggerDeclaration = "Logger logger = LoggerFactory.getLogger(" + pClass + ".class);\n";
					loggerStatement = "logger.info(\"In render method...\");";
				}
				String pluginMainException = EMPTY;
				if(lPage.isExceptionEnabled()) {
					StringBuilder projectExceptionName = new StringBuilder();
					for (String word : projectName.split(PORTLET_NAME_SPLIT)) {
						projectExceptionName.append(word.substring(0, 1).toUpperCase() + word.substring(1));
					}
					pluginMainException = projectExceptionName.toString().replaceAll(PORTLET_NAME_REPLACE_REGEX, EMPTY) + "MainException";

					exceptionImport = "import " + packageName + ".exception." + pluginMainException + ";";
					exceptionMethod = "// Sample method \n\t private void foo() throws " + pluginMainException + "{ \n\t\t throw new " + pluginMainException + "(\"Throwing " + pluginMainException + "\"); \n\t }";
					if(lPage.isLogEnabled()) {
						exceptionStatement = "try { \n\t\t\t foo(); \n\t\t } catch (" + pluginMainException + " e) { \n\t\t\t logger.error(e.getMessage()); \n\t\t }";
					} else {
						exceptionStatement = "try { \n\t\t\t foo(); \n\t\t } catch (" + pluginMainException + " e) { \n\t\t\t // TODO Auto-generated catch block \n\t\t\t e.printStackTrace(); \n\t\t }";
					}
				}
				input.put(LOGGER_IMPORT, loggerImport);
				input.put(LOGGER_DECLARATION, loggerDeclaration);
				input.put(LOGGER_STATEMENT, loggerStatement);
				input.put(EXCEPTION_IMPORT, exceptionImport);
				input.put(EXCEPTION_METHOD, exceptionMethod);
				input.put(EXCEPTION_STATEMENT, exceptionStatement);
				//viewPath = DOCROOT_PATH + viewPath;
				
				if(pTypePage.isAntPlugin()) {
					String srcFolder = ANT_SRC_PATH + packageName.replace(".", SLASH);
					URL antTplPath = getClass().getResource(TEMPLATES_ANT_SAMPLE_PORTLET_PATH);
					WizardUtil.createAntPlugin(viewPath, projectFolder, antTplPath, cfg, input,
							portletConfigName, pClass, pluginMainException, srcFolder, lPage);
				} else {
					String srcFolder = MAVEN_JAVA_PATH + packageName.replace(".", SLASH);
					URL mavenTplPath = getClass().getResource(TEMPLATES_MAVEN_SAMPLE_PORTLET_PATH);
					input.put("groupId", groupId);
					input.put("artifactId", artifactId);
					input.put("version", version);
					WizardUtil.createMavenPlugin(viewPath, projectFolder, mavenTplPath, cfg, input,
							portletConfigName, pClass, pluginMainException, srcFolder, lPage);
				}
			}
		} catch (Exception e) {
			WizardUtil.log(e);
		}
		monitor.worked(1);

	}

	/**
	 * We will accept the selection in the workbench to see if we can initialize
	 * from it.
	 * 
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		//this.selection = selection;
	}
}