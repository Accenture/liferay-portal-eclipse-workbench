# liferay-portal-eclipse-workbench
Liferay Eclipse workbench is a tool which helps developers to auto create Liferay portlet plugins based on Spring MVC framework. It creates the complete project anatomy (based on Maven/ant build type selection) for Liferay spring MVC portlet along with auto inclusion of all necessary jar files, configurations, deployment descriptors, base exception handling and logging Framework enablement. It also has a provision to create a Liferay plugin project and add multiple Spring MVC portlets to it

Qualitative and Quantitative Benefits:

1. Workbench assists in creating deployable Spring portlet within few clicks.

2. Provision to create Maven as well as Ant based plugins

3. No need to manually configure and download related jar files

4. Auto creation of all spring related configuration files

5. Auto updating of Liferay Deployment descriptors for Spring portlet  related configurations

6. Auto creation of Project anatomy with Exception Handling, Logging Framework enablement


Usage Guidelines:

1. Import this project into Eclipse as ‘General->Existing Project into Workspace’
2. Right click on the imported project  and Click on ‘Export’ 
3. Select Plug-in development option in the opened wizard and then Click on ‘Deployable plug-ins and fragments’
4. Another wizard will open listing down all available plugins
5. Please select ‘Liferay-portal-eclipse-workbench’ plugin and provide the path to the folder where you want to export the plugin (it will be in a jar format)
6. Click on Finish button and check if the plugin is exported in the mentioned folder path.
7. Once plugin is available, Copy the downloaded jar inside you local Eclipse installation – /eclipse/plugins/ folder
8. Start eclipse
9. Go to File -> New -> Other in eclipse
10. You will see Liferay Spring Portlet Wizards option in list.
11. Select the appropriate option and start creating your spring MVC portlet
