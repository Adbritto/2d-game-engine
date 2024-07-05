package emerald;

import java.awt.event.KeyEvent;

public class LevelScene extends Scene {

    private boolean changingScene = false;
    private float timeToChangeScene = 2.0f;

    public LevelScene() {
        System.out.println("Inside Level Scene");
        Window.get().r = 0;
        Window.get().b = 0;
        Window.get().g = 1;
    }

    @Override
    public void update(float dt) {
        if (!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            changingScene = true;
        }

        if (changingScene && timeToChangeScene > 0) {
            timeToChangeScene -= dt;
            Window.get().r -= dt * 5.0f;
            Window.get().g -= dt * 5.0f;
            Window.get().b -= dt * 5.0f;
        } else if (changingScene) {
            Window.changeScene(0);
            timeToChangeScene = 2.0f;
        }
    }
}
