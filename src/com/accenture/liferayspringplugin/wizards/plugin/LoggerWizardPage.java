package com.accenture.liferayspringplugin.wizards.plugin;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import static com.accenture.liferayspringplugin.wizards.Constants.*;

/**
 * The "LiferaySpring" wizard page allows setting the portlet/project name and
 * other required settings.
 * 
 * @author Digital Liferay Capability IDC
 */

public class LoggerWizardPage extends WizardPage {
	private Button enableLog;
	private Button enableException;
	private Button simpleLogger;
	private Button log4jLogger;
	private Button jdk14Logger;
	private Button logbackLogger;

	private boolean isLogEnabled = true;
	private boolean isSimpleLogger = false;
	private boolean isLog4jLogger = true;
	private boolean isJdk14Logger = false;
	private boolean isLogbackLogger = false;
	private boolean isExceptionEnabled = true;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public LoggerWizardPage() {
		super(WIZARD_PAGE);
		setTitle(LOGGER_PAGE_WIZARD_TITLE);
		setDescription(LOGGER_PAGE_WIZARD_DESC);
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 4;
		layout.verticalSpacing = 9;

		Label label = new Label(container, SWT.NULL);
		label.setText(_ENABLE_SLF4J_LABEL);

		enableLog = new Button(container, SWT.CHECK);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		enableLog.setLayoutData(gd);
		enableLog.addSelectionListener(new SelectionListener() {			
			@Override
			public void widgetSelected(SelectionEvent e) {
				isLogEnabled = enableLog.getSelection();
				simpleLogger.setEnabled(isLogEnabled);
				log4jLogger.setEnabled(isLogEnabled);
				jdk14Logger.setEnabled(isLogEnabled);
				logbackLogger.setEnabled(isLogEnabled);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {				
			}
		});
		label = new Label(container, SWT.NULL);
		label.setText(EMPTY);
		label = new Label(container, SWT.NULL);
		label.setText(EMPTY);
		
		Listener listener = new Listener () {
			@Override
			public void handleEvent (Event e) {
				Control [] children = container.getChildren ();
				for (int i=0; i<children.length; i++) {
					Control child = children [i];
					if (e.widget != child && child instanceof Button && (child.getStyle () & SWT.RADIO) != 0) {
						((Button) child).setSelection (false);
					}
				}
				((Button) e.widget).setSelection (true);
				
				isSimpleLogger = simpleLogger.getSelection();
				isLog4jLogger = log4jLogger.getSelection();
				isJdk14Logger = jdk14Logger.getSelection();
				isLogbackLogger = logbackLogger.getSelection();
			}
		};

		label = new Label(container, SWT.NULL);
		label.setText(LOGGER_IMPLEMENTATION_TYPE_LABEL);
		label = new Label(container, SWT.NULL);
		label.setText(EMPTY);
		label = new Label(container, SWT.NULL);
		label.setText(EMPTY);
		label = new Label(container, SWT.NULL);
		label.setText(EMPTY);

		simpleLogger = new Button(container, SWT.RADIO);
		simpleLogger.setText(SLF4J_SIMPLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		simpleLogger.setLayoutData(gd);
		simpleLogger.addListener (SWT.Selection, listener);

		log4jLogger = new Button(container, SWT.RADIO);
		log4jLogger.setText(LOG4J);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		log4jLogger.setLayoutData(gd);
		log4jLogger.addListener (SWT.Selection, listener);

		jdk14Logger = new Button(container, SWT.RADIO);
		jdk14Logger.setText(JDK14);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		jdk14Logger.setLayoutData(gd);
		jdk14Logger.addListener (SWT.Selection, listener);

		logbackLogger = new Button(container, SWT.RADIO);
		logbackLogger.setText(LOGBACK);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		logbackLogger.setLayoutData(gd);
		logbackLogger.addListener (SWT.Selection, listener);
		
		label = new Label(container, SWT.NULL);
		label.setText(EMPTY);
		label = new Label(container, SWT.NULL);
		label.setText(EMPTY);
		label = new Label(container, SWT.NULL);
		label.setText(EMPTY);
		label = new Label(container, SWT.NULL);
		label.setText(EMPTY);
		
		label = new Label(container, SWT.NULL);
		label.setText(ADD_EXCEPTION_FRAMWORK_LABEL);
		enableException = new Button(container, SWT.CHECK);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		enableException.setLayoutData(gd);
		enableException.addSelectionListener(new SelectionListener() {			
			@Override
			public void widgetSelected(SelectionEvent e) {
				isExceptionEnabled = enableException.getSelection();				
			}			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {				
			}
		});

		initialize();
		setControl(container);
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
		enableLog.setSelection(true);
		log4jLogger.setSelection(true);
		enableException.setSelection(true);
	}

	public boolean isLogEnabled() {
		return isLogEnabled;
	}

	public boolean isSimpleLogger() {
		return isSimpleLogger;
	}

	public boolean isLog4jLogger() {
		return isLog4jLogger;
	}

	public boolean isJdk14Logger() {
		return isJdk14Logger;
	}

	public boolean isLogbackLogger() {
		return isLogbackLogger;
	}

	public boolean isExceptionEnabled() {
		return isExceptionEnabled;
	}

}