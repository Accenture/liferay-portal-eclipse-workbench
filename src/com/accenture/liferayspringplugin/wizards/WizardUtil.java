package com.accenture.liferayspringplugin.wizards;

import static com.accenture.liferayspringplugin.wizards.Constants.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URL;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

import com.accenture.liferayspringplugin.wizards.plugin.LoggerWizardPage;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

/**
 * This calss hase utitlity methods which required in wizard classes.
 * 
 * @author Digital Liferay Capability IDC
 * 
 */
public class WizardUtil {
	

	/**
	 * This method will append "portlet" entry in existing portlet.xml file.
	 * 
	 * @param filePath
	 * @param portletName
	 * @param portletDisplayName
	 * @param portletConfigName
	 * @return
	 */
	public static boolean addPortletEntry(String filePath, String portletName,
			String portletDisplayName, String portletConfigName) {
		try {
			Document doc = getDoc(filePath);

			// Get the root element
			Node portletApp = doc.getFirstChild();

			Element portlet = doc.createElement(PORTLET);

			Element pName = doc.createElement(PORTLET__NAME);
			pName.appendChild(doc.createTextNode(portletName));
			portlet.appendChild(pName);

			Element dName = doc.createElement("display-name");
			dName.appendChild(doc.createTextNode(portletDisplayName));
			portlet.appendChild(dName);

			Element pClass = doc.createElement("portlet-class");
			pClass.appendChild(doc.createTextNode("org.springframework.web.portlet.DispatcherPortlet"));
			portlet.appendChild(pClass);

			Element initParam = doc.createElement("init-param");
			Element paramName = doc.createElement(NAME);
			paramName.appendChild(doc.createTextNode("contextConfigLocation"));
			Element paramValue = doc.createElement("value");
			paramValue.appendChild(doc.createTextNode(WEB_INF_SPRING+portletConfigName+_PORTLET_CONTEXT_XML));
			initParam.appendChild(paramName);
			initParam.appendChild(paramValue);
			portlet.appendChild(initParam);

			Element exCache = doc.createElement("expiration-cache");
			exCache.appendChild(doc.createTextNode("0"));
			portlet.appendChild(exCache);

			Element supports = doc.createElement("supports");
			Element mineType = doc.createElement(MIME_TYPE);
			mineType.appendChild(doc.createTextNode(TEXT_HTML));
			supports.appendChild(mineType);
			portlet.appendChild(supports);

			Element pInfo = doc.createElement("portlet-info");
			Element title = doc.createElement("title");
			title.appendChild(doc.createTextNode(portletDisplayName));
			Element sTitle = doc.createElement("short-title");
			sTitle.appendChild(doc.createTextNode(portletDisplayName));
			Element keywords = doc.createElement("keywords");
			keywords.appendChild(doc.createTextNode(portletDisplayName));
			pInfo.appendChild(title);
			pInfo.appendChild(sTitle);
			pInfo.appendChild(keywords);
			portlet.appendChild(pInfo);

			Element AdminRoleRef = doc.createElement("security-role-ref");
			Element aRoleName = doc.createElement("role-name");
			aRoleName.appendChild(doc.createTextNode("administrator"));
			AdminRoleRef.appendChild(aRoleName);
			portlet.appendChild(AdminRoleRef);

			Element guestRoleRef = doc.createElement("security-role-ref");
			Element gRoleName = doc.createElement("role-name");
			gRoleName.appendChild(doc.createTextNode("guest"));
			guestRoleRef.appendChild(gRoleName);
			portlet.appendChild(guestRoleRef);

			Element pUserRoleRef = doc.createElement("security-role-ref");
			Element pRoleName = doc.createElement("role-name");
			pRoleName.appendChild(doc.createTextNode("power-user"));
			pUserRoleRef.appendChild(pRoleName);
			portlet.appendChild(pUserRoleRef);

			Element userRoleRef = doc.createElement("security-role-ref");
			Element uRoleName = doc.createElement("role-name");
			uRoleName.appendChild(doc.createTextNode("user"));
			userRoleRef.appendChild(uRoleName);
			portlet.appendChild(userRoleRef);

			portletApp.appendChild(portlet);
			
			// write the content into xml file
			writeXML(filePath, doc);
			
			return true;
		} catch (Exception e) {
			log(e);
		}
		return false;
	}

	/**
	 * This method write xml document in provided path.
	 * 
	 * @param filePath
	 * @param doc
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void writeXML(String filePath, Document doc) throws FileNotFoundException, IOException {
		DOMImplementationLS domImplementationLS = (DOMImplementationLS) doc.getImplementation().getFeature("LS", "3.0");

		LSOutput lsOutput = domImplementationLS.createLSOutput();
		FileOutputStream outputStream = new FileOutputStream(filePath);
		lsOutput.setByteStream((OutputStream) outputStream);
		LSSerializer lsSerializer = domImplementationLS.createLSSerializer();
		lsSerializer.getDomConfig().setParameter(FORMAT_PRETTY_PRINT, Boolean.TRUE);
		lsSerializer.write(doc, lsOutput);
		outputStream.close();
	}

	/**
	 * This method will append "portlet" entry in existing liferay-portlet.xml file.
	 * 
	 * @param filePath
	 * @param portletName
	 * @param portletClass
	 * @return
	 */
	public static boolean addLiferayPortletEntry(String filePath, String portletName,
			String portletClass) {
		try {
			Document doc = getDoc(filePath);

			// Get the root element
			Node lrPortletApp = doc.getLastChild();

			NodeList childNodes = lrPortletApp.getChildNodes();
			
			Node rollMapper = null;
			for (int i=0; i < childNodes.getLength(); i++) {
				Node lPortlet = childNodes.item(i);
				if(!PORTLET.equals(lPortlet.getNodeName())) {
					rollMapper = lPortlet;
					break;
				}
			}
			if(rollMapper == null) {
				return false;
			}

			Element portlet = doc.createElement(PORTLET);

			Element pName = doc.createElement(PORTLET__NAME);
			pName.appendChild(doc.createTextNode(portletName));
			portlet.appendChild(pName);

			Element icon = doc.createElement(ICON2);
			icon.appendChild(doc.createTextNode(_ICON_PNG));
			portlet.appendChild(icon);

			Element headerCSS = doc.createElement(HEADER_PORTLET_CSS);
			headerCSS.appendChild(doc.createTextNode(CSS_MAIN_CSS));
			portlet.appendChild(headerCSS);

			Element headerJS = doc.createElement(FOOTER_PORTLET_JAVASCRIPT);
			headerJS.appendChild(doc.createTextNode(JS_MAIN_JS));
			portlet.appendChild(headerJS);

			Element cssClassWrapper = doc.createElement(CSS_CLASS_WRAPPER);
			cssClassWrapper.appendChild(doc.createTextNode(portletClass));
			portlet.appendChild(cssClassWrapper);

			lrPortletApp.insertBefore(portlet, rollMapper);

			// write the content into xml file			
			writeXML(filePath, doc);

		} catch (Exception e) {
			log(e);
		}
		return false;
	}

	/**
	 * This method will append "portlet" entry in existing liferay-display.xml file.
	 * 
	 * @param filePath
	 * @param portletName
	 * @param displayCategory
	 * @return
	 */
	public static boolean addLiferayDisplayEntry(String filePath, String portletName,
			String displayCategory) {
		try {
			Document doc = getDoc(filePath);

			// Get the root element
			Node display = doc.getLastChild();
			boolean isCategoryExist = false;
			NodeList childNodes = display.getChildNodes();
			for(int i=0; i < childNodes.getLength(); i++) {
				if(CATEGORY.equals(childNodes.item(i).getNodeName()) && childNodes.item(i).getAttributes() != null && displayCategory.equals(childNodes.item(i).getAttributes().getNamedItem(NAME).getNodeValue())) {
					Element portlet = doc.createElement(PORTLET);
					portlet.setAttribute(ID, portletName);
					childNodes.item(i).appendChild(portlet);
					isCategoryExist = true;
					break;
				}
			}

			if(!isCategoryExist){
				Element category = doc.createElement(CATEGORY);
				category.setAttribute(NAME, displayCategory);
				
				Element portlet = doc.createElement(PORTLET);
				portlet.setAttribute(ID, portletName);
				category.appendChild(portlet);

				display.appendChild(category);
			}
			
			writeXML(filePath, doc);

		} catch (Exception e) {
			log(e);
		}
		return false;
	}

	/**
	 * This method load XML from provided file path and return document.
	 * 
	 * @param filePath
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private static Document getDoc(String filePath) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(filePath);
		return doc;
	}

	/**
	 * This method parse provided freemarker template and write file in provided path.
	 * 
	 * @param cfg
	 * @param input
	 * @param projectFolder
	 * @param filePath
	 * @param ftlName
	 * @param outputFileName
	 * @throws TemplateNotFoundException
	 * @throws MalformedTemplateNameException
	 * @throws ParseException
	 * @throws IOException
	 * @throws TemplateException
	 */
	public static void addFileFromTemplate(Configuration cfg, Map<String, Object> input,
			File projectFolder, String filePath, String ftlName, String outputFileName) 
					throws TemplateNotFoundException, MalformedTemplateNameException, 
					ParseException, IOException, TemplateException {
		File sourceFolder = projectFolder;
		if(filePath != null && filePath.trim().length() > 0) {
			sourceFolder = new File(projectFolder, filePath);
			if(!sourceFolder.exists()) {
				sourceFolder.mkdirs();
			}
		}
		// 2.2. Get the template

		Template template = cfg.getTemplate(ftlName);
		// 2.3. Generate the output

		// Write output to the file:
		Writer fileWriter = new FileWriter(new File(sourceFolder,
				outputFileName));
		try {
			template.process(input, fileWriter);
		} catch(Exception e){
			log(e);
		} finally {
			fileWriter.close();
		}
	}

	/**
	 * 
	 * @param sel
	 * @return
	 */
	public IResource extractSelection(ISelection sel) {
	      if (!(sel instanceof IStructuredSelection))
	         return null;
	      IStructuredSelection ss = (IStructuredSelection) sel;
	      Object element = ss.getFirstElement();
	      if (element instanceof IResource)
	         return (IResource) element;
	      if (!(element instanceof IAdaptable))
	         return null;
	      IAdaptable adaptable = (IAdaptable)element;
	      Object adapter = adaptable.getAdapter(IResource.class);
	      return (IResource) adapter;
	   }

	/**
	 * 
	 * @param sdkPath
	 * @param projectName
	 * @param portletName
	 * @return
	 */
	public static boolean isPortletExist(String sdkPath, String projectName, String portletName) {
		File file = new File(sdkPath + PORTLETS);
		File projectFolder = new File(file, projectName + DOCROOT_WEB_INF_SPRING + portletName + _PORTLET_CONTEXT_XML);
		
		return projectFolder.exists();
	}

	/**
	 * 
	 * @param sdkPath
	 * @param projectName
	 * @return
	 */
	public static boolean isProjectExist(String sdkPath, String projectName) {
		if(!projectName.endsWith(PORTLET)) {
			projectName = projectName + __PORTLET2;
		}
		File file = new File(sdkPath + PORTLETS);
		File projectFolder = new File(file, projectName);
		return projectFolder.exists();
	}

	/**
	 * 
	 * @param sdkPath
	 * @param isEnabledSDK
	 * @return
	 */
	public static File getPluginLocation(String sdkPath, boolean isEnabledSDK){
		if(isEnabledSDK) {
			return new File(sdkPath + PORTLETS);
		}
		return new File(sdkPath);
	}

	/**
	 * 
	 * @param viewPath
	 * @param projectFolder
	 * @param fPath
	 * @param cfg
	 * @param input
	 * @param portletConfigName
	 * @param pClass
	 * @param pluginMainException
	 * @param srcFolder
	 * @param lPage
	 * @throws TemplateNotFoundException
	 * @throws MalformedTemplateNameException
	 * @throws ParseException
	 * @throws IOException
	 * @throws TemplateException
	 */
	public static void createAntPlugin(String viewPath, File projectFolder,
			URL fPath, Configuration cfg, Map<String, Object> input,
			String portletConfigName, String pClass,
			String pluginMainException, String srcFolder, LoggerWizardPage lPage)
			throws TemplateNotFoundException, MalformedTemplateNameException,
			ParseException, IOException, TemplateException {
		viewPath = DOCROOT_PATH + viewPath;
		try {
			URL resolve = FileLocator.resolve(fPath);

			File srcFile = new File(resolve.toURI());
			copyFolder(srcFile, projectFolder);
		} catch (Exception e) {
			log(e);
		}
		
		addFileFromTemplate(cfg, input, projectFolder, "/docroot/css/", MAIN_CSS, MAIN_CSS);
		addFileFromTemplate(cfg, input, projectFolder, "/docroot/js/", MAIN_JS, MAIN_JS);
		addFileFromTemplate(cfg, input, projectFolder, "/docroot/META-INF/", MANIFEST_MF, MANIFEST_MF);
		addFileFromTemplate(cfg, input, projectFolder, DOCROOT_WEB_INF + _SPRING, APPLICATION_CONTEXT_XML, APPLICATION_CONTEXT_XML);
		addFileFromTemplate(cfg, input, projectFolder, DOCROOT_WEB_INF_TLD, AUI_TLD, AUI_TLD);
		addFileFromTemplate(cfg, input, projectFolder, DOCROOT_WEB_INF_TLD, LIFERAY_PORTLET_EXT_TLD, LIFERAY_PORTLET_EXT_TLD);
		addFileFromTemplate(cfg, input, projectFolder, DOCROOT_WEB_INF_TLD, LIFERAY_PORTLET_TLD, LIFERAY_PORTLET_TLD);
		addFileFromTemplate(cfg, input, projectFolder, DOCROOT_WEB_INF_TLD, LIFERAY_SECURITY_TLD, LIFERAY_SECURITY_TLD);
		addFileFromTemplate(cfg, input, projectFolder, DOCROOT_WEB_INF_TLD, LIFERAY_THEME_TLD, LIFERAY_THEME_TLD);
		addFileFromTemplate(cfg, input, projectFolder, DOCROOT_WEB_INF_TLD, LIFERAY_UI_TLD, LIFERAY_UI_TLD);
		addFileFromTemplate(cfg, input, projectFolder, DOCROOT_WEB_INF_TLD, LIFERAY_UTIL_TLD, LIFERAY_UTIL_TLD);
		addFileFromTemplate(cfg, input, projectFolder, ANT_WEB_INF_PATH, WEB_XML, WEB_XML);
		addFileFromTemplate(cfg, input, projectFolder, "/docroot/", ICON_PNG, ICON_PNG);

		addFileFromTemplate(cfg, input, projectFolder, null, PROJECT_FTL, _PROJECT);	
		addFileFromTemplate(cfg, input, projectFolder, null, CLASSPATH_FTL, _CLASSPATH);	
		addFileFromTemplate(cfg, input, projectFolder, null, BUILD_FTL, BUILD_XML);
		addFileFromTemplate(cfg, input, projectFolder, viewPath, VIEW_FTL, VIEW_JSP);
		addFileFromTemplate(cfg, input, projectFolder, DOCROOT_WEB_INF, PORTLET_FTL, PORTLET_XML);
		addFileFromTemplate(cfg, input, projectFolder, DOCROOT_WEB_INF, LIFERAY_PORTLET_FTL, LIFERAY_PORTLET_XML);
		addFileFromTemplate(cfg, input, projectFolder, DOCROOT_WEB_INF, LIFERAY_PLUGIN_PACKAGE_FTL, LIFERAY_PLUGIN_PACKAGE_PROPERTIES);
		addFileFromTemplate(cfg, input, projectFolder, DOCROOT_WEB_INF, LIFERAY_DISPLAY_FTL, LIFERAY_DISPLAY_XML);
		addFileFromTemplate(cfg, input, projectFolder, srcFolder, SAMPLE_CONTROLLER_FTL, pClass + ".java");
		addFileFromTemplate(cfg, input, projectFolder, DOCROOT_WEB_INF + _SPRING, SPRING_MVC_PORTLET_CONTEXT_FTL, portletConfigName + _PORTLET_CONTEXT_XML);

		if(lPage.isLogEnabled()) {

			writeFile(projectFolder, ANT_WEB_INF_PATH, LIB, "slf4j-api-1.7.12.jar", "/com/accenture/liferayspringplugin/wizards/tpl/lib/slf4j-api-1.7.12.jar");
			
			if(lPage.isSimpleLogger()) {
				writeFile(projectFolder, ANT_WEB_INF_PATH, LIB, "slf4j-simple-1.7.12.jar", "/com/accenture/liferayspringplugin/wizards/tpl/lib/slf4j-api-1.7.12.jar");
			} else if(lPage.isLog4jLogger()) {
				writeFile(projectFolder, ANT_WEB_INF_PATH, LIB, "slf4j-log4j12-1.7.12.jar", "/com/accenture/liferayspringplugin/wizards/tpl/lib/slf4j-log4j12-1.7.12.jar");
				writeFile(projectFolder, ANT_WEB_INF_PATH, "src", "log4j.properties", "/com/accenture/liferayspringplugin/wizards/tpl/lib/log4j.properties");
			} else if(lPage.isJdk14Logger()) {
				writeFile(projectFolder, ANT_WEB_INF_PATH, LIB, "slf4j-jdk14-1.7.12.jar", "/com/accenture/liferayspringplugin/wizards/tpl/lib/slf4j-jdk14-1.7.12.jar");
				writeFile(projectFolder, ANT_WEB_INF_PATH, "src", "logging.properties", "/com/accenture/liferayspringplugin/wizards/tpl/lib/logging.properties");
			} else if(lPage.isLogbackLogger()) {
				writeFile(projectFolder, ANT_WEB_INF_PATH, LIB, "logback-classic-1.1.3.jar", "/com/accenture/liferayspringplugin/wizards/tpl/lib/logback-classic-1.1.3.jar");
				writeFile(projectFolder, ANT_WEB_INF_PATH, LIB, "logback-core-1.1.3.jar", "/com/accenture/liferayspringplugin/wizards/tpl/lib/logback-core-1.1.3.jar");
				writeFile(projectFolder, ANT_WEB_INF_PATH, "src", "logback.xml", "/com/accenture/liferayspringplugin/wizards/tpl/lib/logback.xml");
			}
		}

		if(lPage.isExceptionEnabled()) {
			input.put("pluginMainException", pluginMainException);
			addFileFromTemplate(cfg, input, projectFolder, srcFolder + SLASH + EXCEPTION, "DuplicateEmail.ftl", "DuplicateUserEmailAddressException.java");
			addFileFromTemplate(cfg, input, projectFolder, srcFolder + SLASH + EXCEPTION, "EmailAddress.ftl", "EmailAddressException.java");
			addFileFromTemplate(cfg, input, projectFolder, srcFolder + SLASH + EXCEPTION, "NoSuchEmail.ftl", "NoSuchEmailAddressException.java");
			addFileFromTemplate(cfg, input, projectFolder, srcFolder + SLASH + EXCEPTION, "NoSuchModel.ftl", "NoSuchModelException.java");
			addFileFromTemplate(cfg, input, projectFolder, srcFolder + SLASH + EXCEPTION, "Password.ftl", "PasswordExpiredException.java");
			addFileFromTemplate(cfg, input, projectFolder, srcFolder + SLASH + EXCEPTION, "WebsiteURL.ftl", "WebsiteURLException.java");
			addFileFromTemplate(cfg, input, projectFolder, srcFolder + SLASH + EXCEPTION, "MainException.ftl", pluginMainException + ".java");
		}
	}

	/**
	 * 
	 * @param viewPath
	 * @param projectFolder
	 * @param fPath
	 * @param cfg
	 * @param input
	 * @param portletConfigName
	 * @param pClass
	 * @param pluginMainException
	 * @param srcFolder
	 * @param lPage
	 * @throws TemplateNotFoundException
	 * @throws MalformedTemplateNameException
	 * @throws ParseException
	 * @throws IOException
	 * @throws TemplateException
	 */
	public static void createMavenPlugin(String viewPath, File projectFolder,
			URL fPath, Configuration cfg, Map<String, Object> input,
			String portletConfigName, String pClass,
			String pluginMainException, String srcFolder, LoggerWizardPage lPage)
			throws TemplateNotFoundException, MalformedTemplateNameException,
			ParseException, IOException, TemplateException {
		viewPath = MAVEN_WEBAPP_PATH + viewPath;
		try {
			URL resolve = FileLocator.resolve(fPath);

			File srcFile = new File(resolve.toURI());
			copyFolder(srcFile, projectFolder);
		} catch (Exception e) {
			log(e);
		}
		
		addFileFromTemplate(cfg, input, projectFolder, "/src/main/webapp/css/", MAIN_CSS, MAIN_CSS);
		addFileFromTemplate(cfg, input, projectFolder, "/src/main/webapp/js/", MAIN_JS, MAIN_JS);
		//addFileFromTemplate(cfg, input, projectFolder, "/docroot/META-INF/", MANIFEST_MF, MANIFEST_MF);
		addFileFromTemplate(cfg, input, projectFolder, "/src/main/webapp/WEB-INF/spring-context/", APPLICATION_CONTEXT_XML, APPLICATION_CONTEXT_XML);
		addFileFromTemplate(cfg, input, projectFolder, MAVEN_WEB_INF_PATH + SLASH, WEB_XML, WEB_XML);
		addFileFromTemplate(cfg, input, projectFolder, "/src/main/webapp/", ICON_PNG, ICON_PNG);

		/*addFileFromTemplate(cfg, input, projectFolder, null, PROJECT_FTL, _PROJECT);
		addFileFromTemplate(cfg, input, projectFolder, null, CLASSPATH_FTL, _CLASSPATH);*/
		//addFileFromTemplate(cfg, input, projectFolder, null, BUILD_FTL, BUILD_XML);
		addFileFromTemplate(cfg, input, projectFolder, viewPath, VIEW_FTL, VIEW_JSP);
		addFileFromTemplate(cfg, input, projectFolder, MAVEN_WEB_INF_PATH, PORTLET_FTL, PORTLET_XML);
		addFileFromTemplate(cfg, input, projectFolder, MAVEN_WEB_INF_PATH, LIFERAY_PORTLET_FTL, LIFERAY_PORTLET_XML);
		addFileFromTemplate(cfg, input, projectFolder, MAVEN_WEB_INF_PATH, LIFERAY_PLUGIN_PACKAGE_FTL, LIFERAY_PLUGIN_PACKAGE_PROPERTIES);
		addFileFromTemplate(cfg, input, projectFolder, MAVEN_WEB_INF_PATH, LIFERAY_DISPLAY_FTL, LIFERAY_DISPLAY_XML);
		addFileFromTemplate(cfg, input, projectFolder, srcFolder, SAMPLE_CONTROLLER_FTL, pClass + ".java");
		addFileFromTemplate(cfg, input, projectFolder, MAVEN_WEB_INF_PATH + _SPRING, SPRING_MVC_PORTLET_CONTEXT_FTL, portletConfigName + _PORTLET_CONTEXT_XML);

		String loggerDependencies = EMPTY;
		if(lPage.isLogEnabled()) {
			loggerDependencies = getLoggerDependencies(lPage, projectFolder);
		}
		input.put("logger", loggerDependencies);

		if(lPage.isExceptionEnabled()) {
			input.put("pluginMainException", pluginMainException);
			addFileFromTemplate(cfg, input, projectFolder, srcFolder + SLASH + EXCEPTION, "DuplicateEmail.ftl", "DuplicateUserEmailAddressException.java");
			addFileFromTemplate(cfg, input, projectFolder, srcFolder + SLASH + EXCEPTION, "EmailAddress.ftl", "EmailAddressException.java");
			addFileFromTemplate(cfg, input, projectFolder, srcFolder + SLASH + EXCEPTION, "NoSuchEmail.ftl", "NoSuchEmailAddressException.java");
			addFileFromTemplate(cfg, input, projectFolder, srcFolder + SLASH + EXCEPTION, "NoSuchModel.ftl", "NoSuchModelException.java");
			addFileFromTemplate(cfg, input, projectFolder, srcFolder + SLASH + EXCEPTION, "Password.ftl", "PasswordExpiredException.java");
			addFileFromTemplate(cfg, input, projectFolder, srcFolder + SLASH + EXCEPTION, "WebsiteURL.ftl", "WebsiteURLException.java");
			try{
				addFileFromTemplate(cfg, input, projectFolder, srcFolder + SLASH + EXCEPTION, "MainException.ftl", pluginMainException + ".java");
			} catch(Exception e){
				log(e);
			}
		} else {
			//Create a blank exception folder for future use
			File file = new File(projectFolder, srcFolder + SLASH + EXCEPTION);
			if(!file.exists()) {
				file.mkdirs();
			}
		}
		input.put("d", "$");
		addFileFromTemplate(cfg, input, projectFolder, null, "pom.tpl", "pom.xml");
	}

	public static void log(Exception e) {
		Bundle bundle = FrameworkUtil.getBundle(WizardUtil.class);
		ILog log = Platform.getLog(bundle);
		IStatus status = new Status(IStatus.ERROR, bundle.getSymbolicName(), e.getMessage(), e);
		log.log(status);
	}

	/**
	 * 
	 * @param lPage
	 * @param projectFolder
	 * @return
	 */
	private static String getLoggerDependencies(LoggerWizardPage lPage, File projectFolder) {
		StringBuilder logger = new StringBuilder();
		appendDependency(logger, "org.slf4j", "slf4j-api", "1.7.18");
		
		if(lPage.isSimpleLogger()) {
			appendDependency(logger, "org.slf4j", "slf4j-simple", "1.7.18");
		} else if(lPage.isLog4jLogger()) {
			appendDependency(logger, "org.slf4j", "slf4j-log4j12", "1.7.18");
			writeFile(projectFolder, MAVEN_JAVA_PATH, EMPTY, "log4j.properties", "/com/accenture/liferayspringplugin/wizards/tpl/lib/log4j.properties");
		} else if(lPage.isJdk14Logger()) {
			appendDependency(logger, "org.slf4j", "slf4j-jdk14", "1.7.18");
			writeFile(projectFolder, MAVEN_JAVA_PATH, EMPTY, "logging.properties", "/com/accenture/liferayspringplugin/wizards/tpl/lib/logging.properties");
		} else if(lPage.isLogbackLogger()) {
			appendDependency(logger, "ch.qos.logback", "logback-core", "1.1.6");
			appendDependency(logger, "ch.qos.logback", "logback-classic", "1.1.6");
			writeFile(projectFolder, MAVEN_JAVA_PATH, EMPTY, "logback.xml", "/com/accenture/liferayspringplugin/wizards/tpl/lib/logback.xml");
		}

		return logger.toString();
	}

	/**
	 * 
	 * @param logger
	 * @param groupId
	 * @param artifactId
	 * @param version
	 */
	private static void appendDependency(StringBuilder logger, String groupId,
			String artifactId, String version) {
		logger.append("\n\t\t").append("<dependency>").append("\n\t\t\t")
				.append("<groupId>").append(groupId).append("</groupId>")
				.append("\n\t\t\t\t").append("<artifactId>").append(artifactId)
				.append("</artifactId>").append("\n\t\t\t\t")
				.append("<version>").append(version).append("</version>")
				.append("\n\t\t").append("</dependency>");
	}

	/**
	 * 
	 * @param projectFolder
	 * @param midPath
	 * @param subFolder
	 * @param fileName
	 * @param srcName
	 */
	private static void writeFile(File projectFolder, String midPath, String subFolder, String fileName, String srcName) {
		InputStream in = null;
		OutputStream out = null;
		try {
			File sourceFolder = new File(projectFolder, midPath + subFolder);
			if (!sourceFolder.exists()) {
				sourceFolder.mkdirs();
			}
			File souFile = new File(sourceFolder, fileName);
			in = LiferaySpringWizard.class.getClassLoader()
					.getResourceAsStream(srcName);
			out = new FileOutputStream(souFile);
			byte[] buffer = new byte[1024];
			int len = in.read(buffer);
			while (len != -1) {
				out.write(buffer, 0, len);
				len = in.read(buffer);
			}
		} catch (Exception e) {
			log(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					log(e);
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					log(e);
				}
			}
		}
	}

	/**
	 * 
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	private static void copyFolder(File src, File dest) throws IOException {

		if (src.isDirectory()) {

			// if directory not exists, create it
			if (!dest.exists()) {
				dest.mkdir();
			}

			// list all the directory contents
			String files[] = src.list();

			for (String file : files) {
				// construct the src and dest file structure
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				// recursive copy
				copyFolder(srcFile, destFile);
			}

		} else {
			// if file, then copy it
			// Use bytes stream to support all file types
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dest);

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}

			in.close();
			out.close();
		}
	}

	/**
	 * 
	 * @param sdkPath
	 * @param projectName
	 * @param portletName
	 * @param viewPath
	 * @param displayCategory
	 * @param projectFolder
	 * @param cfg
	 * @param input
	 * @param portletDisplayName
	 * @param portletConfigName
	 * @param pClass
	 * @param isExceptionEnabled
	 * @param pluginMainException
	 * @param srcFolder
	 * @param isSDKEnabled
	 * @throws TemplateNotFoundException
	 * @throws MalformedTemplateNameException
	 * @throws ParseException
	 * @throws IOException
	 * @throws TemplateException
	 */
	public static void addAntPortlet(String sdkPath, String projectName,
			String portletName, String viewPath, String displayCategory,
			File projectFolder, Configuration cfg, Map<String, Object> input,
			String portletDisplayName, String portletConfigName, String pClass,
			boolean isExceptionEnabled, String pluginMainException,
			String srcFolder, boolean isSDKEnabled) throws TemplateNotFoundException,
			MalformedTemplateNameException, ParseException, IOException,
			TemplateException {
		viewPath = DOCROOT_PATH + viewPath;
		addFileFromTemplate(cfg, input, projectFolder, viewPath, VIEW_FTL, VIEW_JSP);
		addFileFromTemplate(cfg, input, projectFolder, srcFolder, SAMPLE_CONTROLLER_FTL, pClass + ".java");
		addFileFromTemplate(cfg, input, projectFolder, DOCROOT_WEB_INF + _SPRING, SPRING_MVC_PORTLET_CONTEXT_FTL, portletConfigName + _PORTLET_CONTEXT_XML);

		String midPath = SLASH;
		if(isSDKEnabled) {
			midPath = _PORTLETS_;
		}
		addPortletEntry(sdkPath + midPath + projectName + "/docroot/WEB-INF/portlet.xml", portletName, portletDisplayName, portletConfigName);
		addLiferayPortletEntry(sdkPath + midPath + projectName + "/docroot/WEB-INF/liferay-portlet.xml", portletName, pClass);
		addLiferayDisplayEntry(sdkPath + midPath + projectName + "/docroot/WEB-INF/liferay-display.xml", portletName, displayCategory);

		if(isExceptionEnabled) {
			input.put("pluginMainException", pluginMainException);
			addFileFromTemplate(cfg, input, projectFolder, srcFolder + SLASH + EXCEPTION, "MainException.ftl", pluginMainException + ".java");
		}
	}

	/**
	 * 
	 * @param sdkPath
	 * @param projectName
	 * @param portletName
	 * @param viewPath
	 * @param displayCategory
	 * @param projectFolder
	 * @param cfg
	 * @param input
	 * @param portletDisplayName
	 * @param portletConfigName
	 * @param pClass
	 * @param isExceptionEnabled
	 * @param pluginMainException
	 * @param srcFolder
	 * @throws TemplateNotFoundException
	 * @throws MalformedTemplateNameException
	 * @throws ParseException
	 * @throws IOException
	 * @throws TemplateException
	 */
	public static void addMavenPortlet(String sdkPath, String projectName,
			String portletName, String viewPath, String displayCategory,
			File projectFolder, Configuration cfg, Map<String, Object> input,
			String portletDisplayName, String portletConfigName, String pClass,
			boolean isExceptionEnabled, String pluginMainException,
			String srcFolder) throws TemplateNotFoundException,
			MalformedTemplateNameException, ParseException, IOException,
			TemplateException {
		viewPath = MAVEN_WEBAPP_PATH + viewPath;
		addFileFromTemplate(cfg, input, projectFolder, viewPath, VIEW_FTL, VIEW_JSP);
		addFileFromTemplate(cfg, input, projectFolder, srcFolder, SAMPLE_CONTROLLER_FTL, pClass + ".java");
		addFileFromTemplate(cfg, input, projectFolder,  MAVEN_WEB_INF_PATH + _SPRING, SPRING_MVC_PORTLET_CONTEXT_FTL, portletConfigName + _PORTLET_CONTEXT_XML);

		addPortletEntry(sdkPath + SLASH + projectName + MAVEN_WEB_INF_PATH + "/portlet.xml", portletName, portletDisplayName, portletConfigName);
		addLiferayPortletEntry(sdkPath + SLASH + projectName + MAVEN_WEB_INF_PATH + "/liferay-portlet.xml", portletName, pClass);
		addLiferayDisplayEntry(sdkPath + SLASH + projectName + MAVEN_WEB_INF_PATH + "/liferay-display.xml", portletName, displayCategory);

		if(isExceptionEnabled) {
			input.put("pluginMainException", pluginMainException);
			addFileFromTemplate(cfg, input, projectFolder, srcFolder + SLASH + EXCEPTION, "MainException.ftl", pluginMainException + ".java");
		}
	}
}
