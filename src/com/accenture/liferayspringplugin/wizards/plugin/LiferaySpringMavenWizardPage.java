package com.accenture.liferayspringplugin.wizards.plugin;

import static com.accenture.liferayspringplugin.wizards.Constants.*;

import java.io.File;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.accenture.liferayspringplugin.wizards.LiferaySpringWizard;

/**
 * The "LiferaySpring" wizard page allows setting the portlet/project name and
 * other required settings.
 * 
 * @author Digital Liferay Capability IDC
 */

public class LiferaySpringMavenWizardPage extends WizardPage {
	private Text groupId;
	private Text version;
	private Text artifactId;
	private Text sdkHome;
	private Text jspPath;
	private Text displayCategry;

	private Label groupIdLabel;
	private Label artifactIdLabel;
	private Label versionLabel;

	private LiferaySpringWizard liferaySpringWizard;

	/**
	 * Constructor for SampleNewWizardPage.
	 * @param liferaySpringWizard 
	 * @param pageName
	 */
	public LiferaySpringMavenWizardPage(LiferaySpringWizard liferaySpringWizard) {
		super(WIZARD_PAGE);
		setTitle(PLUGIN_WIZARD_PAGE_TITLE);
		setDescription(PLUGIN_WIZARD_PAGE_DESCRIPTION);
		this.liferaySpringWizard = liferaySpringWizard;
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
		label.setText(LOCATION_PATH_LABEL);

		sdkHome = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		sdkHome.setLayoutData(gd);
		sdkHome.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		Button button = new Button(container, SWT.PUSH);
		button.setText(BROWSE_BUTTON_LABEL);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});

		groupIdLabel = new Label(container, SWT.NULL);
		groupIdLabel.setText(GROUP_ID_LABEL);

		groupId = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		groupId.setLayoutData(gd);
		groupId.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		label = new Label(container, SWT.NULL);
		label.setText(EMPTY);

		artifactIdLabel = new Label(container, SWT.NULL);
		artifactIdLabel.setText(ARTIFACT_ID_LABLE);

		artifactId = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		artifactId.setLayoutData(gd);
		artifactId.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		label = new Label(container, SWT.NULL);
		label.setText(EMPTY);
		versionLabel = new Label(container, SWT.NULL);
		versionLabel.setText(VERSION_LABEL);

		version = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		version.setLayoutData(gd);
		version.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		label = new Label(container, SWT.NULL);
		label.setText(EMPTY);

		label = new Label(container, SWT.NULL);
		label.setText(_JSP_PATH_LABEL);

		jspPath = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		jspPath.setLayoutData(gd);
		jspPath.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		label = new Label(container, SWT.NULL);
		label.setText(EMPTY);
		label = new Label(container, SWT.NULL);
		label.setText(_DISPLAY_CATEGORY_LABEL);

		displayCategry = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		displayCategry.setLayoutData(gd);
		displayCategry.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		initialize();
		dialogChanged();
		setControl(container);
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
		jspPath.setText(DEFAULT_JSP_FOLDER);
		displayCategry.setText(DEFAULT_DISPLAY_CATEGORY);
		String path = ResourcesPlugin.getWorkspace().getRoot().getLocation()
				.toString();
		sdkHome.setText(path);

		groupId.setMessage(COM_DOT_ACCENTURE);
		version.setMessage(DEFAULT_VERSION);
	}

	/**
	 * Uses the standard container selection dialog to choose the new value for
	 * the container field.
	 */

	private void handleBrowse() {
		DirectoryDialog dialog = new DirectoryDialog(getShell());
		dialog.setText(LIFERAY_SDK_HOME);
		dialog.setMessage(SELECT_LIFERAY_SDK_PATH);
		dialog.setFilterPath(getSdkHome());
		String path = dialog.open();
		if (path != null && path.length() > 0) {
			sdkHome.setText(path);
		}
	}

	/**
	 * Ensures that both text fields are set.
	 */

	public void dialogChanged() {
		if (!isValidSdkPath(getSdkHome())) {
			return;
		}
		if (getProjectName().length() > 0 && !getProjectName().matches(PACKAGE_NAME_REGEX)) {
			updateStatus(GROUP_ID_ERROR);
			return;
		}
		if(getPortletName().length() == 0) {
			updateStatus(ARTIFACT_ID_REQUIRED);
			return;
		}
		if (getPortletName().length() > 0 && !getPortletName().matches(ARTIFACT_ID_NAME_REGEX)) {
			updateStatus(ARTIFACT_ID_ERROR);
			return;
		}

		if (getPackageName().length() > 0 && !getPackageName().matches(VERSION_NAME_REGEX)) {
			updateStatus(VERSION_NUMBER_ERROR);
			return;
		}
		if (getJspPath().length() == 0) {
			updateStatus(VIEW_PATH_ERROR);
			return;
		}
		if (getDisplayCategry().length() == 0) {
			updateStatus(DISPLAY_CATEGORY_ERROR);
			return;
		}
		updateStatus(null);
	}

	/**
	 * 
	 * @param sdkPath
	 * @return
	 */
	private boolean isValidSdkPath(String sdkPath) {
		if (sdkPath.length() == 0) {
			updateStatus(LOCATION_PATH_REQUIRED);
			return false;
		} else {
			File file = new File(sdkPath);
			if (file.isDirectory()) {
				return true;
			} else {
				updateStatus(LOCATION_PATH_INVALID);
				return false;
			}
		}
	}

	/**
	 * 
	 * @param message
	 */
	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getProjectName() {
		return groupId.getText();
	}

	public String getGroupId() {
		if(groupId.getText().trim().length() == 0) {
			return groupId.getMessage();
		}
		return groupId.getText();
	}	

	public String getPackageName() {
		if(version.getText().trim().length() == 0) {
			return version.getMessage();
		}
		return version.getText();
	}

	public String getPortletName() {
		if(artifactId.getText().trim().length() == 0) {
			return getPortletMessage();
		}
		return artifactId.getText();
	}

	public String getPortletMessage() {
		return artifactId.getMessage();
	}

	public String getJspPath() {
		return jspPath.getText();
	}

	public String getDisplayCategry() {
		return displayCategry.getText();
	}

	public String getSdkHome() {
		return sdkHome.getText();
	}

	@Override
	public IWizardPage getNextPage() {
		return liferaySpringWizard.getNextPage(super.getNextPage());
	}
}