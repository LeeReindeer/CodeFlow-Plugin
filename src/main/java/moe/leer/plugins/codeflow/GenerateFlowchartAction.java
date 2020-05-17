package moe.leer.plugins.codeflow;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import guru.nidi.graphviz.engine.Format;
import moe.leer.codeflowcore.CodeFlow;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

/**
 * @author leer
 * Created at 12/18/19 7:00 PM
 */
public class GenerateFlowchartAction extends AnAction {

  public static final Logger LOG = Logger.getInstance(GenerateFlowchartAction.class);

  public static final CodeFlow codeFlow = CodeFlow.builder()
      .format(Format.PNG)
      .useNative(true)
      .build();

  @Override
  public void update(@NotNull AnActionEvent e) {
    final Project project = e.getProject();
    final Editor editor = e.getData(CommonDataKeys.EDITOR);
    e.getPresentation().setEnabledAndVisible(project != null &&
        editor != null &&
        editor.getSelectionModel().hasSelection());
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    final Project project = e.getProject();
    final Editor editor = e.getData(CommonDataKeys.EDITOR);
    final Document document = editor.getDocument();
    Caret primaryCaret = editor.getCaretModel().getPrimaryCaret();
    String code = primaryCaret.getSelectedText();

    BufferedImage image = null;
    try {
      image = codeFlow.parse(code).toImage();
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    CodeFlowPluginController controller = CodeFlowPluginController.getInstance(e.getProject());
    if (controller != null) {
      controller.flowchartWindow.show(null);
      controller.startGenerateFlowchartEvent(image);
    } else {
      LOG.error("controller is null");
    }
    /*
    FlowchartPopup popup = new FlowchartPopup();
    JPanel panel = popup.getPopup();
    JPanel imagePanel = popup.getImagePanel();
    imagePanel.removeAll();
    imagePanel.add(new JLabel(new ImageIcon(image)));
    JBPopupFactory.getInstance()
        .createComponentPopupBuilder(panel, panel)
        .createPopup().showInBestPositionFor(editor);
     */
  }
}
