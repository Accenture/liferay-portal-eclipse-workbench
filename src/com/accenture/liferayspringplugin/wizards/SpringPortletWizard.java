package com.accenture.liferayspringplugin.wizards;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

import com.accenture.liferayspringplugin.wizards.portlet.FinishWizardPage;
import com.accenture.liferayspringplugin.wizards.portlet.PluginTypeWizardPage;
import com.accenture.liferayspringplugin.wizards.portlet.SpringPortletMavenWizardPage;
import com.accenture.liferayspringplugin.wizards.portlet.SpringPortletAntWizardPage;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;
import static com.accenture.liferayspringplugin.wizards.Constants.*;

/**
 * This is a Spring Portlet wizard. Its role is to create a new ant
 * based spring portlet.
 * 
 * @author Digital Liferay Capability IDC
 */

public class SpringPortletWizard extends Wizard implements INewWizard {
	private SpringPortletAntWizardPage antPage;
	private PluginTypeWizardPage pTypePage;
	private SpringPortletMavenWizardPage mavenPage;
	private ISelection selection;

	/**
	 * Constructor for LiferaSpringWizard.
	 */
	public SpringPortletWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		antPage = new SpringPortletAntWizardPage();
		mavenPage = new SpringPortletMavenWizardPage(this, antPage);
		pTypePage = new PluginTypeWizardPage(this, antPage, mavenPage);
		addPage(pTypePage);
		addPage(mavenPage);
		addPage(antPage);
		addPage(new FinishWizardPage());
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
			sdkPath = antPage.getSdkHome();
			projectName = antPage.getProjectName();
			portletName = antPage.getPortletName();
			packageName = antPage.getPackageName();
			viewPath = antPage.getJspPath();
			displayCategory = antPage.getDisplayCategry();
		} else {
			sdkPath = mavenPage.getSdkHome();
			projectName = mavenPage.getProjectName();
			portletName = mavenPage.getPortletName();
			packageName = EMPTY;
			viewPath = mavenPage.getJspPath();
			displayCategory = mavenPage.getDisplayCategry();
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
	 */

	private void doFinish(String sdkPath, String projectName,
			String portletName, String packageName, String viewPath,
			String displayCategory, IProgressMonitor monitor)
			throws CoreException {
		// create a sample file
		monitor.beginTask("Creating " + projectName, 2);

		try {

			File file;
			if(pTypePage.isAntPlugin() && antPage.isEnabledSDK()) {
				file = new File(sdkPath + "/portlets");
			} else {
				file = new File(sdkPath);
			}
			if (file.isDirectory()) {
				File projectFolder = new File(file, projectName);

				// 1. Configure FreeMarker
				//
				// You should do this ONLY ONCE, when your application starts,
				// then reuse the same Configuration object elsewhere.

				Configuration cfg = new Configuration(new Version(FREEMARKET_VERSION));

				// Where do we load the templates from:
				cfg.setClassForTemplateLoading(SpringPortletWizard.class,
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
				if(!pTypePage.isAntPlugin()) {
					packageName = getMavenPackage(projectFolder);
				}
				input.put(PACKAGE_NAME, packageName);
				
				boolean isLogEnabled = isLoggerEnabled(projectFolder, pTypePage.isAntPlugin());
				boolean isExceptionEnabled = isExceptionEnabled(projectFolder);
				
				String loggerImport = EMPTY;
				String loggerDeclaration = EMPTY;
				String loggerStatement = EMPTY;
				String exceptionImport = EMPTY;
				String exceptionMethod = EMPTY;
				String exceptionStatement = EMPTY;
				if(isLogEnabled) {
					loggerImport = "\nimport org.slf4j.Logger;\nimport org.slf4j.LoggerFactory;";
					loggerDeclaration = "Logger logger = LoggerFactory.getLogger(" + pClass + ".class);\n";
					loggerStatement = "logger.info(\"In render method...\");";
				}
				String pluginMainException = EMPTY;
				if(isExceptionEnabled) {
					StringBuilder projectExceptionName = new StringBuilder();
					for (String word : projectName.split(PORTLET_NAME_SPLIT)) {
						projectExceptionName.append(word.substring(0, 1).toUpperCase() + word.substring(1));
					}
					pluginMainException = projectExceptionName.toString().replaceAll(PORTLET_NAME_REPLACE_REGEX, EMPTY) + "MainException";

					exceptionImport = "import " + packageName + ".exception." + pluginMainException + ";";
					exceptionMethod = "// Sample method \n\t private void foo() throws " + pluginMainException + "{ \n\t\t throw new " + pluginMainException + "(\"Throwing " + pluginMainException + "\"); \n\t }";
					if(isLogEnabled) {
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

				if(pTypePage.isAntPlugin()) {
					String srcFolder = ANT_SRC_PATH + packageName.replace(".", SLASH);
					WizardUtil.addAntPortlet(sdkPath, projectName, portletName, viewPath,
							displayCategory, projectFolder, cfg, input,
							portletDisplayName, portletConfigName, pClass,
							isExceptionEnabled, pluginMainException, srcFolder, antPage.isEnabledSDK());
				} else {
					String srcFolder = MAVEN_JAVA_PATH + packageName.replace(".", SLASH);
					WizardUtil.addMavenPortlet(sdkPath, projectName, portletName, viewPath,
							displayCategory, projectFolder, cfg, input,
							portletDisplayName, portletConfigName, pClass,
							isExceptionEnabled, pluginMainException, srcFolder);
				}

			}
		} catch (Exception e) {
			WizardUtil.log(e);
		}
		monitor.worked(1);

	}

	/**
	 * This method will return package name of maven project.
	 * 
	 * @param projectFolder
	 * @return
	 */
	private String getMavenPackage(File projectFolder) {
		File src = new File(projectFolder, MAVEN_JAVA_PATH);
		File exceptionDir = null;
		if(src.isDirectory()) {
			exceptionDir = getSrcPackage(src);
		}
		if(exceptionDir == null) {
			return EMPTY;
		}
		String absolutePath = exceptionDir.getAbsolutePath();
		absolutePath = absolutePath.replaceAll("\\\\", ".");
		absolutePath = absolutePath.replaceAll(SLASH, ".");
		String path = absolutePath.substring(absolutePath.indexOf(".src.main.java.")+15);
		return path;
	}

	/**
	 * This method will return directory of portlet package.
	 * 
	 * @param src
	 * @return
	 */
	private File getSrcPackage(File src) {
		for(File file : src.listFiles()) {
			if(file.isDirectory()) {
				if(file.getName().contains(EXCEPTION)) {
					return src;
				}
				else {
					File exceptionDir = getSrcPackage(file);
					if(exceptionDir != null) {
						return exceptionDir;
					}
				}
			}
		}
		return null;		
	}

	/**
	 * It will return true if Exception is enabled in provided plugin.
	 * 
	 * @param src
	 * @return
	 */
	private boolean isExceptionEnabled(File src) {
		return isFileExist(src, "NoSuchModelException.java");
	}

	/**
	 * It will return true if logger is enabled in provided plugin.
	 * 
	 * @param src
	 * @param isAnt
	 * @return
	 */
	private boolean isLoggerEnabled(File src, boolean isAnt) {
		if(isAnt) {
			return isFileExist(src, "slf4j-api-1.7.12.jar");
		}

		File file = new File(src, "pom.xml");
		if(file.exists()) {
			try {
				final Scanner scanner = new Scanner(file);
				while (scanner.hasNextLine()) {
				   final String lineFromFile = scanner.nextLine();
				   if(lineFromFile.contains("org.slf4j")) { 
				      return true;
				   }
				}
			} catch (FileNotFoundException e) {
				WizardUtil.log(e);
			}
		}
		return false;
	}
	
	/**
	 * It check if given file name exist in provided src directory.
	 * 
	 * @param src
	 * @param fileName
	 * @return
	 */
	private boolean isFileExist(File src, String fileName){
		if (src.isDirectory()) {

			// list all the directory contents
			File[] files = src.listFiles();

			for (File file : files) {
				if(file.isDirectory()) {
					boolean fileExist = isFileExist(file, fileName);
					if(fileExist) {
						return fileExist;
					}
				} else if(file.getAbsolutePath().contains(fileName)){
					return true;
				}
			}

		}
		return false;
	}

	/**
	 * We will accept the selection in the workbench to see if we can initialize
	 * from it.
	 * 
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}