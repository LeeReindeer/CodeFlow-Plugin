package moe.leer.plugins.codeflow;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;

/**
 * @author leer
 * Created at 12/31/19 2:03 PM
 */
public class CodeFlowPluginController implements ProjectComponent {

  public static final Logger LOG = Logger.getInstance(CodeFlowPluginController.class);

  public Project project;
  public static final String FLOWCHART_WIN_ID = "CodeFlow";
  public ToolWindow flowchartWindow;
  public ScrollScaleImagePanel flowchartPanel;

  public CodeFlowPluginController(Project project) {
    this.project = project;
  }

  @Nullable
  public static CodeFlowPluginController getInstance(Project project) {
    if (project == null) {
      LOG.error("getInstance: project is null");
      return null;
    }
    CodeFlowPluginController pc = project.getComponent(CodeFlowPluginController.class);
    if (pc == null) {
      LOG.error("getInstance: getComponent() for " + project.getName() + " returns null");
    }
    return pc;
  }

  @Override
  public void projectOpened() {
    createToolWindows();
  }

  @Override
  public void projectClosed() {
    flowchartPanel = null;
    flowchartWindow = null;
    project = null;
  }

  @NotNull
  @Override
  public String getComponentName() {
    return "codeflow.ProjectComponent";
  }

  private void createToolWindows() {
    LOG.info("createToolWindows " + project.getName());
    ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
    flowchartPanel = new ScrollScaleImagePanel();

    ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
    Content content = contentFactory.createContent(flowchartPanel, "", false);
    content.setCloseable(false);

    flowchartWindow = toolWindowManager.registerToolWindow(FLOWCHART_WIN_ID, true, ToolWindowAnchor.RIGHT);
    flowchartWindow.getContentManager().addContent(content);
    flowchartWindow.setIcon(Icons.getToolWindow());
  }

  public void startGenerateFlowchartEvent(BufferedImage image) {
    if (flowchartPanel != null) {
      flowchartPanel.setImage(image);
    }
  }
}
