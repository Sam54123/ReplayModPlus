package com.igrium.replayeditorplus.ui;

import com.igrium.craftfx.util.ThreadUtils;
import com.igrium.craftfx.viewport.EngineViewport;
import com.igrium.craftfx.viewport.PrimaryViewport;
import com.igrium.craftfx.viewport.StandardInputController;
import com.igrium.replayeditorplus.ReplayEditor;
import com.igrium.replayeditorplus.ui.controls.TimelineUI;

import javafx.fxml.FXML;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class ReplayEditorUI {
    public static final String FXML = "/assets/replayeditorplus/ui/editor_ui.fxml";

    @FXML
    private PrimaryViewport primaryViewport;

    @FXML
    private TimelineUI timeline;

    private StandardInputController<?> inputController;
    protected ReplayEditor editor;

    public final EngineViewport getPrimaryViewport() {
        return primaryViewport;
    }

    public final StandardInputController<?> getInputController() {
        return inputController;
    }

    public ReplayEditor getEditor() {
        return editor;
    }

    /**
     * Internal use only
     */
    @Deprecated
    public void setEditor(ReplayEditor editor) {
        this.editor = editor;
        timeline.timeProperty().bind(editor.replayProperties().gameTimestamp());
    }
    

    @FXML
    protected void initialize() {
        inputController = new StandardInputController<EngineViewport>(primaryViewport);
        timeline.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.PRIMARY) ThreadUtils.onRenderThread(() -> {
                editor.getReplayHandler().doJump((int) (timeline.getTimeAt(event.getX()) * 1000), true);
            });
        });
    }

    public void close() {
        primaryViewport.close();
        inputController.close();
    }
}
