package com.accenture.liferayspringplugin.wizards;

/**
 * 
 * @author Digital Liferay Capability IDC
 *
 */

public class Constants {
	
	//LiferaySpringWizardPage.java
	public static final String PLUGIN_WIZARD_PAGE_TITLE = "Liferay Spring Portlet Plugin Project";
	public static final String PLUGIN_WIZARD_PAGE_SDK_DESCRIPTION = "This wizard will create Lieray Spring plugin project and store it in your Lifera plugins SDK for future use";
	public static final String PLUGIN_WIZARD_PAGE_DESCRIPTION = "This wizard will create Lieray Spring plugin project and store it in provided location";
	
	//Label & Validation messages
	public static final String LIFERAY_SDK_PATH_INVALID = "Invalid Liferay SDK path - Please specify correct entry to Liferay SDK home";
	public static final String LOCATION_PATH_INVALID = "Invalid Location path - Please specify correct location";
	public static final String LOCATION_PATH_REQUIRED = "Requird Field! Please specify location";
	public static final String LIFERAY_SDK_PATH_REQUIRED = "Requird Field! Please specify Liferay SDK path";
	public static final String PROJECT_NAME_IN_CORRECT = "Invalid Project Name. Allowed characters include [a-z A-Z 0-9 _-] ";
	public static final String PROJECT_NAME_REQUIRED = "Requird Field! Please specify project name";
	public static final String PORTLET_NAME_ERROR = "Invalid Portlet Name. Allowed characters include [a-z A-Z 0-9 _-] ";
	public static final String GROUP_ID_ERROR = "Invalid GroupId. Allowed characters include [a-z A-Z 0-9 _-]";
	public static final String ARTIFACT_ID_REQUIRED = "Requird Field! Please specify artifactId";
	public static final String ARTIFACT_ID_ERROR = "Invalid ArtifactId. Allowed characters include [a-z A-Z 0-9_-] ";
	public static final String PORTLET_NAME_REQUIRED = "Requird Field! Please specify porlet name";
	public static final String DISPLAY_CATEGORY_ERROR = "Requird Field! Please specify display category";
	public static final String VIEW_PATH_ERROR = "Requird Field! Please specify path which will contain all JSPs for the polet";
	public static final String PACKAGE_NAME_ERROR = "Invalid Package Name. Allowed characters include [a-z A-Z 0-9 _-]";
	public static final String VERSION_NUMBER_ERROR = "Invalid Version Number. Allowed characters include [a-z A-Z 0-9 .]";
	
	public static final String SELECT_LIFERAY_SDK_PATH = "Pleasse specify Liferay SDK home";
	public static final String LIFERAY_SDK_HOME = "Liferay SDK Home";
	public static final String DEFAULT_DISPLAY_CATEGORY = "Sample";
	public static final String DEFAULT_JSP_FOLDER = "/views/";
	public static final String COM_DOT_ACCENTURE = "com.accenture";
	public static final String _DISPLAY_CATEGORY_LABEL = "&Display Category:";
	public static final String _JSP_PATH_LABEL = "&View Path:";
	public static final String PACKAGE_LABEL = "&Package:";
	public static final String _PORTLET_NAME_LABEL = "&Portlet name:";
	public static final String _PROJECT_NAME_LABLE = "&Project name:";
	public static final String BROWSE_BUTTON_LABEL = "Select";
	public static final String SDK_PATH_LABEL = "&Liferay SDK Home";
	public static final String LOCATION_PATH_LABEL = "&Specify File Location";
	public static final String SDK_LOCATION_PATH_LABEL = "&Plugins SDK Location";
	public static final String BUILD_TYPE_LABEL = "&Select Build Type:";
	public static final String ANT_BUTTON_LABEL = "Ant";
	public static final String MAVEN_BUTTON_LABEL = "Maven";
	public static final String STORE_LOCATION_LABEL = "&Select if you want to store the portlet in Liferay Plugins SDK?";
	public static final String VERSION_LABEL = "&Version:";
	public static final String GROUP_ID_LABEL = "&GroupId:";
	public static final String ARTIFACT_ID_LABLE = "&ArtifactId:";
	public static final String DEFAULT_VERSION = "0.1";

	//LoggerWizardPage.java
	public static final String LOGGER_PAGE_WIZARD_TITLE = "Liferay Spring Portlet Plugin Project";
	public static final String LOGGER_PAGE_WIZARD_DESC = "This wizard will enable base Logging framework and Exception handling framework for your plugin project";

	public static final String LOGGER_IMPLEMENTATION_TYPE_LABEL = "&Select Base Logging Framework";
	public static final String ADD_EXCEPTION_FRAMWORK_LABEL = "&Enable Base Exception Handling Framwork";
	public static final String LOGBACK = "logback";
	public static final String JDK14 = "jdk14";
	public static final String LOG4J = "log4J";
	public static final String SLF4J_SIMPLE = "slf4j simple";
	public static final String _ENABLE_SLF4J_LABEL = "&Enable Base Logging Framework";

	//FinishWizardPage.java
	public static final String FINISH_PAGE_WIZARD_TITLE = "Liferay Spring Portlet Plugin Project";
	public static final String FINISH_PAGE_WIZARD_DESCRIPTION = "This wizard will create a Lieray Spring Portlet plugin project in the provided Lifera SDK";

	//SpringPortletWizardPage.java
	public static final String PORTLET_WIZARD_PAGE_TITLE = "Liferay Spring Portlet Plugin Project";
	public static final String PORTLET_WIZARD_DESCRIPTION = "This wizard will create Lieray Spring plugin portlet within specified Liferay plugin project";
	public static final String DUPLICATE_PORTLET_NAME = "A Portlet with specified name already exists in the selected Liferay plugin project";

	//REGEX
	public static final String PORTLET_NAME_SPLIT = "\\s+|,_-\\s*|\\.\\s*";
	public static final String PORTLET_NAME_REPLACE_REGEX = "[^a-zA-Z]";
	public static final String PORTLET_NAME_REGEX = "^[a-zA-Z]+[a-zA-Z0-9 _-]*$";
	public static final String ARTIFACT_ID_NAME_REGEX = "^[a-zA-Z]+[a-zA-Z0-9_-]*$";
	public static final String PACKAGE_NAME_REGEX = "^[a-z]+(\\.[a-z][a-z0-9]*)*$";
	public static final String PROJECT_NAME_REGEX = "^[a-zA-Z]+([a-zA-Z0-9 _-]*)*$";
	public static final String VERSION_NAME_REGEX = "^[a-zA-Z0-9]+(\\.[a-zA-Z0-9]*)*$";

	//Template paths
	public static final String SLASH = "/";
	public static final String DOCROOT_PATH = "/docroot";
	public static final String MAVEN_WEBAPP_PATH = "/src/main/webapp";
	public static final String DOCROOT_WEB_INF = "/docroot/WEB-INF";
	public static final String ANT_WEB_INF_PATH = "/docroot/WEB-INF/";
	public static final String MAVEN_JAVA_PATH = "/src/main/java/";
	public static final String MAVEN_WEB_INF_PATH = "/src/main/webapp/WEB-INF";
	public static final String ANT_SRC_PATH = "/docroot/WEB-INF/src/";
	//public static final String MAVEN_SRC_PATH = "/src/main/webapp/WEB-INF/src/";
	public static final String DOCROOT_WEB_INF_TLD = "/docroot/WEB-INF/tld/";
	public static final String DOCROOT_WEB_INF_SPRING = "/docroot/WEB-INF/spring/";
	public static final String WEB_INF_SPRING = "/WEB-INF/spring-context/";
	public static final String TEMPLATES_ANT_SAMPLE_PORTLET_PATH = "tpl/ant";
	public static final String TEMPLATES_MAVEN_SAMPLE_PORTLET_PATH = "tpl/maven";
	public static final String TEMPLATES_FREEMARKER_PATH = "tpl";
	public static final String _SPRING = "/spring-context";
	public static final String PORTLETS = "/portlets";
	public static final String _PORTLETS_ = "/portlets/";

	public static final String PORTLET_ = "-portlet";
	public static final String EXCEPTION = "exception";
	public static final String EX = "ex";
	public static final String LIB = "lib";
	public static final String _PORTLET_CONTEXT_XML = "-portlet-context.xml";
	public static final String SPRING_MVC_PORTLET_CONTEXT_FTL = "portlet-context.ftl";
	public static final String LIFERAY_DISPLAY_XML = "liferay-display.xml";
	public static final String LIFERAY_PLUGIN_PACKAGE_PROPERTIES = "liferay-plugin-package.properties";
	public static final String SAMPLE_CONTROLLER_FTL = "SampleController.ftl";
	public static final String LIFERAY_DISPLAY_FTL = "liferay-display.ftl";
	public static final String LIFERAY_PLUGIN_PACKAGE_FTL = "plugin-package.ftl";
	public static final String LIFERAY_PORTLET_XML = "liferay-portlet.xml";
	public static final String LIFERAY_PORTLET_FTL = "liferay-portlet.ftl";
	public static final String PORTLET_XML = "portlet.xml";
	public static final String VIEW_JSP = "view.jsp";
	public static final String BUILD_XML = "build.xml";
	public static final String _CLASSPATH = ".classpath";
	public static final String _PROJECT = ".project";
	public static final String PORTLET_FTL = "portlet.ftl";
	public static final String VIEW_FTL = "view.ftl";
	public static final String BUILD_FTL = "build.ftl";
	public static final String CLASSPATH_FTL = "classpath.ftl";
	public static final String PROJECT_FTL = "project.ftl";
	public static final String ICON_PNG = "icon.png";
	public static final String WEB_XML = "web.xml";
	public static final String LIFERAY_UTIL_TLD = "liferay-util.tld";
	public static final String LIFERAY_UI_TLD = "liferay-ui.tld";
	public static final String LIFERAY_THEME_TLD = "liferay-theme.tld";
	public static final String LIFERAY_SECURITY_TLD = "liferay-security.tld";
	public static final String LIFERAY_PORTLET_TLD = "liferay-portlet.tld";
	public static final String LIFERAY_PORTLET_EXT_TLD = "liferay-portlet-ext.tld";
	public static final String AUI_TLD = "aui.tld";
	public static final String APPLICATION_CONTEXT_XML = "application-context.xml";
	public static final String MANIFEST_MF = "MANIFEST.MF";
	public static final String ICON2 = "icon";
	public static final String _ICON_PNG = "/icon.png";
	public static final String HEADER_PORTLET_CSS = "header-portlet-css";
	public static final String FOOTER_PORTLET_JAVASCRIPT = "footer-portlet-javascript";
	public static final String CSS_CLASS_WRAPPER = "css-class-wrapper";
	public static final String MAIN_JS = "main.js";
	public static final String MAIN_CSS = "main.css";
	public static final String JS_MAIN_JS = "/js/main.js";
	public static final String CSS_MAIN_CSS = "/css/main.css";
	public static final String APPLICATION_CONTEXT_XML_PATH = "docroot/WEB-INF/spring-context/application-context.xml";
	public static final String MVN_APPLICATION_CONTEXT_XML_PATH = "src/main/webapp/WEB-INF/spring-context/application-context.xml";
	public static final String FORMAT_PRETTY_PRINT = "format-pretty-print";
	public static final String PORTLET__NAME = "portlet-name";
	public static final String CATEGORY = "category";
	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String __PORTLET2 = "-portlet";
	public static final String MIME_TYPE = "mime-type";
	public static final String TEXT_HTML = "text/html";

	public static final String LOGGER_STATEMENT = "loggerStatement";
	public static final String LOGGER_DECLARATION = "loggerDeclaration";
	public static final String LOGGER_IMPORT = "loggerImport";
	public static final String EXCEPTION_STATEMENT = "exceptionStatement";
	public static final String EXCEPTION_METHOD = "exceptionMethod";
	public static final String EXCEPTION_IMPORT = "exceptionImport";
	public static final String PORTLET_CLASS = "portletClass";
	public static final String VIEW_PATH = "viewPath";
	public static final String DISPLAY_CATEGORY = "displayCategory";
	public static final String PORTLET_CONFIG_NAME = "portletConfigName";
	public static final String PORTLET_NAME = "portletName";
	public static final String PORTLET_DISPLAY_NAME = "portletDisplayName";
	public static final String PROJECT_NAME = "projectName";
	public static final String PACKAGE_NAME = "packageName";
	
	public static final String EMPTY = "";
	public static final String DOT = ".";
	public static final String WIZARD_PAGE = "wizardPage";

	public static final String PORTLET = "portlet";
	public static final String CREATING = "Creating ";	

	public static final String PORTLETS_LBL = "portlets";

	public static final String FREEMARKET_VERSION = "2.3.0";

}
