import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Image {
    public static void main(String[] args){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat img = getImg("testimg1.jpg");
        Mat newImg = new Mat();
        Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);

    }

    public static Mat getImg(String path){
        return Imgcodecs.imread("testimg1.jpg");
    }

    public static void showImg(Mat frame){
        HighGui.imshow("Image", frame);
        HighGui.waitKey();
    }
}
