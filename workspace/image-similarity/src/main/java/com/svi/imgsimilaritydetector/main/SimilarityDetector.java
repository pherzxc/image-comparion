package com.svi.imgsimilaritydetector.main;

import org.opencv.core.Core;
import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.*;
import org.opencv.features2d.DescriptorMatcher;

import org.opencv.imgcodecs.Imgcodecs;

public class SimilarityDetector {

	public static void main(String[] args) {
		String image1 = "C:/Users/jmata/Desktop/bmp/0001.bmp";
		String image2 = "C:/Users/jmata/Desktop/bmp/10002.bmp";

		
		boolean result = isImagesSimilar(image1, image2);
		System.out.println(result == true? "Images are same" : "Images are not the same");

	}
	
	public static boolean isImagesSimilar(String image1, String image2) {
		boolean value = false;
		int accurateCount = 0;
		int inaccurateCount = 0;
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		FeatureDetector detector = FeatureDetector.create(FeatureDetector.ORB);
	    DescriptorExtractor extractor  = DescriptorExtractor.create(DescriptorExtractor.ORB);
	    DescriptorMatcher matcher = BFMatcher.create(BFMatcher.BRUTEFORCE_HAMMING);
	    
	    Mat img1 = Imgcodecs.imread(image1);
	    Mat descriptors1 = new Mat();
	    MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
	    
		Mat img2 = Imgcodecs.imread(image2);
		Mat descriptors2 = new Mat();
	    MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
	    
	    detector.detect(img1, keypoints1);
	    detector.detect(img2, keypoints2);
	    
	    extractor.compute(img1, keypoints1, descriptors1);
	    extractor.compute(img2, keypoints2, descriptors2);
	    
	    MatOfDMatch matches = new MatOfDMatch();
		
	    
		if(descriptors2.cols() == descriptors1.cols()) {
			matcher.match(descriptors1, descriptors2, matches);

			DMatch[] match = matches.toArray();

			double max_dist = 0;
			double min_dist = 100;

			for (int i = 0; i < descriptors1.rows(); i++) {
				System.out.println(match[i].distance);
				double dist = match[i].distance;
				if(dist < min_dist) {
					min_dist = dist;
				}
				if(dist > max_dist) {
					max_dist = dist;
				}

			}
			System.out.println("max_dist= " + max_dist + ", min dist= " + min_dist);
			
			double thresholdDistance = 5 * min_dist;
			
			for (int i = 0; i < match.length; i++) {
				if (match[i].distance <= thresholdDistance) {
					accurateCount++;
				} else if (match[i].distance > thresholdDistance) {
					inaccurateCount++;
				}
			}

			System.out.println("matching count = " + match.length);
			System.out.println("matching count = " + accurateCount);
			
			if (accurateCount > 5) {
				value = true;
			} else {
				value = false;
			}
			
		} 
		return value;
	}

}
