package sing.camera.library.listener;

import android.graphics.Bitmap;

public interface SCameraListener {

    void captureSuccess(Bitmap bitmap);
    void recordSuccess(String url, Bitmap firstFrame);
}
