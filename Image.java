import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class Image {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat img = getImg("testimg1.png");
        Mat outputImg = getImg("testimg1.png");
        turnGray(img);
        gradientSubtract(img);
        setPixelThreshold(img);
        dilationAndErosion(img);
        contourFind(img, outputImg);
        showImg(outputImg);
    }

    //This function simple reads in an image from your drive
    public static Mat getImg(String path) {
        return Imgcodecs.imread("testimg1.png");
    }

    //This function displays the image in a GUI on your computer
    public static void showImg(Mat frame) {
        HighGui.imshow("Image", frame);
        HighGui.waitKey();
    }

    //This function turns image gray which is step one in image processing
    public static Mat turnGray(Mat theImg) {
        Imgproc.cvtColor(theImg, theImg, Imgproc.COLOR_BGR2GRAY);
        return theImg;
    }

    //This function is will sharpen the edges of the image to make it easier to find barcodes
    public static Mat gradientSubtract(Mat theImg) {
        Mat tempX = new Mat();
        Mat tempY = new Mat();
        //These two functions will find the edges of an image both in vertical and horizontal
        Imgproc.Sobel(theImg, tempX, CvType.CV_64F, 1, 0, -1);
        Imgproc.Sobel(theImg, tempY, CvType.CV_64F, 0, 1, -1);
        //After we subtract the edges we find with each other leading to an image with higher horizontal gradients
        Core.subtract(tempX, tempY, theImg);
        Core.convertScaleAbs(theImg, theImg);
        //This blur functions removes any high frequency noise in the image
        Imgproc.blur(theImg, theImg, new Size(9, 9));
        return theImg;
    }

    //In this function we set some rgb values so that only pixels that are white will stand out
    public static Mat setPixelThreshold(Mat theImg) {
        Imgproc.threshold(theImg, theImg, 225, 255, Imgproc.THRESH_BINARY);
        return theImg;
    }

    /*This function removes any random white dots that may still appear on the image, so contour function will not
    be confused*/
    public static Mat dilationAndErosion(Mat theImg) {
        Mat se = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(100, 7));
        Imgproc.morphologyEx(theImg, theImg, Imgproc.MORPH_CLOSE, se);
        Imgproc.erode(theImg, theImg, se, new Point(4, 4));
        Imgproc.dilate(theImg, theImg, se, new Point(4, 4));
        return theImg;
    }

    //This function finds the contours that are now easily visible after image processing, and draws them on to the img
    public static Mat contourFind(Mat theImg, Mat result) {
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(theImg, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        List<MatOfPoint> finalContours = new ArrayList<>();
        Imgproc.drawContours(result, contours, -1, new Scalar(0, 255, 0), 3);
        return result;
    }

    //this function is for processing the contours one by one(used in testing)
    public static Mat drawContour(Mat inputImg, MatOfPoint contour){
        Mat finalImg = inputImg.clone();
        List<MatOfPoint> contourList = new ArrayList<>();
        contourList.add(contour);
        Imgproc.drawContours(finalImg, contourList, -1, new Scalar(0, 255, 0), 3);
        return finalImg;
    }

    //Function is not called, but it will save each contour as its own image on to your computer(used in testing)
    public static void saveImages(List<MatOfPoint> contours, Mat inputImg){
        int count = 0;
        for(MatOfPoint contour : contours){
            Mat resImg = drawContour(inputImg, contour);
            String outputFileName = "Contour-" + count + ".png";
            Imgcodecs.imwrite(outputFileName, resImg);
        }
    }
}
