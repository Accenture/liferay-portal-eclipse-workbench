package com.accenture.liferayspringplugin.wizards.plugin;

import static com.accenture.liferayspringplugin.wizards.Constants.ANT_BUTTON_LABEL;
import static com.accenture.liferayspringplugin.wizards.Constants.BUILD_TYPE_LABEL;
import static com.accenture.liferayspringplugin.wizards.Constants.EMPTY;
import static com.accenture.liferayspringplugin.wizards.Constants.MAVEN_BUTTON_LABEL;
import static com.accenture.liferayspringplugin.wizards.Constants.PLUGIN_WIZARD_PAGE_DESCRIPTION;
import static com.accenture.liferayspringplugin.wizards.Constants.PLUGIN_WIZARD_PAGE_TITLE;
import static com.accenture.liferayspringplugin.wizards.Constants.WIZARD_PAGE;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import com.accenture.liferayspringplugin.wizards.LiferaySpringWizard;

/**
 * The "LiferaySpring" wizard page allows setting the portlet/project name and
 * other required settings.
 * 
 * @author Digital Liferay Capability IDC
 */

public class PluginTypeWizardPage extends WizardPage {
	private Button antButton;
	private Button mavenButton;

	private boolean isAntPlugin = false;

	private LiferaySpringWizard liferaySpringWizard;
	private LiferaySpringAntWizardPage antPage;
	private LiferaySpringMavenWizardPage mvnPage;

	/**
	 * Constructor for SampleNewWizardPage.
	 * @param liferaySpringWizard 
	 * @param mvnPage 
	 * @param antPage 
	 * 
	 * @param pageName
	 */
	public PluginTypeWizardPage(LiferaySpringWizard liferaySpringWizard, LiferaySpringAntWizardPage antPage, LiferaySpringMavenWizardPage mvnPage) {
		super(WIZARD_PAGE);
		setTitle(PLUGIN_WIZARD_PAGE_TITLE);
		setDescription(PLUGIN_WIZARD_PAGE_DESCRIPTION);
		this.liferaySpringWizard = liferaySpringWizard;
		this.antPage = antPage;
		this.mvnPage = mvnPage;
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 4;
		layout.verticalSpacing = 12;
		
		Label label = new Label(container, SWT.NULL);
		label.setText(EMPTY);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 4;
		label.setLayoutData(gd);

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
				
				isAntPlugin = antButton.getSelection();
				if(isAntPlugin) {
					mvnPage.setPageComplete(true);
					antPage.dialogChanged();
				} else {
					antPage.setPageComplete(true);
					mvnPage.dialogChanged();
				}
			}
		};
		
		label = new Label(container, SWT.NULL);
		label.setText(BUILD_TYPE_LABEL);
		
		mavenButton = new Button(container, SWT.RADIO);
		mavenButton.setText(MAVEN_BUTTON_LABEL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		mavenButton.setLayoutData(gd);
		mavenButton.addListener (SWT.Selection, listener);
		antButton = new Button(container, SWT.RADIO);
		antButton.setText(ANT_BUTTON_LABEL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		antButton.setLayoutData(gd);
		antButton.addListener (SWT.Selection, listener);
		label = new Label(container, SWT.NULL);
		label.setText(EMPTY);

		initialize();
		setControl(container);
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
		mavenButton.setSelection(true);
		antPage.setPageComplete(true);
	}

	public boolean isAntPlugin() {
		return isAntPlugin;
	}

	@Override
	public IWizardPage getNextPage() {
		IWizardPage nextPage = super.getNextPage();
		if(isAntPlugin) {
			return liferaySpringWizard.getNextPage(nextPage);
		}
		return nextPage;
	}
}