import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class Image {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat img = getImg("testimg1.jpg");
        Mat outputImg = getImg("testimg1.jpg");
        img = turnGray(img);
        img = gradientSubtract(img);
        img = setPixelThreshold(img);
        img = dilationAndErosion(img);
        outputImg = contourFinish(img, outputImg);
        showImg(outputImg);
    }

    public static Mat getImg(String path) {
        return Imgcodecs.imread("testimg1.jpg");
    }

    public static void showImg(Mat frame) {
        HighGui.imshow("Image", frame);
        HighGui.waitKey();
    }

    public static Mat turnGray(Mat theImg) {
        Imgproc.cvtColor(theImg, theImg, Imgproc.COLOR_BGR2GRAY);
        return theImg;
    }

    public static Mat gradientSubtract(Mat theImg) {
        Mat tempX = new Mat();
        Mat tempY = new Mat();
        Imgproc.Sobel(theImg, tempX, CvType.CV_64F, 1, 0, -1);
        Imgproc.Sobel(theImg, tempY, CvType.CV_64F, 0, 1, -1);
        Core.subtract(tempX, tempY, theImg);
        Core.convertScaleAbs(theImg, theImg);
        Imgproc.blur(theImg, theImg, new Size(9, 9));
        return theImg;
    }

    public static Mat setPixelThreshold(Mat theImg) {
        Imgproc.threshold(theImg, theImg, 225, 255, Imgproc.THRESH_BINARY);
        return theImg;
    }

    public static Mat dilationAndErosion(Mat theImg) {
        Mat se = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(100, 7));
        Imgproc.morphologyEx(theImg, theImg, Imgproc.MORPH_CLOSE, se);
        Imgproc.erode(theImg, theImg, se, new Point(4, 4));
        Imgproc.dilate(theImg, theImg, se, new Point(4, 4));
        return theImg;
    }

    public static Mat contourFinish(Mat theImg, Mat result) {
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(theImg, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        MatOfPoint largestContour = contours.get(0);
        for (MatOfPoint contour : contours) {
            if (Imgproc.contourArea(contour) > Imgproc.contourArea(largestContour)) {
                largestContour = contour;
            }
        }
        List<MatOfPoint> finalContours = new ArrayList<>();
        finalContours.add(largestContour);
        Imgproc.drawContours(result, finalContours, -1, new Scalar(0, 255, 0), 3);
        return result;
    }
}
