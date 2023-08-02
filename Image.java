import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Image {
    public static void main(String[] args){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat img = getImg("testimg1.jpg");
        Mat tempImg = getImg("testimg1.jpg");
        Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);
        Imgproc.Sobel(img, tempImg, -1, 0, 1, -1);
        Imgproc.Sobel(img, img, -1, 1, 0, -1);
        Core.subtract(tempImg, img, img);
        //Imgproc.blur(img, img, new Size(9,9));
        //Imgproc.threshold(img, img, 225, 255, 0);
        //tempImg = dilation(img);
        showImg(img);
    }

    public static Mat getImg(String path){
        return Imgcodecs.imread("testimg1.jpg");
    }

    public static void showImg(Mat frame){
        HighGui.imshow("Image", frame);
        HighGui.waitKey();
    }

    public static Mat dilation(Mat theImg){
        Mat se = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(100,7));
        Imgproc.morphologyEx(theImg, theImg,Imgproc.MORPH_CLOSE, se);
        Imgproc.erode(theImg, theImg, se, new Point(4,4));
        Imgproc.dilate(theImg, theImg, se, new Point(4,4));
        return theImg;
    }
}
